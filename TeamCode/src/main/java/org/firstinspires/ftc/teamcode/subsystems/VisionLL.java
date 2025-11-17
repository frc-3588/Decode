package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ftc.FTCCoordinates;
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
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Constants;

import java.util.List;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class VisionLL implements Subsystem {
    private Limelight3A limelight;
    private IMU imu;
    private TelemetryManager telemetryManager;
    private LLResult currPose;

    public static VisionLL INSTANCE = new VisionLL() {};
    public VisionLL() {
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

    }

    @Override
    public void initialize() {
        limelight = ActiveOpMode.hardwareMap().get(Limelight3A.class, "limelight");
        imu = ActiveOpMode.hardwareMap().get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.LEFT)));
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void periodic() {
        if (imu == null) {
            return;
        }
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
        if (limelight == null){
            return;
        }
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                Pose3D botpose = result.getBotpose_MT2();
                currPose = result;
                double x = botpose.getPosition().x;
                double y = botpose.getPosition().y;
                telemetryManager.addData("MT2 Location:", "(" + x + ", " + y + ")");
            }
        }
    }

    public Pose getCurrPose() {
        // This needs to get improved, but it works for now
        if (currPose.getStaleness() < Constants.VisionConstants.VisionStalenessTimeout) {
            Pose3D llPosition = currPose.getBotpose_MT2();
            return new Pose(llPosition.getPosition().x,
                    llPosition.getPosition().y,
                    llPosition.getOrientation().getYaw(),
                    FTCCoordinates.INSTANCE)
                    .getAsCoordinateSystem(PedroCoordinates.INSTANCE);
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
