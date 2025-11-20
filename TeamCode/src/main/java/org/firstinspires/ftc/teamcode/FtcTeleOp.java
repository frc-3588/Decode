package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotState.currentPose;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.Shoot;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Indicators;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Kicker;
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

import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.bindings.Bindings.range;

@TeleOp
public class FtcTeleOp extends NextFTCOpMode {

    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;
    private boolean aimAtGoal = false;
    private final double slowModeMultiplier = 0.5;
    Button gamepad1rightBumper = button(() -> gamepad1.right_bumper);
    Button gamepad1leftBumper = button(() -> gamepad1.left_bumper);
    Button gamepad1RightTrigger = button(() -> gamepad1.right_trigger > 0.5);
    Button gamepad1LeftTrigger = button(() -> gamepad1.left_trigger > 0.5);
    Button gamepad1Dpad = button(() -> gamepad1.dpad_up);
    Button gamepad1X = button(() -> gamepad1.cross);
    Button gamepad1Tri = button(() -> gamepad1.triangle);
    Button gamepad1Square = button(() -> gamepad1.square);
    Button gamepad1Circle = button(() -> gamepad1.circle);


    Range leftStickY = range(() -> -gamepad1.left_stick_y).deadZone(Constants.controllerDeadband);
    Range leftStickX = range(() -> -gamepad1.left_stick_x).deadZone(Constants.controllerDeadband);
    Range rightStickX = range(() -> -gamepad1.right_stick_x).deadZone(Constants.controllerDeadband);


    AimGoalPID aimGoalPID;

    public FtcTeleOp(){
        addComponents(
                new SubsystemComponent(Intake.INSTANCE,
                        Shooter.INSTANCE,
                        VisionLL.INSTANCE,
                        Kicker.INSTANCE,
                        Indicators.INSTANCE,
                        Gate.INSTANCE),
                new PedroComponent(Constants::createFollower)
        );
    }
    @Override
    public void onInit() {
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        aimGoalPID = new AimGoalPID();
        PedroComponent.follower().update();
        Indicators.INSTANCE.setIndicators(Indicators.indicatorStates.preMatch);

        pathChain = () -> PedroComponent.follower().pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(PedroComponent.follower()::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(PedroComponent.follower()::getHeading, Math.toRadians(45), 0.8))
                .build();

        configureBindings();
    }

    private void configureBindings() {
        // Follow sample automated path on A button
//        gamepad1a.whenBecomesTrue(() -> {
//            new FollowPath(pathChain.get());
//            automatedDrive = true;
//        });

        // Toggle slow mode on right bumper
        gamepad1rightBumper.whenBecomesTrue(Shoot.shooterReverse());

        // Toggle aim at goal when left bumper is pressed (this eventually will happen w/o driver input)
        gamepad1leftBumper.whenBecomesTrue(() -> aimAtGoal = !aimAtGoal);

//        // Cancel auto path when B is pressed
//        gamepad1B
//                .and(() -> !follower.isBusy())
//                .whenBecomesTrue(() -> {
//                    follower.startTeleopDrive();
//                    automatedDrive = false;
//                });
        gamepad1X.whenBecomesTrue(new InstantCommand(Intake.INSTANCE::toggleIntake));
        gamepad1Tri.whenBecomesTrue(Shooter.INSTANCE::toggleShooter);
        gamepad1Dpad.whenBecomesTrue(Shooter.INSTANCE.shooterOnFar);
        gamepad1Square.whenBecomesTrue(Shooter.INSTANCE.shooterOnMedium);
        gamepad1RightTrigger.whenBecomesTrue(Shoot.shoot1());
        gamepad1Circle.whenBecomesTrue(new InstantCommand(()->aimAtGoal = !aimAtGoal));
//        gamepad1Circle.whenBecomesTrue(new InstantCommand(() -> PedroComponent.follower().setPose(new Pose(0, 0, 0, PedroCoordinates.INSTANCE))));
        gamepad1LeftTrigger.whenBecomesTrue(Shoot.shoot3());
    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setStartingPose(currentPose == null ? new Pose() : currentPose); // Take leftover pose from auto
        PedroComponent.follower().startTeleopDrive(true);
        Kicker.INSTANCE.onStart();
        Gate.INSTANCE.onStart();
        Shooter.INSTANCE.onStart();
        Indicators.INSTANCE.setIndicators(Indicators.indicatorStates.driving);
    }

    @Override
    public void onUpdate() {
        //Call this once per loop
        telemetryM.update();
        PedroComponent.follower().update();
        BindingManager.update();

        if (!automatedDrive) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors

            //This is the normal version to use in the TeleOp
//            if (!slowMode && !aimAtGoal) {follower.setTeleOpDrive(
//                    -gamepad1.left_stick_y,
//                    -gamepad1.left_stick_x,
//                    -gamepad1.right_stick_x,
//                    false // Robot Centric
//            );} else if (aimAtGoal) {
//                follower.setTeleOpDrive(
//                        -gamepad1.left_stick_y,
//                        -gamepad1.left_stick_x,
//                        aimGoalPID.update(),
//                        false); // Disable robot centric coordinates for aiming at the goal
//
//            }
//            //This is how it looks with slowMode on
//            else follower.setTeleOpDrive(
//                    -gamepad1.left_stick_y * slowModeMultiplier,
//                    -gamepad1.left_stick_x * slowModeMultiplier,
//                    -gamepad1.right_stick_x * slowModeMultiplier,
//                        false // Robot Centric
//            );
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
        Shooter.INSTANCE.shooterOff.schedule();
    }

}
//Path Diagram for each of the classes and explain the sensor associated with the class,
// the library's needed, the variables in the class, the overall function of the class to just make the robot drive.


