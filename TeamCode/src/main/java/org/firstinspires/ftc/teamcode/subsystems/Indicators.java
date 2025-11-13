package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Indicators implements Subsystem {
    ServoEx servo = new ServoEx("indicator");

    public static Indicators INSTANCE = new Indicators();

    public static enum indicatorStates {
        hasArtifact,
        canShoot,
        shooting,
        preMatch,
        postMatch
    }

    public void setIndicators(indicatorStates state) {
        switch (state) {
            case preMatch:
                servo.setPosition(0.3);
            case canShoot:
                servo.setPosition(0.5);
            case shooting:
                servo.setPosition(0.8);
        }
    }
}
