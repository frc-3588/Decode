package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.FtcTeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Indicators;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.utils.AimGoalPID;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.ParallelRaceGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

public class Shoot {
    public static Command shoot3Auto(){
        return new SequentialGroup(
                new ParallelGroup(
                        Intake.INSTANCE.intakeOff,
                        Gate.INSTANCE.gateToFirePos
                ),
                new ParallelGroup(
                        new Delay(1),
                        new SequentialGroup(new Delay(0.5), Intake.INSTANCE.intakeOn)
                ),
                Gate.INSTANCE.gateToOpenPos,
                new Delay(0.4),
                Gate.INSTANCE.gateToFirePos,
                new Delay(1.7),
                Gate.INSTANCE.gateToOpenPos,
                new Delay(0.9),
                Gate.INSTANCE.gateToFirePos,
                new Delay(0.8),
                Gate.INSTANCE.gateToOpenPos
        );

    }
    public static Command shoot1() {
        return new SequentialGroup(
                new InstantCommand(Gate.INSTANCE.gateToOpenPos),
                new Delay(0.5),
                new InstantCommand(Gate.INSTANCE.gateToFirePos),
                new InstantCommand(()->FtcTeleOp.aimAtGoal = false)
        ).requires(Gate.INSTANCE);

    }
    public static Command shoot3(){
        return new SequentialGroup(
                Gate.INSTANCE.gateToOpenPos,
                Intake.INSTANCE.intakeOff,
                new Delay(.4),
                new ParallelGroup(
                        Intake.INSTANCE.intakeOn,
                        Gate.INSTANCE.gateToFirePos
                ),
                new Delay(1.2),
                Gate.INSTANCE.gateToOpenPos,
                new Delay(0.4),
                new ParallelGroup(
                        Gate.INSTANCE.gateToFirePos
                ),
                new Delay(1.7),
                Gate.INSTANCE.gateToOpenPos,
                new Delay(0.9),
                Gate.INSTANCE.gateToFirePos,
                new Delay(0.8),
                new InstantCommand(()->FtcTeleOp.aimAtGoal = false)
        ).requires(Gate.INSTANCE);
    }




}
