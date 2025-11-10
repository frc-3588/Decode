package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


@TeleOp(name = "Indicator Light Test")
public class IndicatorTest extends LinearOpMode {
    Servo indicator;
    double lastPos = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        indicator = hardwareMap.servo.get("indicator");
        indicator.setPosition(0.388);
        waitForStart();
        while (opModeIsActive()){
            if (lastPos < .95){
                indicator.setPosition(lastPos+ .01);
            } else {
                lastPos = 0;
            }
        }
    }
}
