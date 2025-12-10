package org.firstinspires.ftc.teamcode;

import com.bylazar.field.Style;
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

import dev.nextftc.bindings.BindingManager;
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

@Autonomous(name = "Red Goal Start", preselectTeleOp = "FtcTeleOp")
public class RedGoalStart extends NextFTCOpMode {
    PathChain one, two, three, four, five, six, seven, eight;
    TelemetryManager panelsTelemetry;

    public RedGoalStart() {
        addComponents(
                new SubsystemComponent(
                        Shooter.INSTANCE,
                        Intake.INSTANCE,
                        VisionLL.INSTANCE,
                        Gate.INSTANCE
                ),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }

    @Override
    public void onInit() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        one = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(116, 130), new Pose(92, 97))).setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(44)).build();
        two = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 97), new Pose(92, 86))).setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(0)).build();
        three = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 86), new Pose(120, 86))).setConstantHeadingInterpolation(Math.toDegrees(0)).build();
        four = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(120, 86), new Pose(88, 107))).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(44)).build();
        five = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(88, 107), new Pose(92, 65))).setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(0)).build();
        six = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 65), new Pose(120, 65))).setConstantHeadingInterpolation(Math.toRadians(0)).build();
        seven = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(120, 65), new Pose(92, 97))).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(44)).build();
        eight = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 97), new Pose(92, 75))).setLinearHeadingInterpolation(Math.toRadians(44), Math.toRadians(0)).build();

    }

    @Override
    public void onStartButtonPressed() {
        PedroComponent.follower().setPose(new Pose(116, 130, Math.toRadians(44)));
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
                                        new FollowPath(three, true, .90),
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
        Drawing.drawDebug(PedroComponent.follower());
        if (panelsTelemetry != null) panelsTelemetry.update();
        Pose llPose = VisionLL.INSTANCE.getCurrPose();
        if (llPose != null) {
//            PedroComponent.follower().setPose(llPose);
        }
        Pose pose = PedroComponent.follower().getPose();
        double normH = Math.toDegrees((pose.getHeading() + 2 * Math.PI) % (2 * Math.PI));

        log("X", String.format("%.2f", pose.getX()));
        log("Y", String.format("%.2f", pose.getY()));
        log("Heading", String.format("%.2f°", normH));

        telemetry.update();
    }

    @Override
    public void onStop() {
        BindingManager.reset();
        Shooter.INSTANCE.shooterOff.schedule();
    }
}
