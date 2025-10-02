package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotState.currentPose;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

import java.util.function.Supplier;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.bindings.Button;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;

import static dev.nextftc.bindings.Bindings.button;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

@Configurable
@TeleOp
public class FtcTeleOp extends NextFTCOpMode {
    private final Vision vision = new Vision(hardwareMap);
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();
    private final Turret turret = new Turret();

    {
        addComponents(
                new PedroComponent(hardwareMap1 -> Constants.createFollower(hardwareMap, vision::getCurrPose)),
                new SubsystemComponent(vision),
                new SubsystemComponent(intake),
                new SubsystemComponent(shooter),
                new SubsystemComponent(turret));
    }

    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;
    private final double slowModeMultiplier = 0.5;
    Button gamepad1a = button(() -> gamepad1.a);
    Button gamepad1rightBumper = button(() -> gamepad1.right_bumper);
    Button gamepad1B = button(()-> gamepad1.b);



    @Override
    public void onInit() {
        follower().setStartingPose(currentPose == null ? new Pose() : currentPose); // Take leftover pose from auto
        follower().update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathChain = () -> follower().pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower()::getPose, new Pose(45, 98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower()::getHeading, Math.toRadians(45), 0.8))
                .build();
        configureBindings();
    }

    private void configureBindings(){
        // Follow sample automated path on a
        gamepad1a.whenBecomesTrue(() -> {
            new FollowPath(pathChain.get());
            automatedDrive = true;
        });

        // Toggle slow mode on right bumper
        gamepad1rightBumper.whenBecomesTrue(() ->{
            slowMode = !slowMode;
        });

        // Cancel auto path when B is pressed
        gamepad1B
                .and(()-> !follower().isBusy())
                .whenBecomesTrue(()->{
                    follower().startTeleopDrive();
                    automatedDrive = false;
                });
    }
    @Override
    public void onStartButtonPressed() {
        follower().startTeleopDrive(true);
    }

    @Override
    public void onUpdate() {
        //Call this once per loop
        telemetryM.update();
        BindingManager.update();

        if (!automatedDrive) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors

            //This is the normal version to use in the TeleOp
            if (!slowMode) follower().setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true // Robot Centric
            );

                //This is how it looks with slowMode on
            else follower().setTeleOpDrive(
                    -gamepad1.left_stick_y * slowModeMultiplier,
                    -gamepad1.left_stick_x * slowModeMultiplier,
                    -gamepad1.right_stick_x * slowModeMultiplier,
                    true // Robot Centric
            );
        }

        //Stop automated following if the follower is done
        if (automatedDrive && !follower().isBusy()) {
            follower().startTeleopDrive();
            automatedDrive = false;
        }


        telemetryM.debug("position", follower().getPose());
        telemetryM.debug("velocity", follower().getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);
    }

    @Override
    public void onStop() {
        BindingManager.reset();
    }

}