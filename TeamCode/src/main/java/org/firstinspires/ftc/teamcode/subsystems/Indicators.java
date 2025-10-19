package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.subsystems.Subsystem;

public class Indicators implements Subsystem {
    Servo indicator1;
    Servo indicator2;

    public Indicators(HardwareMap hardwareMap){
        indicator1 = hardwareMap.get(Servo.class, "led1");
        indicator2 = hardwareMap.get(Servo.class, "led2");
    }

    @Override
    public void initialize() {
        indicator1.setPosition(0.722);
        indicator2.setPosition(0.500);
    }

    public void setColorRed(){
        indicator1.setPosition(0.277);
        indicator2.setPosition(0.277);
    }
}
