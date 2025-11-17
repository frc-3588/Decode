package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Two Motor Shooter Test")
public class TwoMotorShooterTest extends LinearOpMode {
    MotorEx shooter1;
//    MotorEx intake;
//    MotorGroup motorGroup;

    @Override
    public void runOpMode() throws InterruptedException {
        shooter1 = new MotorEx(hardwareMap.get(DcMotorEx.class, "shooter1"));
//        motor2 = new MotorEx(hardwareMap.get(DcMotorEx.class, "shooter2"));
//        motorGroup = new MotorGroup(motor1, motor2);
        waitForStart();
        while (opModeIsActive()) {
            shooter1.setPower(gamepad1.left_stick_x);
        }
    }
}
