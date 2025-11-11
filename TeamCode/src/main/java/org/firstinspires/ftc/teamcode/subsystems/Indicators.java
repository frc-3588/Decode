package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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

    @Override
    public void initialize() {
        servo.setPosition(0.3);
    }

    public void setColorRed(){
    }
}
