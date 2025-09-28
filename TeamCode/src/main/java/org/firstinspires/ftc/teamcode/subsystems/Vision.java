package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.core.subsystems.Subsystem;

public class Vision implements Subsystem {
    private Limelight3A limelight;
    private IMU imu;
    private TelemetryManager telemetryManager;
    private LLResult currPose;
    private final HardwareMap hardwareMap;
    public Vision(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();

    }

    @Override
    public void initialize() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        imu = hardwareMap.get(IMU.class, "imu");

        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void periodic() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
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

    public Pose3D getCurrPose() {
        // This needs to get improved, but it works for now
        if (currPose.getStaleness() < Constants.VisionStalenessTimeout){
            return currPose.getBotpose_MT2();
        } else {
            return null;
        }
    }
}
