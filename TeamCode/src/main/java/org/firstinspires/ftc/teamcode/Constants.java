package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.control.PIDFCoefficients;
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

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;

@Configurable
public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(25)
            .forwardZeroPowerAcceleration(-41.130076526156046)
            .lateralZeroPowerAcceleration(-69.85431572316342)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.13,0,0.01,0.035))
            .headingPIDFCoefficients(new PIDFCoefficients(1,0,0,0.02));
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
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(61.54)
            .yVelocity(48)
            ;
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);


    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }


    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(0)
            .strafePodX(0)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("odo")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            ;

    @Configurable
    public static class IntakeConstants {
        public final static double intakePower = 0.8;
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

        public static double VisionStalenessTimeout = 150; //milliseconds
        public static double visionModelVariance = 0;
        public static double visionSensorVariance = 0;
        public static int visionPastSamples = 1;

        public static double minDecisionMargin = 10;
    }

    @Configurable
    public static class ShooterConstants {
        public static double closeShootVelocity = 1025;
        public static double mediumShootVelocity = 1100;
        public static double longShootVelocity = 1350;

        public static double shooterInterpolationYIntercept = 850.36;
        public static double shooterInterpolationSlope = 160.49;


        public static PIDCoefficients shooterPIDCoefficients = new PIDCoefficients(0.08,0,0);
        public static BasicFeedforwardParameters shooterFeedForward = new BasicFeedforwardParameters(0.0013, 0, 0.18);
    }

    @Configurable
    public static class AutoAimConstants {
        public static double blueOffset = -5;
        public static double redOffset = -5;
        public static com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients aimGoalCoefficients = new com.ThermalEquilibrium.homeostasis.Parameters.PIDCoefficients(0.013, 0, 0.005);

    }

    @Configurable
    public static class AutoConstants {
        public static Pose startPose = new Pose(28.5, 131, Math.toRadians(135)); // Start Pose of our robot.
        public static Pose scorePose = new Pose(50, 96, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.
        public static Pose pickup1Pose = new Pose(41, 84, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
        public static Pose pickup2Pose = new Pose(41, 61, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
        public static Pose pickup3Pose = new Pose(41, 36, Math.toRadians(180)); // Lowest (Third Set) of Artifacts from the Spike Mark.
    }


    @Configurable
    public static class IndexingConstants {
        public static double gateInitPosition = 0.3;
        public static double gateShootPosition = 0;
        public static double kickerSpeed = 1;
//        public static double kickerKickPosition = 0;
        public static Servo.Direction gateDir = Servo.Direction.FORWARD;
        public static Servo.Direction kickerDir = Servo.Direction.REVERSE;
    }
}
