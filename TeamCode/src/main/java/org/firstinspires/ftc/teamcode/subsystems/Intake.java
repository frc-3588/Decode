package org.firstinspires.ftc.teamcode.subsystems;


import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakeInverted;
import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakePower;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Intake implements Subsystem {
    MotorEx motor = new MotorEx("intake").brakeMode();
    boolean power = false;

    public static final Intake INSTANCE = new Intake() {};

    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {

    }
    public void togglePower(){
        if (power){
            motor.setPower(0);
            power = false;
        } else {
            motor.setPower(intakeInverted ? -intakePower : intakePower);
            power = true;
        }
    }
}
