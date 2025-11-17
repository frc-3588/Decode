package org.firstinspires.ftc.teamcode.utils;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.AngleController;
import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;

public class AimGoalPID {
    BasicPID pid = new BasicPID(Constants.aimGoalCoefficients);
    AngleController controller = new AngleController(pid);

    public double calculate(){
        LLResultTypes.FiducialResult tag =  VisionLL.INSTANCE.getTag(24);
        if (tag!= null){
            return controller.calculate(0,tag.getTargetXDegrees());
        }
        return 0;
    }
}
