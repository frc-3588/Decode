
package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.AutoPoses.Blue12.startPose;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.Shoot;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Autonomous(name = "AutoBlueBack12Linear", preselectTeleOp = "FtcTeleOp")
public class AutoBlueBack12 extends NextFTCOpMode {
    public AutoBlueBack12() {
        addComponents(
                new SubsystemComponent(Shooter.INSTANCE,
                        Intake.INSTANCE,
                        VisionLL.INSTANCE
                        ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    // === Timers ===
    private final Timer pathTimer = new Timer();
    private final Timer opmodeTimer = new Timer();

    // === Pathing ===

    private TelemetryManager panelsTelemetry;
    private int pathState = 0;
    private int foundID = 0;


    // === Path Chains ===
    private PathChain scoreInitial, scoreFar, scoreClose;
    private PathChain park;
    private PathChain alignPPG, scoopPPG;
    private PathChain alignPGP, scoopPGP;
    private PathChain alignGPP, scoopGPP;
    // === Shooting ===
    private boolean isShooting = false;
    private final ElapsedTime shootTimer = new ElapsedTime();

    // === Timer Class ===
    public static class Timer {
        private final ElapsedTime t = new ElapsedTime();

        public void resetTimer() {
            t.reset();
        }

        public double getElapsedTimeSeconds() {
            return t.seconds();
        }
    }

    // === Logging ===
    private void log(String caption, Object value) {
        telemetry.addData(caption, value);
        if (panelsTelemetry != null) panelsTelemetry.debug(caption + ": " + value);
    }

    private void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        log("Path State", pathState);
    }

    // === Path Building ===
    private void buildPaths() {
        scoreInitial = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, AutoPoses.Blue12.scoreFar))
                .setLinearHeadingInterpolation(startPose.getHeading(), AutoPoses.Blue12.scoreFar.getHeading())
                .build();
        scoreFar = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.scoreFar))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.scoreFar.getHeading())
                .build();
        scoreClose = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.scoringClose))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.scoringClose.getHeading())
                .build();
        alignPPG = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(startPose, AutoPoses.Blue12.alignPPG))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.alignPPG.getHeading())
                .build();
        scoopPPG = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.scoopPPG))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.scoopPPG.getHeading())
                .build();

        alignPGP = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.alignPGP))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.alignPGP.getHeading())
                .build();
        scoopPGP = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.scoopPGP))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.scoopPGP.getHeading())
                .build();
        alignGPP = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.alignGPP))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.alignGPP.getHeading())
                .build();
        scoopGPP = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(PedroComponent.follower().getPose(), AutoPoses.Blue12.scoopGPP))
                .setLinearHeadingInterpolation(PedroComponent.follower().getHeading(),  AutoPoses.Blue12.scoopGPP.getHeading())
                .build();
    }

    // ==============================================================
    //  OpMode Lifecycle – ONLY THESE 5 METHODS
    // ==============================================================

    @Override
    public void onInit() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        PedroComponent.follower().setMaxPower(0.75);
        PedroComponent.follower().setStartingPose(startPose);

        opmodeTimer.resetTimer();

        buildPaths();

        log("Status", "INIT: Ready");
    }

    @Override
    public void onWaitForStart() {
        log("Status", "INIT_LOOP: Press START");
        telemetry.update();
    }

    @Override
    public void onStartButtonPressed() {
        opmodeTimer.resetTimer();
        log("Status", "START: Running");

        // 1. Create a placeholder for the command we are about to build
        SequentialGroup autonomousCommand = new SequentialGroup(
//                new InstantCommand(Shooter.INSTANCE.shooterOnFar),
                // Initial Score
//                Shoot.shoot3(),
                // PPG
                new FollowPath(alignPPG, true, .8),
                Intake.INSTANCE.intakeOn,
                new FollowPath(scoopPPG, true, .8),
                Intake.INSTANCE.intakeOff,
                new FollowPath(scoreFar, true, .8),
//                Shoot.shoot3(),
                // GPP
                new FollowPath(alignGPP, true, .8),
                Intake.INSTANCE.intakeOn,
                new FollowPath(scoopGPP, true, .8),
                Intake.INSTANCE.intakeOff,
                new FollowPath(scoreClose, true, .8),
//                Shoot.shoot3(),
                // PGP
                new FollowPath(alignPGP, true, .8),
                Intake.INSTANCE.intakeOn,
                new FollowPath(scoopPGP, true, .8),
                Intake.INSTANCE.intakeOff,
                new FollowPath(scoreClose, true, .8)
//                Shoot.shoot3()
        );


        // 3. Schedule the one, big command to run. The scheduler handles the rest.
        autonomousCommand.schedule();
    }

    ;

    @Override
    public void onUpdate() {
        // onUpdate can be simplified. The command scheduler runs automatically.
        // You only need Pedro's update and your telemetry.
        PedroComponent.follower().update();
        if (panelsTelemetry != null) panelsTelemetry.update();

        Pose pose = PedroComponent.follower().getPose();
        double normH = Math.toDegrees((pose.getHeading() + 2 * Math.PI) % (2 * Math.PI));

        log("X", String.format("%.2f", pose.getX()));
        log("Y", String.format("%.2f", pose.getY()));
        log("Heading", String.format("%.2f°", normH));
        log("Time (s)", String.format("%.2f", opmodeTimer.getElapsedTimeSeconds()));

        telemetry.update();
    }

};