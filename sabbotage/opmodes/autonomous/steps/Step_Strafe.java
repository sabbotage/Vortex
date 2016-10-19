package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_Strafe implements StepInterface {

    private final Integer distanceEncoderCounts;
    private final Robot.StrafeEnum direction;
    private final Robot.MotorPowerEnum motorPowerEnum;

    private Robot robot;

    private boolean resetMotors_DoneFlag = false;
    private boolean initializedMotors_DoneFlag = false;

    private final int DONE_TOLERANCE = 100;


    private static final double MOTOR_POWER_BALANCE_FACTOR = 1.0;


    // Constructor, called to create an instance of this class.
    public Step_Strafe(Integer distanceEncoderCounts, Robot.StrafeEnum direction) {
        this.distanceEncoderCounts = distanceEncoderCounts;
        this.direction = direction;
        this.motorPowerEnum = Robot.MotorPowerEnum.FTL;
    }


    @Override
    public String getLogKey() {
        return "Step_Strafe";
    }


    @Override
    public void runStep() {

        resetEncodersAndStopMotors_Only_Once();

        if (robot.isStillWaiting()) {
            return;
        }

        initializeMotors_Only_Once();

        if (robot.isStillWaiting()) {
            return;
        }

        strafe();

    }


    private DcMotor getEncoderMotor() {

        if (Robot.StrafeEnum.RIGHT.equals(this.direction)) {

            return robot.motorRightRear;
        } else {

            return robot.motorLeftRear;
        }
    }

    private void strafe() {

        double motorPower = determineMotorPower();

        if (Robot.StrafeEnum.RIGHT.equals(this.direction)) {

            robot.motorRightFront.setPower(-motorPower);
            robot.motorRightRear.setPower(motorPower);
            robot.motorLeftFront.setPower(motorPower);
            robot.motorLeftRear.setPower(-motorPower);

        } else {

            robot.motorRightFront.setPower(motorPower);
            robot.motorRightRear.setPower(-motorPower);
            robot.motorLeftFront.setPower(-motorPower);
            robot.motorLeftRear.setPower(motorPower);
        }

    }

    private double determineMotorPower() {


        return this.motorPowerEnum.getValue();

    }


    private void resetEncodersAndStopMotors_Only_Once() {

        if (resetMotors_DoneFlag == false) {

            robot.resetEncodersAndStopMotors();
            resetMotors_DoneFlag = true;

            robot.setLoopDelay();
        }

    }


    private void initializeMotors_Only_Once() {


        if (initializedMotors_DoneFlag == false) {

            robot.setDriveMotorForwardDirection();

            robot.runWithoutEncoders();

            DcMotor encoderMotor = getEncoderMotor();
            encoderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            encoderMotor.setTargetPosition(distanceEncoderCounts);

            robot.setLoopDelay();
            initializedMotors_DoneFlag = true;

            logIt("initializeMotors_Only_Once ...");
        }
    }


    private int getRemainingDistance() {

        DcMotor encoderMotor = getEncoderMotor();

        return Math.abs(encoderMotor.getTargetPosition() - encoderMotor.getCurrentPosition());
    }


    @Override
    public boolean isStepDone() {

        if (robot.isStillWaiting() || resetMotors_DoneFlag == false || initializedMotors_DoneFlag == false) {
            return false;
        }


        if (isDistanceDone() || robot.touchSensor.isPressed()) {

            logIt("Step is Done:");

            robot.runWithoutEncoders();

            robot.motorRightFront.setPower(0);
            robot.motorRightRear.setPower(0);
            robot.motorLeftFront.setPower(0);
            robot.motorLeftRear.setPower(0);

            return true;
        }

        return false;
    }

    private boolean isDistanceDone() {

        return Math.abs(getRemainingDistance()) <= DONE_TOLERANCE;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName);
        sb.append(" CurrentPosition:" + robot.motorLeftFront.getCurrentPosition());
        sb.append(" Target:" + robot.motorLeftFront.getTargetPosition());
        sb.append(" Remaining:" + this.getRemainingDistance());
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
