package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.subsystems.Subsystem;

public class Indicators implements Subsystem {

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
    }

    public void setColorRed(){
    }
}
