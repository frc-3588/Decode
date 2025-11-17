package org.firstinspires.ftc.teamcode.subsystems;


import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakeInverted;
import static org.firstinspires.ftc.teamcode.Constants.IntakeConstants.intakePower;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.IfElseCommand;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Intake implements Subsystem {
    MotorEx intakeMotor = new MotorEx("intake");

    boolean power = false;
    public static final Intake INSTANCE = new Intake() {};

    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {
    }
    public Command intakeOn = new InstantCommand(()->{
        intakeMotor.setPower(-intakePower);
        power = true;
    });
    public Command intakeOff = new InstantCommand(()->{
        intakeMotor.setPower(0);
        power = false;
    });

    public void toggleIntake(){
        if (power){
            INSTANCE.intakeOff.schedule();
        } else {
            INSTANCE.intakeOn.schedule();
        }
    }

}
