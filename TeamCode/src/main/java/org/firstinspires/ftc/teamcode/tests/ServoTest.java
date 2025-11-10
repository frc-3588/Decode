package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "Gate Servo Test")
public class ServoTest extends LinearOpMode {
    Timer timer = new Timer();
    Servo servo;
    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("gate");
    servo.setDirection(Servo.Direction.REVERSE);
        waitForStart();
        timer.schedule(pos1, 5000);
        timer.schedule(pos2, 10000);
        servo.setPosition(0.4);
        while (opModeIsActive()) {
            telemetry.addData("Servo Pos: ", servo.getPosition());
            telemetry.update();

        }
    }

    private TimerTask pos1 = new TimerTask() {
        @Override
        public void run() {
            servo.setPosition(0.7);
        }
    };
    private TimerTask pos2 = new TimerTask() {
        @Override
        public void run() {
            servo.setPosition(0.8);
        }
    };
//    private TimerTask pos0 = new TimerTask() {
//        @Override
//        public void run() {
//            servo.setPosition(0);
//        }
//    };

}
