package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;

@TeleOp(name = "Shooter FF Character")
public class ShooterFeedForwardCharachterization extends LinearOpMode {
    double[] powers = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
    MotorEx motor1 = new MotorEx("shooter1");
    MotorEx motor2 = new MotorEx("shooter2");
    MotorGroup shooterMotors = new MotorGroup(motor1, motor2);

    @Override
    public void runOpMode() throws InterruptedException {

        for (double p : powers) {
            shooterMotors.setPower(p);
            Thread.sleep(1000); // wait to reach steady speed
            double v = shooterMotors.getVelocity();
            telemetry.addData("Power", p);
            telemetry.addData("Velocity", v);
            telemetry.update();
        }

    }
}
