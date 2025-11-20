package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.Shoot;
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

@Autonomous
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
                .addPath(new BezierLine(new Pose(25, 130), new Pose(45, 107)))
                .setLinearHeadingInterpolation(Math.
                        toRadians(130), Math.toRadians(130))
                .build();
        two = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(45, 107), new Pose(52, 86)))
                .setLinearHeadingInterpolation(Math.toRadians(130), Math.toRadians(180))
                .build();
        three = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(52, 86), new Pose(20, 86)))
                .setConstantHeadingInterpolation(Math.toDegrees(180))
                .build();
        four = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(20, 86), new Pose(45, 107)))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(140))
                .build();
        five = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(45, 107), new Pose(52, 60)))
                .setLinearHeadingInterpolation(Math.toRadians(140), Math.toRadians(180))
                .build();
        six = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(52, 60), new Pose(20, 60)))
                .setConstantHeadingInterpolation(Math.toDegrees(180))
                .build();
        seven = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(20, 60), new Pose(45,107)))
                .setLinearHeadingInterpolation(Math.toDegrees(140), Math.toDegrees(180))
                .build();

    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(new Pose(25, 130, Math.toRadians(130)));
        Command auto =
                new SequentialGroup(
                        new ParallelRaceGroup(new ParallelGroup(
                                Shooter.INSTANCE.shooterOnClose,
                                new SequentialGroup(
                                        new FollowPath(one, true, 0.8),
                                        Intake.INSTANCE.intakeOn,
                                        new InstantCommand(this::visionUpdatePose),
                                        Shoot.shoot3Auto(),
                                        new FollowPath(two, true, 0.8),
                                        new FollowPath(three, true, 0.5),
                                        new FollowPath(four, true, 0.8),
                                        Shoot.shoot3Auto(),
                                        new FollowPath(four, true, 0.8),
                                        new FollowPath(five, true, 0.8)
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
            PedroComponent.follower().setPose(llPose);
        }
    }
}
