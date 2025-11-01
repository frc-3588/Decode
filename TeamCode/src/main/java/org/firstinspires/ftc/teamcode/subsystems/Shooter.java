package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.shooterPower;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Shooter implements Subsystem {
    MotorEx motor1 = new MotorEx("shooter1");
    MotorEx motor2 = new MotorEx("shooter2");
    ServoEx gate = new ServoEx("gate");
    MotorGroup shooterMotors = new MotorGroup(motor1, motor2);
    boolean power = false;
    public static final Shooter INSTANCE = new Shooter() {};


    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {
    }

    public void toggleShooterPower(){

        if (power){
            shooterMotors.setPower(0);
            power = false;
        } else {
            shooterMotors.setPower(Constants.ShooterConstants.shooterInverted ? -shooterPower : shooterPower);
            power = true;;
        }
    }

    public void toggleGate(){
        if (gate.getPosition() == 0){
            gate.setPosition(0.5);
        } else {
            gate.setPosition(0);
        }
    }
}
