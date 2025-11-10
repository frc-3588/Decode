package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class StupidDrive extends LinearOpMode {
    DcMotor fl, fr, bl, br;

    @Override
    public void runOpMode() {
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        waitForStart();

        while (opModeIsActive()) {
            fl.setPower(-1 * -gamepad1.left_stick_y);
            fr.setPower(1 * -gamepad1.right_stick_y);
            bl.setPower(-1 * -gamepad1.left_stick_y);
            br.setPower(1 * -gamepad1.right_stick_y
            );
            telemetry.addData("FL", fl.getCurrentPosition());
            telemetry.addData("FR", fr.getCurrentPosition());
            telemetry.addData("BL", bl.getCurrentPosition());
            telemetry.addData("BR", br.getCurrentPosition());
            telemetry.update();
        }
    }
}
