package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.maxGoalAngle;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.maxRange;

import com.bylazar.opmodecontrol.ActiveOpMode;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorMRRangeSensor;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.teamcode.utils.DebouncedButton;
import org.firstinspires.ftc.teamcode.utils.DetectInLaunchZone;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Shooter implements Subsystem {
    public double getShooterVelocity() {

        return controller.getGoal().getVelocity();
    };

    public void toggleShooter(){
        if (controller.getGoal().getVelocity() > 100){
            Shooter.INSTANCE.reverseShooter.schedule();
        } else {
            Shooter.INSTANCE.shooterOnClose.schedule();
        }
    }
    MotorEx motor1 = new MotorEx("shooter1");
    TelemetryManager telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
    public boolean autoEnable = false;
    public static final Shooter INSTANCE = new Shooter() {};
    private Shooter() { }
    private final ControlSystem controller = ControlSystem.builder()
            .velPid(Constants.ShooterConstants.shooterPIDCoefficients)
            .basicFF(Constants.ShooterConstants.shooterFeedForward)
            .build();
    public final Command shooterOff = new RunToVelocity(controller, 0.0).requires(this).named("FlywheelOff");
    public final Command shooterOnClose = new RunToVelocity(controller, Constants.ShooterConstants.closeShootVelocity).requires(this).named("FlywheelOn");
    public final Command shooterOnMedium = new RunToVelocity(controller, Constants.ShooterConstants.mediumShootVelocity).requires(this).named("FlywheelOn");
    public final Command shooterOnFar = new RunToVelocity(controller, Constants.ShooterConstants.longShootVelocity).requires(this).named("FlywheelOn");

    public final Command reverseShooter = new RunToVelocity(controller, -100).requires(this).named("ReverseFlywheel");

    DebouncedButton loadedSensor;
    NormalizedColorSensor loadedDetector;



    @Override
    public void periodic() {
        motor1.setPower(controller.calculate(motor1.getState()));
//        if (autoEnable && loadedSensor.get() && motor1.getVelocity() < 0.1){
//            shooterOn.schedule();
//        } else if (autoEnable && !loadedSensor.get() && motor1.getVelocity() > 0.1){
//            shooterOff.schedule();
//        }

        telemetryManager.addData("Current Shooter Velocity", motor1.getVelocity());
        telemetryManager.addData("Shooter Power", motor1.getPower());
    }

    @Override
    public void initialize() {
//        loadedDetector = ActiveOpMode.hardwareMap().get(NormalizedColorSensor.class, "loadedSensor");
        loadedSensor = new DebouncedButton(
                Constants.ShooterConstants.loadedDetectorDebounceDelay,
                () -> (true));
        controller.setGoal(new KineticState(0,0,0));
    }
    public void onStart() {
        INSTANCE.reverseShooter.schedule();
    }




    /**
     * Ability to shoot depends on being in a valid position and motor speed
     * @return Is it safe to shoot?
     */
    public boolean canShoot(){
        // For shooter alignment we only use the april tag on the goal we are trying to shoot in
        AprilTagDetection detection;
        if (RobotState.isRed) {
            detection = Vision.INSTANCE.getDetectionById(24);
        } else {
            detection = Vision.INSTANCE.getDetectionById(20);
        }
        // If we can not see the april tag on the goal, we can not shoot
        if (detection == null){
            return false;
        }
        // Convert to Pedro Coords
        Pose pedroPose = new Pose(detection.robotPose.getPosition().x,
                detection.robotPose.getPosition().y,
                detection.robotPose.getOrientation().getYaw(),
                FTCCoordinates.INSTANCE).getAsCoordinateSystem(PedroCoordinates.INSTANCE);
        // Check 1: Is bot in launch zone?
        if (!DetectInLaunchZone.isInLaunchZone(pedroPose.getX(), pedroPose.getY())){
            return false;
        }
        // Check 2: Is bot in range?
        if (detection.ftcPose.range > maxRange){
            return false;
        }
        // Check 3: Is angle reasonable
        if (Math.abs(detection.ftcPose.bearing) > maxGoalAngle){
            return false;
        }

        // If all checks pass the bot can shoot
        return true;
    }


}
