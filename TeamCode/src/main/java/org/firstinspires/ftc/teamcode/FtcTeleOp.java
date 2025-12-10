package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotState.currentPose;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.Shoot;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Indicators;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;
import org.firstinspires.ftc.teamcode.utils.AimGoalPID;

import java.util.function.Supplier;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.hardware.impl.ServoEx;

import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.bindings.Bindings.range;

@TeleOp
public class FtcTeleOp extends NextFTCOpMode {
    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    public static boolean aimAtGoal = false;
    Button gamepad1leftBumper = button(() -> gamepad1.left_bumper);
    Button gamepad1RightTrigger = button(() -> gamepad1.right_trigger > 0.5);
    Button gamepad1LeftTrigger = button(() -> gamepad1.left_trigger > 0.5);
    Button gamepad1dpadUp = button(() -> gamepad1.dpad_up);
    Button gamepad1dpadDown = button(() -> gamepad1.dpad_down);
    Button gamepad1x = button(() -> gamepad1.cross);
    Button gamepad1Tri = button(() -> gamepad1.triangle);
    Button gamepad1Square = button(() -> gamepad1.square);
    Button gamepad1Circle = button(() -> gamepad1.circle);

    Range leftStickY = range(() -> -gamepad1.left_stick_y).deadZone(Constants.controllerDeadband);
    Range leftStickX = range(() -> -gamepad1.left_stick_x).deadZone(Constants.controllerDeadband);
    Range rightStickX = range(() -> -gamepad1.right_stick_x).deadZone(Constants.controllerDeadband);


    AimGoalPID aimGoalPID;

    ServoEx indicator1 = new ServoEx("indicator1");
    ServoEx indicator2 = new ServoEx("indicator2");


    public FtcTeleOp(){
        addComponents(
                new SubsystemComponent(Intake.INSTANCE,
                        Shooter.INSTANCE,
                        VisionLL.INSTANCE,
                        Indicators.INSTANCE,
                        Gate.INSTANCE),
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onInit() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        Shooter.INSTANCE.shooterEnabled = false;
        aimGoalPID = new AimGoalPID();
        PedroComponent.follower().update();

        pathChain = () -> PedroComponent.follower().pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(PedroComponent.follower()::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(PedroComponent.follower()::getHeading, Math.toRadians(45), 0.8))
                .build();

        configureBindings();
    }

    private void configureBindings() {
        // Toggle aim at goal when left bumper is pressed (this eventually will happen w/o driver input)

        gamepad1x.whenBecomesTrue(new InstantCommand(Intake.INSTANCE::toggleIntake));
        gamepad1Circle.whenBecomesTrue(new InstantCommand(()->aimAtGoal = !aimAtGoal));
        gamepad1Tri.whenBecomesTrue(new InstantCommand(()->PedroComponent.follower().setPose(new Pose(0,0,0))));
        gamepad1dpadUp.whenBecomesTrue(Shooter.INSTANCE.shooterOnFar);
        gamepad1dpadDown.whenBecomesTrue(Shooter.INSTANCE::toggleAdaptive);


        gamepad1RightTrigger.whenBecomesTrue(Shoot.shoot1());
        gamepad1LeftTrigger.whenBecomesTrue(Shoot.shoot3());
    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setStartingPose(currentPose == null ? new Pose() : currentPose); // Take leftover pose from auto
        PedroComponent.follower().startTeleopDrive(true);
        Gate.INSTANCE.gateToOpenPos.schedule();
        Shooter.INSTANCE.shooterEnabled = true;
    }

    @Override
    public void onUpdate() {
        //Call this once per loop
        telemetryM.update();
        PedroComponent.follower().update();
        BindingManager.update();

        // Update Indicators
        LLResultTypes.FiducialResult blueTag= VisionLL.INSTANCE.getTag(20);
        LLResultTypes.FiducialResult redTag= VisionLL.INSTANCE.getTag(24);
        if (blueTag != null){
            if (Math.abs(blueTag.getTargetXDegrees()) + Constants.AutoAimConstants.blueOffset < 5.0) {
                indicator1.setPosition(0.5);
                indicator2.setPosition(0.5);
            } else {
                indicator1.setPosition(0.3);
                indicator2.setPosition(0.3);
            }

        } else if (redTag != null){
            if (Math.abs(redTag.getTargetXDegrees()) + Constants.AutoAimConstants.redOffset < 5.0) {
                indicator1.setPosition(0.5);
                indicator2.setPosition(0.5);
            } else {
                indicator1.setPosition(0.3);
                indicator2.setPosition(0.3);
            }
        }
        else {
            indicator2.setPosition(0);
            indicator1.setPosition(0);
        }

        // Drive Commands
        if (!automatedDrive) {
            if (aimAtGoal) {
                PedroComponent.follower().setTeleOpDrive(leftStickY.get(),
                        leftStickX.get(),
                        aimGoalPID.calculate(),
                        true);
            } else {
                PedroComponent.follower().setTeleOpDrive(
                        leftStickY.get(),
                        leftStickX.get(),
                        rightStickX.get(),
                        true);
            }
        }

        //Stop automated following if the follower is done
        if (automatedDrive && !PedroComponent.follower().isBusy()) {
            PedroComponent.follower().startTeleopDrive();
            automatedDrive = false;
        }


        telemetryM.debug("position", PedroComponent.follower().getPose());
        telemetryM.debug("velocity", PedroComponent.follower().getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
    }

    @Override
    public void onStop() {
        BindingManager.reset();
        Shooter.INSTANCE.shooterEnabled = false;
    }

}
//Path Diagram for each of the classes and explain the sensor associated with the class,
// the library's needed, the variables in the class, the overall function of the class to just make the robot drive.


