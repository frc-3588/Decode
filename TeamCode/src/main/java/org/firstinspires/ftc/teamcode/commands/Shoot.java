package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

public class Shoot {
    public static Command shoot3Auto(){
        return  new SequentialGroup(
                Kicker.INSTANCE.kickerToOpenPos,
                Gate.INSTANCE.gateToFirePos,
                Intake.INSTANCE.intakeOff,
                new Delay(.4),
                new ParallelGroup(
                        Intake.INSTANCE.intakeOn,
                        Gate.INSTANCE.gateToOpenPos,
                        Kicker.INSTANCE.kickerToKickPos
                ),
                new Delay(1.2),
                Kicker.INSTANCE.kickerToOpenPos,
                Gate.INSTANCE.gateToFirePos,
                new Delay(0.4),
                new ParallelGroup(
                        Gate.INSTANCE.gateToOpenPos,
                        Kicker.INSTANCE.kickerToKickPos
                ),
                new Delay(1.7),
                Gate.INSTANCE.gateToFirePos,
                new Delay(0.9),
                Gate.INSTANCE.gateToOpenPos,
                Kicker.INSTANCE.kickerToOpenPos,
                new Delay(0.8)
        ).requires(Gate.INSTANCE, Kicker.INSTANCE);
    }
    public static Command shoot1() {
        return new SequentialGroup(
                new InstantCommand(Gate.INSTANCE.gateToFirePos),
                new Delay(0.5),
                new InstantCommand(Gate.INSTANCE.gateToOpenPos)
        ).requires(Gate.INSTANCE);

    }
    public static Command shoot3(){
        return  new SequentialGroup(
                Kicker.INSTANCE.kickerToOpenPos,
                Gate.INSTANCE.gateToFirePos,
                Intake.INSTANCE.intakeOff,
                new Delay(.4),
                new ParallelGroup(
                        Intake.INSTANCE.intakeOn,
                        Gate.INSTANCE.gateToOpenPos,
                        Kicker.INSTANCE.kickerToKickPos
                ),
                new Delay(1.2),
                Kicker.INSTANCE.kickerToOpenPos,
                Gate.INSTANCE.gateToFirePos,
                new Delay(0.4),
                new ParallelGroup(
                        Gate.INSTANCE.gateToOpenPos,
                        Kicker.INSTANCE.kickerToKickPos
                ),
                new Delay(1.7),
                Gate.INSTANCE.gateToFirePos,
                new Delay(0.9),
                Gate.INSTANCE.gateToOpenPos,
                Kicker.INSTANCE.kickerToOpenPos,
                new Delay(0.8)).requires(Gate.INSTANCE, Kicker.INSTANCE);
    }


    public static Command shooterReverse(){
        return new SequentialGroup(Shooter.INSTANCE.reverseShooter, new Delay(3), Shooter.INSTANCE.shooterOff);
    }

}
