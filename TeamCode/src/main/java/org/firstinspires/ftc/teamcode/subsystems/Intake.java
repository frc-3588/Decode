package org.firstinspires.ftc.teamcode.subsystems;


import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakeInverted;
import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakePower;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Intake implements Subsystem {
    MotorEx flywheelMotor = new MotorEx("intake").brakeMode();
    ServoEx gate = new ServoEx("gate");
    boolean power = false;
    boolean gateOpen = false;
    public static final Intake INSTANCE = new Intake() {};

    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {

    }
    public void toggleShooterPower(){
        if (power){
            flywheelMotor.setPower(0);
            power = false;
        } else {
            flywheelMotor.setPower(intakeInverted ? -intakePower : intakePower);
            power = true;
        }
    }
    public void toggleGate(){
        if (gateOpen){
            gate.setPosition(0);
        } else{
            gate.setPosition(1);
        }

    }

}
