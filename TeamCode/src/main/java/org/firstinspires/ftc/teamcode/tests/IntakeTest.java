package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Intake Test")
public class IntakeTest extends LinearOpMode {
    DcMotorEx intake;
    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        waitForStart();
        while (opModeIsActive()){
            intake.setPower(gamepad1.left_stick_x);
        }

    }
}
