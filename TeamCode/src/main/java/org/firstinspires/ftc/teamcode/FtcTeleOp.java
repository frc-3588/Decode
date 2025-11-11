package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotState.currentPose;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.WarmupThenShoot;
import org.firstinspires.ftc.teamcode.subsystems.Indicators;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;
import org.firstinspires.ftc.teamcode.utils.AimGoalPID;

import java.util.function.Supplier;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.bindings.Range;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.ftc.NextFTCOpMode;

import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.bindings.Bindings.range;

@Configurable
@TeleOp
public class FtcTeleOp extends NextFTCOpMode {
    private VisionLL visionLL;

    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;
    private boolean aimAtGoal = false;
    private final double slowModeMultiplier = 0.5;
    Button gamepad1a = button(() -> gamepad1.cross);
    Button gamepad1rightBumper = button(() -> gamepad1.right_bumper);
    Button gamepad1leftBumper = button(() -> gamepad1.left_bumper);
    Button gamepad1RightTrigger = button(() -> gamepad1.right_trigger > 0.5);
    Button gamepad1LeftTrigger = button(() -> gamepad1.left_trigger > 0.5);
    Button gamepad1B = button(() -> gamepad1.circle);
    Button gamepad1X = button(() -> gamepad1.cross);
    Button gamepad1Tri = button(()->gamepad1.triangle);

    Range leftStickY = range(() -> -gamepad1.left_stick_y).deadZone(Constants.controllerDeadband);
    Range leftStickX = range(() -> -gamepad1.left_stick_x).deadZone(Constants.controllerDeadband);
    Range rightStickX = range(() -> -gamepad1.right_stick_x).deadZone(Constants.controllerDeadband);



    Follower follower;
    AimGoalPID aimGoalPID;


    @Override
    public void onInit() {
        addComponents(
                new SubsystemComponent(Vision.INSTANCE),
                new SubsystemComponent(Intake.INSTANCE),
                new SubsystemComponent(Shooter.INSTANCE),
                new SubsystemComponent(Indicators.INSTANCE));
        follower = Constants.createFollower(hardwareMap);
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        follower.setStartingPose(currentPose == null ? new Pose() : currentPose); // Take leftover pose from auto
        follower.update();

        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(45), 0.8))
                .build();

        aimGoalPID = new AimGoalPID(visionLL, follower);

        configureBindings();
    }

    private void configureBindings() {
        // Follow sample automated path on A button
        gamepad1a.whenBecomesTrue(() -> {
            new FollowPath(pathChain.get());
            automatedDrive = true;
        });

        // Toggle slow mode on right bumper
        gamepad1rightBumper.whenBecomesTrue(() -> {
            slowMode = !slowMode;
        });

        // Toggle aim at goal when left bumper is pressed (this eventually will happen w/o driver input)
        gamepad1leftBumper.whenBecomesTrue(() -> aimAtGoal = !aimAtGoal);

        // Cancel auto path when B is pressed
        gamepad1B
                .and(() -> !follower.isBusy())
                .whenBecomesTrue(() -> {
                    follower.startTeleopDrive();
                    automatedDrive = false;
                });
        gamepad1X.whenBecomesTrue(()->{
            Intake.INSTANCE.toggleIntakePower();
            telemetry.speak("INTAKE");
        });
        gamepad1Tri.whenBecomesTrue(Shooter.INSTANCE::toggleShooterPower);
        gamepad1RightTrigger.whenBecomesTrue(Shooter.INSTANCE::toggleGate);
        gamepad1LeftTrigger.whenBecomesTrue(WarmupThenShoot.get());
    }

    @Override
    public void onStartButtonPressed() {
        follower.startTeleopDrive(true);
        Shooter.INSTANCE.onStart();
    }

    @Override
    public void onUpdate() {
        //Call this once per loop
        telemetryM.update();
        follower.update();
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
            follower.setTeleOpDrive(
                    leftStickY.get(),
                    leftStickX.get(),
                    rightStickX.get(),
                    true);
        }

        //Stop automated following if the follower is done
        if (automatedDrive && !follower.isBusy()) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }


        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
    }

    @Override
    public void onStop() {
        BindingManager.reset();
    }

}
//Path Diagram for each of the classes and explain the sensor associated with the class,
// the library's needed, the variables in the class, the overall function of the class to just make the robot drive.


