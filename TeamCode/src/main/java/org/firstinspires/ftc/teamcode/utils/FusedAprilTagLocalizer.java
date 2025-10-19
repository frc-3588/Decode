package org.firstinspires.ftc.teamcode.utils;

import static org.firstinspires.ftc.teamcode.Constants.visionModelVariance;
import static org.firstinspires.ftc.teamcode.Constants.visionPastSamples;
import static org.firstinspires.ftc.teamcode.Constants.visionSensorVariance;

import com.ThermalEquilibrium.homeostasis.Filters.FilterAlgorithms.KalmanFilter;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.geometry.Pose;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;


public class FusedAprilTagLocalizer extends PinpointLocalizer {
    Supplier<Pose> visionPoseSupplier;
    KalmanFilter kalmanFilter;
    public FusedAprilTagLocalizer(HardwareMap map, PinpointConstants constants, Pose setStartPose, Supplier<Pose> visionPoseSupplier) {
        super(map, constants, setStartPose);
        this.visionPoseSupplier = visionPoseSupplier;
        kalmanFilter = new KalmanFilter(visionModelVariance, visionSensorVariance, visionPastSamples);
    }

    @Override
    public void update(){
        super.update();

        if (visionPoseSupplier.get() != null){
            // Eventually add in the kalman filter here
            super.setPose(visionPoseSupplier.get());
        }
    }

}
