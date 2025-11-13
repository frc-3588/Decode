package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class AutoPoses {

    public static class Red12 {
        public static final Pose startPose = new Pose(88, 7, Math.toRadians(90));
        public static final Pose scoring1 = new Pose(90, 90, Math.toRadians(45));
        public static final Pose scoring2 = new Pose(96, 49, Math.toRadians(45));
        public static final Pose pickup1PPG = new Pose(100, 83, Math.toRadians(0));
        public static final Pose pickup2PPG = new Pose(124, 83, Math.toRadians(0));
        public static final Pose pickup1PGP = new Pose(100, 59, Math.toRadians(0));
        public static final Pose pickup2PGP = new Pose(124, 59, Math.toRadians(0));
        public static final Pose pickup1GPP = new Pose(100, 39, Math.toRadians(0));
        public static final Pose pickup2GPP = new Pose(124, 39, Math.toRadians(0));
    }

    public static class Blue12 {
        public static final Pose startPose = new Pose(64, 8, Math.toRadians(90));
        public static final Pose scoringClose = new Pose(56, 7, Math.toRadians(135));
        public static final Pose scoreFar = new Pose(56, 7, Math.toRadians(110));
        public static final Pose alignPPG = new Pose(41, 38, Math.toRadians(180));
        public static final Pose scoopPPG = new Pose(14, 38, Math.toRadians(180));

        public static final Pose alignPGP = new Pose(41, 61, Math.toRadians(180));
        public static final Pose scoopPGP = new Pose(14, 61, Math.toRadians(180));

        public static final Pose alignGPP = new Pose(41, 87, Math.toRadians(180));
        public static final Pose scoopGPP = new Pose(14, 87, Math.toRadians(180));

    }
}
