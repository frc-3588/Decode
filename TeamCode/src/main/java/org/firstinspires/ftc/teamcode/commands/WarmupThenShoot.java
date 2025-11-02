package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

public class WarmupThenShoot {
    public static SequentialGroup get() {
        return new SequentialGroup(new InstantCommand(Shooter.INSTANCE::enableShooter),
                new Delay(Constants.ShooterConstants.warmUpTime),
                new InstantCommand(Shooter.INSTANCE::toggleGate),
                new Delay(Constants.ShooterConstants.gateOpenTime),
                new InstantCommand(Shooter.INSTANCE::toggleGate),
                new InstantCommand(Shooter.INSTANCE::disableShooter)
                );
    }
}
