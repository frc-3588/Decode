package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.shooterMotor;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.shooterPower;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.subsystems.Subsystem;

public class Shooter implements Subsystem {
    DcMotor motor;
    boolean power = false;

    HardwareMap map;
    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {
//        motor = map.get(DcMotor.class, shooterMotor);
    }

    public Shooter(HardwareMap map){
        this.map = map;

    }

    public void togglePower(){

        if (power){
//            motor.setPower(0);
            power = false;
        } else {
//            motor.setPower(Constants.ShooterConstants.shooterInverted ? -shooterPower : shooterPower);
            power = true;;
        }
    }
}
