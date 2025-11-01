package org.firstinspires.ftc.teamcode.subsystems;


import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakeInverted;
import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakePower;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Intake implements Subsystem {
    MotorEx flywheelMotor = new MotorEx("intake").brakeMode();
    boolean power = false;
    public static final Intake INSTANCE = new Intake() {};

    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {

    }
    public void toggleIntakePower(){
        if (power){
            flywheelMotor.setPower(0);
            power = false;
        } else {
            flywheelMotor.setPower(intakeInverted ? -intakePower : intakePower);
            power = true;
        }
    }

}
