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
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Simple Auto")
public class BlueBack extends NextFTCOpMode {
    PathChain one, two;
    TelemetryManager panelsTelemetry;
    public BlueBack() {
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

    @Override
    public void onInit (){
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        one = PedroComponent.follower().pathBuilder()
                .addPath(new BezierLine(new Pose(60,11, Math.toRadians(135)), new Pose(60, 16, Math.toRadians(135))))
                .setConstantHeadingInterpolation(Math.toRadians(135))
                .build();
    }

    @Override
    public void onStartButtonPressed(){
        PedroComponent.follower().setPose(new Pose(60,11, Math.toRadians(135)));
        Command auto = new SequentialGroup(
                new ParallelGroup(new InstantCommand(Shooter.INSTANCE.shooterOnFar),
                        new SequentialGroup(new Delay(2),
                                Shoot.shoot3(),
                                new FollowPath(one, true, 0.8)
                                ))


        );

        auto.schedule();
    }
    private void log(String caption, Object value) {
        telemetry.addData(caption, value);
        if (panelsTelemetry != null) panelsTelemetry.debug(caption + ": " + value);
    }
    @Override
    public void onUpdate(){
        PedroComponent.follower().update();
        if (panelsTelemetry != null) panelsTelemetry.update();

        Pose pose = PedroComponent.follower().getPose();
        double normH = Math.toDegrees((pose.getHeading() + 2 * Math.PI) % (2 * Math.PI));

        log("X", String.format("%.2f", pose.getX()));
        log("Y", String.format("%.2f", pose.getY()));
        log("Heading", String.format("%.2f°", normH));

        telemetry.update();
    }
}
