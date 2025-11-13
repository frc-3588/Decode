package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.teamcode.subsystems.Indicators;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;

public class Shoot {
    public static SequentialGroup get() {
        return new SequentialGroup(
                new InstantCommand(() -> {
                    Indicators.INSTANCE.setIndicators(Indicators.indicatorStates.shooting);
                }),
                new InstantCommand(Shooter.INSTANCE.gateToFirePos),
                new Delay(0.5),
                new InstantCommand(Shooter.INSTANCE.gateToOpenPos)
        );

    }

}
