package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_TurnRight implements StepInterface {

    private static final double TARGET_TOLERANCE = 1;
    private static final double HIGH_POWER = .6;
    protected Robot robot;

    private boolean resetMotors = false;
    protected Double targetAngle;
    private int delayUntilLoopCount = 0;


    public String getLogKey() {

        return "OLD_Step_TurnRight";
    }

    //constructor
    public Step_TurnRight(double angleDegrees) {

        this.targetAngle = angleDegrees;

    }


    @Override
    public void runStep() {

        resetMotors();

        if (isStillWaiting()) {
            return;
        }

        logIt("runStep");

        robot.runWithoutEncoders();

        turn();

    }

    protected double determinePower() {


        double absRemainingAngle = Math.abs(remainingAngle());

        int slowDownAngle = 60;
        if (absRemainingAngle > slowDownAngle) {

            return HIGH_POWER;
        }

        if (absRemainingAngle < 8) {
            return 0.1;
        }

        return HIGH_POWER * absRemainingAngle / (slowDownAngle);


    }


    private boolean isAtTargetAngle() {

        return Math.abs(remainingAngle()) <= TARGET_TOLERANCE;
    }

    protected double remainingAngle() {

        return this.targetAngle + robot.gyroSensor.getIntegratedZValue();

    }

    private void turn() {


        double power = determinePower();

        if (hasOverShotTargetAngle()) {

            robot.motorRightFront.setPower(+power);
            robot.motorRightRear.setPower(+power);

            robot.motorLeftFront.setPower(-power);
            robot.motorLeftRear.setPower(-power);


        } else {

            robot.motorRightFront.setPower(-power);
            robot.motorRightRear.setPower(-power);

            robot.motorLeftFront.setPower(+power);
            robot.motorLeftRear.setPower(+power);

        }
    }

    private boolean hasOverShotTargetAngle() {
        return remainingAngle() < 0;
    }


    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }

    private void resetMotors() {

        if (resetMotors == false) {
            Log.w(getLogKey(), "resetEncodersAndStopMotors..." + robot.loopCounter);

            robot.runWithoutEncoders();
            robot.setDriveMotorForwardDirection();

            setLoopDelay();
            resetMotors = true;
        }

    }

    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }


    @Override
    public boolean isStepDone() {


        if (isStillWaiting() || resetMotors == false) {
            return false;

        }


        if (isAtTargetAngle()) {

            robot.motorRightFront.setPower(0);
            robot.motorRightRear.setPower(0);
            robot.motorLeftFront.setPower(0);
            robot.motorLeftRear.setPower(0);
            logIt("isStepDone");
            return true;
        }


        return false;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(" , CurrentAngle:" + robot.gyroSensor.getIntegratedZValue());
        sb.append(" , TargetAngle:" + targetAngle);
        sb.append(" , RemainingAngle:" + remainingAngle());
        sb.append(" , L Power:" + robot.motorLeftFront.getPower() + " , R Power:" + robot.motorRightFront.getPower());

        Log.i(getLogKey(), sb.toString());

    }


    @Override
    public boolean isAborted() {
        return false;
    }


    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


}
