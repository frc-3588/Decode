package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.maxGoalAngle;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.maxRange;
import static org.firstinspires.ftc.teamcode.Constants.ShooterConstants.shooterPower;

import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.teamcode.utils.DetectInLaunchZone;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;

public class Shooter implements Subsystem {
    MotorEx motor1 = new MotorEx("shooter1");
    MotorEx motor2 = new MotorEx("shooter2");
    ServoEx gate = new ServoEx("gate");
    MotorGroup shooterMotors = new MotorGroup(motor1, motor2);
    boolean power = false;
    public static final Shooter INSTANCE = new Shooter() {};

    @Override
    public void periodic() {
    }

    @Override
    public void initialize() {
        gate.getServo().setDirection(Constants.ShooterConstants.gateDir);
    }

    public void onStart(){
        gate.setPosition(Constants.ShooterConstants.gateInitPosition);
    }

    public void toggleShooterPower(){

        if (power){
            shooterMotors.setPower(0);
            power = false;
        } else {
            shooterMotors.setPower(Constants.ShooterConstants.shooterInverted ? -shooterPower : shooterPower);
            power = true;;
        }
    }

    public void toggleGate(){
        if (gate.getPosition() == Constants.ShooterConstants.gateInitPosition){
            gate.setPosition(Constants.ShooterConstants.gateShootPosition);
        } else {
            gate.setPosition(Constants.ShooterConstants.gateInitPosition);
        }
    }

    public void enableShooter(){
        power = true;
        shooterMotors.setPower(Constants.ShooterConstants.shooterInverted ? -shooterPower : shooterPower);
    }

    public void disableShooter(){
        power = false;
        shooterMotors.setPower(0);
    }

    /**
     * Ability to shoot depends on being in a valid position and motor speed
     * @return Is it safe to shoot?
     */
    public boolean canShoot(){
        // For shooter alignment we only use the april tag on the goal we are trying to shoot in
        AprilTagDetection detection;
        if (RobotState.isRed) {
            detection = Vision.INSTANCE.getDetectionById(24);
        } else {
            detection = Vision.INSTANCE.getDetectionById(20);
        }
        // If we can not see the april tag on the goal, we can not shoot
        if (detection == null){
            return false;
        }
        // Convert to Pedro Coords
        Pose pedroPose = new Pose(detection.robotPose.getPosition().x,
                detection.robotPose.getPosition().y,
                detection.robotPose.getOrientation().getYaw(),
                FTCCoordinates.INSTANCE).getAsCoordinateSystem(PedroCoordinates.INSTANCE);
        // Check 1: Is bot in launch zone?
        if (!DetectInLaunchZone.isInLaunchZone(pedroPose.getX(), pedroPose.getY())){
            return false;
        }
        // Check 2: Is bot in range?
        if (detection.ftcPose.range > maxRange){
            return false;
        }
        // Check 3: Is angle reasonable
        if (Math.abs(detection.ftcPose.bearing) > maxGoalAngle){
            return false;
        }

        // If all checks pass the bot can shoot
        return true;
    }
}
