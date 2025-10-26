package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Intake Test")
public class IntakeTest extends LinearOpMode {
    DcMotorEx intake;
    DcMotorEx shooter;
    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");

        waitForStart();
        while (opModeIsActive()){
            intake.setPower(gamepad1.left_stick_x);
//            shooter.setPower(1.0);
        }

    }
}
