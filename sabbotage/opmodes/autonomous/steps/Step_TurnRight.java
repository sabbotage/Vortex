package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_TurnRight implements StepInterface {

    private static final double TARGET_TOLERANCE = 1;
    private static final double HIGH_POWER = .8;
    protected Robot robot;

    private boolean encodersReset = false;
    protected Double targetAngle;
    private int delayUntilLoopCount = 0;


    public String getLogKey() {

        return "Step_TurnRight";
    }

    //constructor
    public Step_TurnRight(double angleDegrees) {

        this.targetAngle = angleDegrees;

    }


    @Override
    public void runStep() {

        resetEncodersAndSetMotorDirectionOnlyOnce();

        if (isStillWaiting()) {
            return;
        }


//        Log.w(getLogKey(), "Remaining Angle..." + remainingAngle() + " motorPower: " + robot.motorRightFront.getPower());

        logIt("runStep");

        turn();


    }

    protected double determinePower() {


        double absRemainingAngle = Math.abs(remainingAngle());

        if (absRemainingAngle > 60) {

            return HIGH_POWER;
        }

        if (absRemainingAngle < 8) {
            return 0.1;
        }

        return HIGH_POWER * absRemainingAngle / (60);


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
            robot.motorRightFront.setPower(-power);
            robot.motorLeftFront.setPower(+power);
        } else {
            robot.motorRightFront.setPower(+power);
            robot.motorLeftFront.setPower(-power);
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

    private void resetEncodersAndSetMotorDirectionOnlyOnce() {

        if (encodersReset == false) {
            Log.w(getLogKey(), "resetEncodersAndSetMotorDirectionOnlyOnce..." + robot.loopCounter);
            robot.motorLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            robot.motorLeftFront.setDirection(DcMotor.Direction.FORWARD);
            robot.motorRightFront.setDirection(DcMotor.Direction.REVERSE);


            setLoopDelay();
            encodersReset = true;
        }

    }

    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }


    @Override
    public boolean isStepDone() {


        if (isStillWaiting() || encodersReset == false) {
            return false;

        }


        if (isAtTargetAngle()) {

            robot.motorRightFront.setPower(0);
            robot.motorLeftFront.setPower(0);
            logIt("isStepDone");
            return true;
        }


        return false;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append("methodName:" + methodName);
        sb.append(" , LoopCounter:" + robot.loopCounter);
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
