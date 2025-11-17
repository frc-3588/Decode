package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Kicker implements Subsystem {
    ServoEx kicker = new ServoEx("kicker");
    public static Kicker INSTANCE = new Kicker() {};
    public void toggleKicker() {
        if (kicker.getPosition() == Constants.IndexingConstants.kickerInitPosition){
            kickerToKickPos.schedule();
        } else {
            kickerToOpenPos.schedule();
        }
    }
    public Command kickerToKickPos =
            new InstantCommand(
                    ()-> kicker.setPosition(Constants.IndexingConstants.kickerKickPosition));
    public Command kickerToOpenPos =
            new InstantCommand(
                    ()-> kicker.setPosition(Constants.IndexingConstants.kickerInitPosition));
    public void onStart(){
        kicker.setPosition(Constants.IndexingConstants.kickerInitPosition);
    }
}
