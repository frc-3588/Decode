package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.Constants;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.MotorEx;

public class Shooter implements Subsystem {
    public boolean shooterEnabled = true;
    public double getShooterVelocity() {
        return controller.getGoal().getVelocity();
    };

    public void toggleShooter(){
        if (controller.getGoal().getVelocity() > 100){
            Shooter.INSTANCE.shooterOff.schedule();
        } else {
            Shooter.INSTANCE.shooterOnClose.schedule();
        }
    }
    MotorEx motor1 = new MotorEx("shooter");
    TelemetryManager telemetryManager = PanelsTelemetry.INSTANCE.getTelemetry();
    private boolean adaptive = false;
    public static final Shooter INSTANCE = new Shooter() {};
    private Shooter() { }
    private final ControlSystem controller = ControlSystem.builder()
            .velPid(Constants.ShooterConstants.shooterPIDCoefficients)
            .basicFF(Constants.ShooterConstants.shooterFeedForward)
            .build();
    private final ControlSystem adaptiveController = ControlSystem.builder()
            .velPid(Constants.ShooterConstants.shooterPIDCoefficients)
            .basicFF(Constants.ShooterConstants.shooterFeedForward)
            .build();
    public final Command shooterOff = new InstantCommand(()->setControllerGoal(0)).requires(this).named("FlywheelOff");
    public final Command shooterOnClose = new InstantCommand(()->setControllerGoal(Constants.ShooterConstants.closeShootVelocity)).requires(this).named("FlywheelClose");
    public final Command shooterOnMedium = new InstantCommand(()->setControllerGoal(Constants.ShooterConstants.mediumShootVelocity)).requires(this).named("FlywheelMedium");
    public final Command shooterOnFar = new InstantCommand(()->setControllerGoal(Constants.ShooterConstants.longShootVelocity)).requires(this).named("FlywheelFar");

    public void toggleAdaptive(){
        adaptive = !adaptive;
    }

    @Override
    public void periodic() {
        if (!shooterEnabled){
            return;
        }
        if (adaptive){
            telemetryManager.addData("ADAPTIVE", true);
            double calculatedVelocity = calculateShooterAdaptiveVelocity();
            telemetryManager.addData("Calculated Adaptive Velocity", calculatedVelocity);
            adaptiveController.setGoal(new KineticState(0, calculatedVelocity));
            double goal = adaptiveController.calculate(motor1.getState());
            telemetryManager.addData("Shooter Adaptive Goal", goal);
            motor1.setPower(goal);
        } else {
            telemetryManager.addData("ADAPTIVE", false);
            motor1.setPower(controller.calculate(motor1.getState()));
        }
//        if (loadedSensor.get())
//        if (autoEnable && loadedSensor.get() && motor1.getVelocity() < 0.1){
//            shooterOn.schedule();
//        } else if (autoEnable && !loadedSensor.get() && motor1.getVelocity() > 0.1){
//            shooterOff.schedule();
//        }

        telemetryManager.addData("Current Shooter Velocity", motor1.getVelocity());
        telemetryManager.addData("Shooter Power", motor1.getPower());
    }

    @Override
    public void initialize() {
        controller.setGoal(new KineticState(0,0,0));
    }

    public double calculateShooterAdaptiveVelocity() {
            LLResultTypes.FiducialResult blueTag = VisionLL.INSTANCE.getTag(20);
            LLResultTypes.FiducialResult redTag = VisionLL.INSTANCE.getTag(24);

            double llDist = 0;

            if (blueTag != null){
                llDist = blueTag.getTargetPoseCameraSpace().getPosition().z;
                telemetryManager.addData("LL Dist", llDist);


                return (Constants.ShooterConstants.shooterInterpolationYIntercept + (llDist * Constants.ShooterConstants.shooterInterpolationSlope));
            } else if (redTag != null){
                llDist = redTag.getTargetPoseCameraSpace().getPosition().z;
                telemetryManager.addData("LL Dist", llDist);
               return (Constants.ShooterConstants.shooterInterpolationYIntercept + (llDist * Constants.ShooterConstants.shooterInterpolationSlope));
            }

            return Constants.ShooterConstants.closeShootVelocity;
    }

    public void setControllerGoal(double velocity){
        controller.setGoal(new KineticState(0, velocity));
    }
}


