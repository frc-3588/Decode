//package org.firstinspires.ftc.teamcode.tests;
//
//import com.bylazar.telemetry.PanelsTelemetry;
//import com.bylazar.telemetry.TelemetryManager;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import dev.nextftc.hardware.impl.MotorEx;
//
//@TeleOp(name = "Encoder Compare ")
//public class EncoderCompare extends LinearOpMode {
//    MotorEx motor1 = new MotorEx("shooter1");
//    MotorEx motor2 = new MotorEx("shooter2");
//    TelemetryManager telemetry = PanelsTelemetry.INSTANCE.getTelemetry();
//    @Override
//    public void runOpMode() throws InterruptedException {
//        telemetry.addData("Motor 1 Velocity", motor1.getVelocity());
//        telemetry.addData("Motor 2 Velocity", motor2.getVelocity());
//        waitForStart();
//
//        motor1.setPower(0.5);
//
//        while (opModeIsActive()){
//            telemetry.addData("Motor 1 Velocity", motor1.getVelocity());
//            telemetry.addData("Motor 2 Velocity", motor2.getVelocity());
//            telemetry.update();
//        }
//    }
//}
