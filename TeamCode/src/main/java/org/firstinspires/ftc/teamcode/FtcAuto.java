package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.startPose;
import static org.firstinspires.ftc.teamcode.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.utils.Paths.grabPickup1;
import static org.firstinspires.ftc.teamcode.utils.Paths.grabPickup2;
import static org.firstinspires.ftc.teamcode.utils.Paths.grabPickup3;
import static org.firstinspires.ftc.teamcode.utils.Paths.scorePickup1;
import static org.firstinspires.ftc.teamcode.utils.Paths.scorePickup2;
import static org.firstinspires.ftc.teamcode.utils.Paths.scorePickup3;
import static org.firstinspires.ftc.teamcode.utils.Paths.scorePreload;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.Indicators;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.VisionLL;
import org.firstinspires.ftc.teamcode.utils.Paths;

import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;


@Autonomous(name = "Auto", group = "Primary")
public class FtcAuto extends NextFTCOpMode {
    private final VisionLL visionLL = new VisionLL(hardwareMap);
    private final Intake intake = new Intake();
    private final Shooter shooter = new Shooter();
    private final Turret turret = new Turret();
    private final Indicators indicators = new Indicators(hardwareMap);

    {
        addComponents(
                new PedroComponent(hardwareMap1 -> createFollower(hardwareMap, visionLL::getCurrPose)),
                new SubsystemComponent(visionLL),
                new SubsystemComponent(intake),
                new SubsystemComponent(shooter),
                new SubsystemComponent(indicators),
                new SubsystemComponent(turret));
    }

    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;


    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower().followPath(scorePreload);
                setPathState(1);
                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower().isBusy()) {
                    /* Score Preload */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower().followPath(grabPickup1, true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower().isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower().followPath(scorePickup1, true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower().isBusy()) {
                    /* Score Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower().followPath(grabPickup2, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if (!follower().isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower().followPath(scorePickup2, true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower().isBusy()) {
                    /* Score Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower().followPath(grabPickup3, true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if (!follower().isBusy()) {
                    /* Grab Sample */
                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    follower().followPath(scorePickup3, true);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower().isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
        }
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void onUpdate() {
        // These loop the movements of the robot, these must be called continuously in order to work
        autonomousPathUpdate();
        follower().update();

        // Keep pose updated on

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower().getPose().getX());
        telemetry.addData("y", follower().getPose().getY());
        telemetry.addData("heading", follower().getPose().getHeading());
        telemetry.update();
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void onInit() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        Paths.buildPaths(follower());
        follower().setStartingPose(startPose);
    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void onWaitForStart() {
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void onStartButtonPressed() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void onStop() {
        RobotState.currentPose = follower().getPose(); // Preserve end of auto pose
    }
}