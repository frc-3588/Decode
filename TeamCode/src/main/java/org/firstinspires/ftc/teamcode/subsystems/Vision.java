package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.teamcode.Constants.VisionConstants.cameraOrientation;
import static org.firstinspires.ftc.teamcode.Constants.VisionConstants.cameraPosition;
import static org.firstinspires.ftc.teamcode.Constants.VisionConstants.localizationWebCamName;
import static org.firstinspires.ftc.teamcode.Constants.VisionConstants.minDecisionMargin;

import android.util.Size;

import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

import dev.nextftc.core.subsystems.Subsystem;

public class Vision implements Subsystem {
    public static Vision INSTANCE = new Vision();

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private List<AprilTagDetection> detectionList = new ArrayList<>();


    @Override
    public void initialize() {
        initAprilTag();

    }

    @Override
    public void periodic() {
        detectionList = aprilTag.getDetections();
    }

    public List<AprilTagDetection> getDetectionList() {
        return detectionList;
    }

    public AprilTagDetection getDetectionById(int id) {
        for (AprilTagDetection detection : detectionList) {
            if (detection.id == id) {
                return detection;
            }
        }

        return null;
    }

    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                .setDrawAxes(false)
                .setDrawCubeProjection(false)
                .setDrawTagOutline(true)
                .setTagLibrary(AprilTagGameDatabase.getDecodeTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(cameraPosition, cameraOrientation)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(hardwareMap.get(WebcamName.class, localizationWebCamName));

        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }

    /**
     * First finds "best" April Tag if there are multiple based off distance to tag and decision margin
     * Then converts this tag to pedro coordinates
     *
     * @return Global pose of the robot in the field in Pedro Coordinates.
     */
    public Pose getFieldPose() {

        double minRange = Double.MAX_VALUE;
        Pose3D bestPose = null;

        for (AprilTagDetection detection : detectionList) {

            if (detection.metadata != null && detection.decisionMargin > minDecisionMargin) {

                if (detection.ftcPose.range < minRange) {
                    minRange = detection.ftcPose.range;
                    bestPose = detection.robotPose;
                }
            }
        }
        if (bestPose != null) {
            return new Pose(bestPose.getPosition().x,
                    bestPose.getPosition().y,
                    bestPose.getOrientation().getYaw(),
                    FTCCoordinates.INSTANCE).getAsCoordinateSystem(PedroCoordinates.INSTANCE);

        }
        return null;
    }

}
