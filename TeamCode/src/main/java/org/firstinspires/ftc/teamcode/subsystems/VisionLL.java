package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Constants;

import java.util.List;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.ActiveOpMode;

public class VisionLL implements Subsystem {
    private Limelight3A limelight;
    private TelemetryManager telemetryManager;
    private LLResult currPose;

    public static VisionLL INSTANCE = new VisionLL() {};
    public VisionLL() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

    }

    @Override
    public void initialize() {
        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void periodic() {
        if (limelight == null){
            return;
        }
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                Pose3D botpose = result.getBotpose();
                Position poseInches = botpose.getPosition().toUnit(DistanceUnit.INCH);
                currPose = result;
                Pose posePedro = new Pose(poseInches.x + 72,
                        poseInches.y + 72,
                        (botpose.getOrientation().getYaw(AngleUnit.RADIANS) + 2 * Math.PI) % (2 * Math.PI));

                double x = posePedro.getX();
                double y = posePedro.getY();
                double theta = posePedro.getHeading();
                telemetryManager.addData("MT1 Location Pedro:", "(" + x + ", " + y + ") theta: " + Math.toDegrees(theta));
            }
        }
    }

    /**
     * @return Current Pose in Pedro Coordinates
     */
    public Pose getCurrPose() {
        // This needs to get improved, but it works for now
        if (currPose != null && currPose.getStaleness() < Constants.VisionConstants.VisionStalenessTimeout) {
            telemetryManager.debug("SETTING POSE VIA LIMELIGHT!");
            Pose3D llPosition = currPose.getBotpose();
            Position llPoseInches = llPosition.getPosition().toUnit(DistanceUnit.INCH);
            return new Pose(llPoseInches.x + 72,
                    llPoseInches.y + 72,
                    (llPosition.getOrientation().getYaw(AngleUnit.RADIANS) + 2 * Math.PI) % (2 * Math.PI));
        } else {
            return null;
        }
    }

    public LLResult getLLResult() {
        if (currPose.getStaleness() < Constants.VisionConstants.VisionStalenessTimeout) {
            return currPose;
        } else {
            return null;
        }
    }

    public LLResultTypes.FiducialResult getTag(int id){

        if (limelight == null){
            telemetryManager.addData("LIMELIGHT FOUND", false);
            return null;
        }
        List<LLResultTypes.FiducialResult> tags = limelight.getLatestResult().getFiducialResults();
        for (LLResultTypes.FiducialResult tag : tags){
            if (tag.getFiducialId() == id){
                telemetryManager.addData("Tag Identified", tag.getFiducialId());
                telemetryManager.addData("TX", tag.getTargetXDegrees());
                telemetryManager.addData("TY", tag.getTargetYDegrees());
                return tag;
            }
        }

        return null;
    }

}
