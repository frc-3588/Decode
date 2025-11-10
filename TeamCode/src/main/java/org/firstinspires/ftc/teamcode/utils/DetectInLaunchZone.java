package org.firstinspires.ftc.teamcode.utils;

public class DetectInLaunchZone {
    // Define the line of launch zone that originates at blue as y+x=144
    public static final double LAUNCH_ZONE_LINE1_X_COEFF = 1.0;
    public static final double LAUNCH_ZONE_LINE1_Y_COEFF = 1.0;
    public static final double LAUNCH_ZONE_LINE1_CONSTANT = 144.0;

    // Define the line of launch zone that originates at red as y=x
    public static final double LAUNCH_ZONE_LINE2_X_COEFF = -1.0;
    public static final double LAUNCH_ZONE_LINE2_Y_COEFF = 1.0;
    public static final double LAUNCH_ZONE_LINE2_CONSTANT = 0.0;

    /**
     * Checks if a position is in the launch zone
     * @param x X position of global pose to check (IN PEDRO COORDS)
     * @param y Y position of global pose to check (IN PEDRO COORDS)
     */
    public static boolean isInLaunchZone (double x, double y) {
        // Plug the x and y coordinate of the position into the two equations defined above
        boolean aboveLeftLine = (LAUNCH_ZONE_LINE1_X_COEFF * x) + (LAUNCH_ZONE_LINE1_Y_COEFF * y)
                >= LAUNCH_ZONE_LINE1_CONSTANT;

        boolean aboveRightLine = (LAUNCH_ZONE_LINE2_X_COEFF * x) + (LAUNCH_ZONE_LINE2_Y_COEFF * y)
                >= LAUNCH_ZONE_LINE2_CONSTANT;

        return aboveLeftLine && aboveRightLine;
    }
}
