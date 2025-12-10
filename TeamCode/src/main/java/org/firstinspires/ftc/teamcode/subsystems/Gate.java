package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Gate implements Subsystem {
    ServoEx gate = new ServoEx("gate");

    public static Gate INSTANCE = new Gate() {};

    @Override
    public void initialize() {
        gate.getServo().setDirection(Constants.IndexingConstants.gateDir);

    }

    @Override
    public void periodic() {
    }

    public void toggleGate(){
        if (gate.getPosition() == Constants.IndexingConstants.gateInitPosition){
            if (Shooter.INSTANCE.getShooterVelocity() < 100){
                new SequentialGroup(
                        Shooter.INSTANCE.shooterOnMedium,
                        new Delay(1.5),
                        gateToFirePos
                );
            } else {
                gateToFirePos.schedule();
            }
        } else {
            gateToOpenPos.schedule();
        }
    }
    public void onStart() {
    }

    public Command gateToFirePos =
            new InstantCommand(
                    ()-> gate.setPosition(Constants.IndexingConstants.gateShootPosition)).requires(this);
    public Command gateToOpenPos =
            new InstantCommand(
                    ()-> gate.setPosition(Constants.IndexingConstants.gateInitPosition)).requires(this);
}
