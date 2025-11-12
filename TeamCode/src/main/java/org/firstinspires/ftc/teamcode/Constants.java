package org.firstinspires.ftc.teamcode;

import com.ThermalEquilibrium.homeostasis.Parameters.FeedforwardCoefficients;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients;
import com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficientsEx;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Configurable
public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();
    public static final double controllerDeadband = 0.1;
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("fr")
            .rightRearMotorName("br")
            .leftRearMotorName("bl")
            .leftFrontMotorName("fl")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);


    public static Follower createFollower(HardwareMap hardwareMap, Supplier<Pose> visionPoseSupplier) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
//                .setLocalizer(new FusedAprilTagLocalizer(hardwareMap, localizerConstants, AutoConstants.startPose, visionPoseSupplier))
                .build();
    }

    public static Follower createFollower(HardwareMap hardwareMap) {
        return createFollower(hardwareMap, () -> null);
    }

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-1.812)
            .strafePodX(-0.604)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("odo")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    @Configurable
    public static class IntakeConstants {
        public final static double intakePower = 0.75;
        public static final boolean intakeInverted = true;
        public static final String intakeMotor = "intake";
    }

    @Configurable
    public static class VisionConstants {
        public static Position cameraPosition = new Position(DistanceUnit.INCH,
                0, 0, 0, 0);
        public static YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
                0, 0, 0, 0);
        public static String localizationWebCamName = "Webcam 1";

        public static double VisionStalenessTimeout = 100; //milliseconds
        public static double visionModelVariance = 0;
        public static double visionSensorVariance = 0;
        public static int visionPastSamples = 1;

        public static double minDecisionMargin = 10;
    }

    @Configurable
    public static class ShooterConstants {
        public static String shooterMotor = "shooter";

        public static final boolean shooterInverted = false;
        public static final double shooterPower = 1;
        public static double gateInitPosition = 0.3;
        public static double gateShootPosition = 0;
        public static Servo.Direction gateDir = Servo.Direction.FORWARD;
        public static double warmUpTime = 2; // Seconds
        public static double gateOpenTime = 1; // Seconds
        public static double maxRange = 60; // inches
        public static double maxGoalAngle = 10; // degrees
        public static PIDCoefficientsEx shooterPIDCoefficients = new PIDCoefficientsEx(0,0,0,0,0,0);
        public static FeedforwardCoefficients feedForwardConstants = new FeedforwardCoefficients(0,0,0);
    }

    @Configurable
    public static class AutoConstants {
        public static Pose startPose = new Pose(28.5, 131, Math.toRadians(135)); // Start Pose of our robot.
        public static Pose scorePose = new Pose(50, 96, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        public static Pose pickup1Pose = new Pose(41, 84, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
        public static Pose pickup2Pose = new Pose(41, 61, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
        public static Pose pickup3Pose = new Pose(41, 36, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    }

    public static PIDCoefficients aimGoalCoefficients = new PIDCoefficients(0, 0, 0);
    public static double aimGoalThresholdDegrees = 3.0;
}
