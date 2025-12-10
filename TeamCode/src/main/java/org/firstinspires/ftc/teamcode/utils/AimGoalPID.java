package org.firstinspires.ftc.teamcode.utils;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.AngleController;
import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;

import dev.nextftc.core.commands.Command;
import dev.nextftc.extensions.pedro.PedroComponent;

public class AimGoalPID {
    static BasicPID pid = new BasicPID(Constants.AutoAimConstants.aimGoalCoefficients);

    public static double calculate(){
        LLResultTypes.FiducialResult redTag =  VisionLL.INSTANCE.getTag(24);
        LLResultTypes.FiducialResult blueTag =  VisionLL.INSTANCE.getTag(20);

        if (redTag != null){
            return pid.calculate(Constants.AutoAimConstants.redOffset,redTag.getTargetXDegrees());
        } else if (blueTag != null){
            return pid.calculate(Constants.AutoAimConstants.blueOffset,blueTag.getTargetXDegrees());
        }
        return 0;


    }

    private static class aimWithPID extends Command {
        @Override
        public void update() {
            PedroComponent.follower().setTeleOpDrive(0,0,calculate(),true);
        }

        @Override
        public void run() {
            PedroComponent.follower().startTeleopDrive();
        }

        @Override
        public boolean isDone() {
            LLResultTypes.FiducialResult redTag =  VisionLL.INSTANCE.getTag(24);
            LLResultTypes.FiducialResult blueTag =  VisionLL.INSTANCE.getTag(20);

            if (redTag != null){
                return Math.abs(redTag.getTargetXDegrees() - Constants.AutoAimConstants.redOffset) < 5;
            } else if (blueTag != null){
                return Math.abs(blueTag.getTargetXDegrees() - Constants.AutoAimConstants.blueOffset) < 5;
            }
            return true;
        }
    }

    public static Command aimWithPID = new aimWithPID();
}
