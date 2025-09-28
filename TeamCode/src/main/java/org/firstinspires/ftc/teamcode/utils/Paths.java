package org.firstinspires.ftc.teamcode.utils;

import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.pickup1Pose;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.pickup2Pose;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.pickup3Pose;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.scorePose;
import static org.firstinspires.ftc.teamcode.Constants.AutoConstants.startPose;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

public class Paths {
    public static Path scorePreload;
    public static PathChain grabPickup1, scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3;
    public static void buildPaths(Follower follower){
    /* This is our scorePreload path. We are using a BezierLine, which is a straight line. */
    scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

    /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
    grabPickup1 = follower.pathBuilder()
            .addPath(new BezierLine(scorePose, pickup1Pose))
            .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
            .build();

    /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
    scorePickup1 = follower.pathBuilder()
            .addPath(new BezierLine(pickup1Pose, scorePose))
            .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
            .build();

    /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
    grabPickup2 = follower.pathBuilder()
            .addPath(new BezierLine(scorePose, pickup2Pose))
            .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
            .build();

    /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
    scorePickup2 = follower.pathBuilder()
            .addPath(new BezierLine(pickup2Pose, scorePose))
            .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
            .build();

    /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
    grabPickup3 = follower.pathBuilder()
            .addPath(new BezierLine(scorePose, pickup3Pose))
            .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
            .build();

    /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
    scorePickup3 = follower.pathBuilder()
            .addPath(new BezierLine(pickup3Pose, scorePose))
            .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
            .build();}
}
