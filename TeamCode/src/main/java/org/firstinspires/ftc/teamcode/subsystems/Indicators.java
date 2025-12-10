package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;

public class Indicators implements Subsystem {
//    ServoEx indicator1 = new ServoEx("indicator");
//    ServoEx indicator2 = new ServoEx("indicator2");

    public static Indicators INSTANCE = new Indicators();

//    @Override
//    public void initialize() {
//        setIndicators(indicatorStates.preMatch);
//    }

//    public static enum indicatorStates {
//        driving,
//        hasArtifact,
//        canShoot,
//        shooting,
//        preMatch,
//        postMatch
//    }
//
//    public void setIndicators(indicatorStates state) {
//        switch (state) {
//            case preMatch:
//                indicator1.setPosition(0.3);
//                indicator2.setPosition(0.3);
//            case driving:
//                indicator1.setPosition(0.4);
//                indicator2.setPosition(0.4);
//            case canShoot:
//                indicator1.setPosition(0.5);
//                indicator2.setPosition(0.5);
//            case shooting:
//                indicator1.setPosition(0.8);
//                indicator2.setPosition(0.8);
//        }
//    }
}
