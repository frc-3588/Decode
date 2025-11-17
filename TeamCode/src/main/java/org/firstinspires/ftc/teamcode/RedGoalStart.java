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
import org.firstinspires.ftc.teamcode.subsystems.Kicker;
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
    PathChain one, two, three, four, five, six;
    TelemetryManager panelsTelemetry;

    public RedGoalStart() {
        addComponents(new SubsystemComponent(Shooter.INSTANCE, Intake.INSTANCE, VisionLL.INSTANCE, Gate.INSTANCE, Kicker.INSTANCE), BulkReadComponent.INSTANCE, BindingsComponent.INSTANCE, new PedroComponent(Constants::createFollower));
    }

    @Override
    public void onInit() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        one = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(116, 130), new Pose(88, 107))).setLinearHeadingInterpolation(Math.toRadians(36), Math.toRadians(36)).build();
        two = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 107), new Pose(92, 86))).setLinearHeadingInterpolation(Math.toRadians(40), Math.toRadians(0)).build();
        three = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 86), new Pose(120, 86))).setConstantHeadingInterpolation(Math.toDegrees(0)).build();
        four = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(123, 86), new Pose(88, 107))).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(40)).build();
        five = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(88, 107), new Pose(92, 60))).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(40)).build();
        six = PedroComponent.follower().pathBuilder().addPath(new BezierLine(new Pose(92, 60), new Pose(120, 60))).setConstantHeadingInterpolation(Math.toDegrees(0)).build();
    }

    @Override
    public void onStartButtonPressed() {
        Gate.INSTANCE.onStart();
        Kicker.INSTANCE.onStart();
        PedroComponent.follower().setPose(new Pose(116, 130, Math.toRadians(36)));
        Command auto =
                new SequentialGroup(
                        new ParallelGroup(
                                Shooter.INSTANCE.shooterOnClose,
                                new SequentialGroup(
                                        new FollowPath(one, true, 0.8),
                                        Intake.INSTANCE.intakeOn,
                                        Shoot.shoot3Auto(),
                                        Shoot.shoot1(),
                                        new FollowPath(two, true, 0.8),
                                        new FollowPath(three, true, 0.5),
                                        new FollowPath(four, true, 0.8),
                                        Shoot.shoot3Auto(),
                                        new FollowPath(four, true, 0.8),
                                        new FollowPath(five, true, 0.8)
                                )

                        ));

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

    @Override
    public void onStop() {
        BindingManager.reset();
        Shooter.INSTANCE.shooterOff.schedule();
    }
}
