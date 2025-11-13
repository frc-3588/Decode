package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.maxGoalAngle;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.maxRange;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.PIDEx;
import com.ThermalEquilibrium.homeostasis.Controllers.Feedforward.BasicFeedforward;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.teamcode.utils.DebouncedButton;
import org.firstinspires.ftc.teamcode.utils.DetectInLaunchZone;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import dev.nextftc.bindings.Button;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.IfElseCommand;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Shooter implements Subsystem {
    MotorEx motor1 = new MotorEx("shooter1");
    ServoEx gate = new ServoEx("gate");
    TelemetryManager telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
    public boolean autoEnable = false;
    public static final Shooter INSTANCE = new Shooter() {};
    private Shooter() { }
    private double targetVelocity = 0;
    private final ControlSystem controller = ControlSystem.builder()
            .velPid(0.005, 0, 0)
            .basicFF(0.01, 0.02, 0.03)
            .build();
    public final Command shooterOff = new RunToVelocity(controller, 0.0).requires(this).named("FlywheelOff");
    public final Command shooterOn = new RunToVelocity(controller, targetVelocity).requires(this).named("FlywheelOn");
    DebouncedButton loadedSensor;
    NormalizedColorSensor loadedDetector;

    public Command shoot3 = new SequentialGroup(
            Shooter.INSTANCE.gateToFirePos,
            new Delay(0.5),
            Shooter.INSTANCE.gateToOpenPos,
            new Delay(0.5),
            Shooter.INSTANCE.gateToFirePos,
            new Delay(0.5),
            Shooter.INSTANCE.gateToOpenPos,
            new Delay(0.5),
            Shooter.INSTANCE.gateToFirePos,
            new Delay(0.5),
            Shooter.INSTANCE.gateToOpenPos,
            new Delay(0.5)
    );



    @Override
    public void periodic() {
        motor1.setPower(controller.calculate(motor1.getState()));
        if (autoEnable && loadedSensor.get() && motor1.getVelocity() < 0.1){
            shooterOn.schedule();
        } else if (autoEnable && !loadedSensor.get() && motor1.getVelocity() > 0.1){
            shooterOff.schedule();
        }

        telemetryManager.addData("Current Shooter Velocity", motor1.getVelocity());
        telemetryManager.addData("Target Shooter Velocity", targetVelocity);
        telemetryManager.addData("Shooter Power", motor1.getPower());
    }

    @Override
    public void initialize() {
        loadedDetector = ActiveOpMode.hardwareMap().get(NormalizedColorSensor.class, "loadedSensor");
        gate.getServo().setDirection(Constants.ShooterConstants.gateDir);

        loadedSensor = new DebouncedButton(
                Constants.ShooterConstants.loadedDetectorDebounceDelay,
                () -> ((OpticalDistanceSensor) loadedSensor)
                                .getLightDetected() > Constants.ShooterConstants.loadDetectionLightCutoff);
    }

    public void onStart(){
        gate.setPosition(Constants.ShooterConstants.gateInitPosition);
    }
    public Command gateToFirePos =
            new InstantCommand(
                    ()-> gate.setPosition(Constants.ShooterConstants.gateShootPosition))
            .requires(this);
    public Command gateToOpenPos =
            new InstantCommand(
                    ()-> gate.setPosition(Constants.ShooterConstants.gateInitPosition))
            .requires(this);
    public Command toggleGate = new IfElseCommand(()-> gate.getPosition() == Constants.ShooterConstants.gateInitPosition,
            gateToFirePos,
            gateToOpenPos).requires(this);

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
