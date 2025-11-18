package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp
public class EncoderDirectionTest extends LinearOpMode {
    DcMotor fl, fr, bl, br;
    Timer timer = new Timer();
    @Override
    public void runOpMode() {
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        waitForStart();
        fl.setPower(-.2);
        timer.schedule(cycle1, 10000);
        timer.schedule(cycle2, 20000);
        timer.schedule(cycle3, 30000);
        while (opModeIsActive()) {
            telemetry.addData("FL", fl.getCurrentPosition());
            telemetry.addData("FR", fr.getCurrentPosition());
            telemetry.addData("BL", bl.getCurrentPosition());
            telemetry.addData("BR", br.getCurrentPosition());
            telemetry.update();
        }
    }

    private TimerTask cycle1 = new TimerTask() {
        @Override
        public void run() {
            fl.setPower(0);
            fr.setPower(.2);
        }
    };

    private TimerTask cycle2 = new TimerTask() {
        @Override
        public void run() {
            fr.setPower(0);
            bl.setPower(-.2);
        }
    };
    private TimerTask cycle3 = new TimerTask() {
        @Override
        public void run() {
            bl.setPower(0);
            br.setPower(.2);
        }
    };
}
