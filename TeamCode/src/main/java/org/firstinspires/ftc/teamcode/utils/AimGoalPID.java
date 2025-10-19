package org.firstinspires.ftc.teamcode.utils;

import static org.firstinspires.ftc.teamcode.Constants.aimGoalCoefficients;
import static org.firstinspires.ftc.teamcode.Constants.aimGoalThresholdDegrees;
import static org.firstinspires.ftc.teamcode.FieldConstants.blueGoalX;
import static org.firstinspires.ftc.teamcode.FieldConstants.blueGoalY;
import static org.firstinspires.ftc.teamcode.FieldConstants.redGoalX;
import static org.firstinspires.ftc.teamcode.FieldConstants.redGoalY;
import static org.firstinspires.ftc.teamcode.RobotState.isRed;

import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.AngleController;
import com.ThermalEquilibrium.homeostasis.Controllers.Feedback.BasicPID;
import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.subsystems.Vision;

import java.util.List;



public class AimGoalPID {
    private final Vision vision;
    private AngleController pid;
    private Follower follower;

    /**
     * This class calculates the power to set the drive rotation to in order to aim at the goal
     * Its primary system of doing this is attempting to achieve an angle of 0 with the April Tag on the goal
     * If it can not see the April Tag it uses odo data to calculate the angle to face the goal
     *
     * @param vision The limelight class
     * @param follower The drive train
     */
    public AimGoalPID(Vision vision, Follower follower) {
        this.vision = vision;
        this.follower = follower;
        pid = new AngleController(new BasicPID(aimGoalCoefficients));
    }

    public double update() {
        LLResultTypes.FiducialResult fiducial = getVisionTarget();
        if (fiducial != null && Math.abs(fiducial.getTargetXDegrees()) < aimGoalThresholdDegrees) {
            // If there is a target, use a PID loop to get the angle from the target to 0 degrees (straight on)
            // This is thresholded to allow for some error to prevent heavy oscillation
            return pid.calculate(0,fiducial.getTargetXDegrees());
        } else {
            // If there is not a target in sight, calculate angle to face the goal

            double tagX = isRed ? redGoalX : blueGoalX;
            double tagY = isRed ? redGoalY : blueGoalY;

            // In this case odometry robot pose data is used rather than vision
            // (If there was vision data to use it would be used instead in the first if statement)
            double dX = tagX - follower.getPose().getX();
            double dY = tagY - follower.getPose().getY();

            double targetHeading = Math.toDegrees(Math.atan2(dY, dX));

            // Calculate PID value to face this angle
            return pid.calculate(0, targetHeading);
        }
    }

    /**
     * This method checks first if the limelight can view any april tags
     * Then it checks if it can see the april tag on the goal for its alliance
     * If it can then the data for this april tag is returned
     * @return The april tag mounted on the goal if it can be seen, null otherwise
     */
    private LLResultTypes.FiducialResult getVisionTarget() {
        // If there is no valid data don't call a method on it and crash
        if (vision.getLLResult() == null){
            return null;
        }

        List<LLResultTypes.FiducialResult> fiducials = vision.getLLResult().getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            if ((isRed && fiducial.getFiducialId() == 24) || (!isRed && fiducial.getFiducialId() == 20)) {
                return fiducial;
            }
        }
        return null;

    }
}
