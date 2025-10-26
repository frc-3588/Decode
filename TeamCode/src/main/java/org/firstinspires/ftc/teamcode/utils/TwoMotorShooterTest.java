package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Two Motor Shooter Test")
public class TwoMotorShooterTest extends LinearOpMode {
    MotorEx motor1;
    MotorEx motor2;
    MotorGroup motorGroup;

    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = new MotorEx(hardwareMap.get(DcMotorEx.class, "shooter"));
        motor2 = new MotorEx(hardwareMap.get(DcMotorEx.class, "shooter2"));
        motorGroup = new MotorGroup(motor1, motor2);
        waitForStart();
        while (opModeIsActive()) {
            motorGroup.setPower(gamepad1.left_stick_x);
        }
    }
}
