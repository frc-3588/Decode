//package org.firstinspires.ftc.teamcode.tests;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.PwmControl;
//import com.qualcomm.robotcore.hardware.ServoImplEx;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//@TeleOp(name = "Servo Test V2")
//public class ServoTest2 extends LinearOpMode {
//    ServoImplEx servo;
//    Timer timer = new Timer();
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        servo = (ServoImplEx) hardwareMap.servo.get("TestServo");
//        servo.setPwmRange(new PwmControl.PwmRange(500,2500));
//        waitForStart();
//        servo.setPosition(0);
//        timer.schedule(pos1, 5000);
//        timer.schedule(pos2, 10000);
//        timer.schedule(pos3, 15000);
//        timer.schedule(pos4, 20000);
//        timer.schedule(pos5, 25000);
//
//        while (opModeIsActive()) {
//            telemetry.addData("Servo Pos: ", servo.getPosition());
//            telemetry.update();
//        }
//
//    }
//
//    private TimerTask pos1 = new TimerTask() {
//        @Override
//        public void run() {
//            servo.setPosition(0.2);
//        }
//    };
//    private TimerTask pos2 = new TimerTask() {
//        @Override
//        public void run() {
//            servo.setPosition(0.4);
//        }
//    };
//    private TimerTask pos3 = new TimerTask() {
//        @Override
//        public void run() {
//            servo.setPosition(0.6);
//        }
//    };
//    private TimerTask pos4 = new TimerTask() {
//        @Override
//        public void run() {
//            servo.setPosition(0.8);
//        }
//    };
//    private TimerTask pos5 = new TimerTask() {
//        @Override
//        public void run() {
//            servo.setPosition(1);
//        }
//    };
//}