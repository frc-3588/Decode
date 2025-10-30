package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;
import java.util.TimerTask;

import dev.nextftc.hardware.impl.ServoEx;

@TeleOp(name = "Gate Servo Test")
public class ServoTest extends LinearOpMode {
    Timer timer = new Timer();
    Servo servo;
    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("gate");

        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition(1);
            telemetry.addData("Servo Pos: ", servo.getPosition());
            telemetry.update();

        }
    }

    private TimerTask pos1 = new TimerTask() {
        @Override
        public void run() {
            servo.setPosition(100);
        }
    };
    private TimerTask pos2 = new TimerTask() {
        @Override
        public void run() {
            servo.setPosition(2);
        }
    };
    private TimerTask pos0 = new TimerTask() {
        @Override
        public void run() {
            servo.setPosition(0);
        }
    };

}
