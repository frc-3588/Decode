package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.Shoot;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.ParallelRaceGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Blue Goal Start", preselectTeleOp = "FtcTeleOp")
public class BlueGoalStart extends NextFTCOpMode {
    PathChain one, two, three, four, five, six, seven, eight;
    TelemetryManager panelsTelemetry;

    public BlueGoalStart() {
        addComponents(
                new SubsystemComponent(
                        Shooter.INSTANCE,
                        Intake.INSTANCE,
                        VisionLL.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    @Override
    public void onInit() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        one = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(28, 130), new Pose(52, 99)))
                .setLinearHeadingInterpolation(Math.
                        toRadians(137), Math.toRadians(133))
                .build();
        two = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(52, 99), new Pose(52, 93)))
                .setLinearHeadingInterpolation(Math.toRadians(133), Math.PI)
                .build();
        three = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(52, 93), new Pose(20, 93)))
                .setConstantHeadingInterpolation(Math.PI)
                .build();
        four = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(20, 93), new Pose(52, 93)))
                .setLinearHeadingInterpolation(Math.PI, Math.toRadians(137))
                .build();
        five = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(52, 93), new Pose(52, 69)))
                .setLinearHeadingInterpolation(Math.toRadians(137), Math.PI)
                .build();
        six = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(52, 69), new Pose(18, 69)))
                .setConstantHeadingInterpolation(Math.PI)
                .build();
        seven = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(18, 69), new Pose(52,97)))
                .setLinearHeadingInterpolation(Math.PI, Math.toRadians(137))
                .build();
        eight = PedroComponent.follower().pathBuilder().addPath(
                new BezierLine(new Pose(52,97), new Pose(52,50))
        ).setLinearHeadingInterpolation(Math.toRadians(137), Math.PI).build();

    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(new Pose(28, 130, Math.toRadians(137)));
        Command auto =
                new SequentialGroup(
                        new ParallelRaceGroup(new ParallelGroup(
                                Shooter.INSTANCE.shooterOnClose,
                                new SequentialGroup(
                                        Gate.INSTANCE.gateToOpenPos,
                                        new FollowPath(one, true, 0.8),
                                        new Delay(2),
                                        Intake.INSTANCE.intakeOff,
                                        Shoot.shoot3Auto(),
                                        new FollowPath(two, true, 1.0),
                                        new FollowPath(three, true, .65),
                                        new Delay(0.5),
                                        new ParallelGroup(
                                                new FollowPath(four, true, 1.0),
                                                new SequentialGroup(
                                                        new Delay(0.2),
                                                        Intake.INSTANCE.intakeOff,
                                                        new Delay(0.2),
                                                        Intake.INSTANCE.intakeOn
                                                )
                                        ),
                                        Shoot.shoot3Auto(),
                                        new FollowPath(four, true, 1.0),
                                        new FollowPath(five, true, 1.0),
                                        new FollowPath(six, true, .6),
                                        new ParallelGroup(
                                                new FollowPath(seven, true, 1.0),
                                                new SequentialGroup(
                                                        new Delay(0.2),
                                                        Intake.INSTANCE.intakeOff,
                                                        new Delay(0.2),
                                                        Intake.INSTANCE.intakeOn
                                                )
                                        ),
                                        Shoot.shoot3Auto(),
                                        new FollowPath(eight, true, 1.0)
                                )

                        ), new Delay(28)),
                        Shooter.INSTANCE.shooterOff,
                        new InstantCommand(PedroComponent.follower()::breakFollowing)
                );
        auto.schedule();
    }

    private void log(String caption, Object value) {
        telemetry.addData(caption, value);
        if (panelsTelemetry != null) panelsTelemetry.debug(caption + ": " + value);
    }

    @Override
    public void onUpdate() {
        PedroComponent.follower().update();
        if (panelsTelemetry != null) panelsTelemetry.update();

        Pose pose = PedroComponent.follower().getPose();

        double normH = Math.toDegrees((pose.getHeading() + 2 * Math.PI) % (2 * Math.PI));

        log("X", String.format("%.2f", pose.getX()));
        log("Y", String.format("%.2f", pose.getY()));
        log("Heading", String.format("%.2f°", normH));

        telemetry.update();
    }

    public void visionUpdatePose(){
        Pose llPose = VisionLL.INSTANCE.getCurrPose();
        if (llPose != null){
            panelsTelemetry.addData("New Pose", llPose);
            PedroComponent.follower().setPose(llPose);
        }
    }
}
