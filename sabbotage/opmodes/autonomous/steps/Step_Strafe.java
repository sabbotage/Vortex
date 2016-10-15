package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_Strafe implements StepInterface {

    private final Integer distanceEncoderCounts;
    private final DcMotor.Direction direction;
    private final Robot.MotorPowerEnum motorPowerEnum = Robot.MotorPowerEnum.Med;

    private Robot robot;

    private boolean resetMotors = false;
    private boolean initializedMotors = false;

    private final int DONE_TOLERANCE = 100;
    private final int SLOW_MODE_REMAINING_DISANCE = 1500;
    private final int VERY_SLOW_MODE_REMAINING_DISTANCE = 200;


    private static final double MOTOR_POWER_BALANCE_FACTOR = 1.0;
    private int delayUntilLoopCount = 0;

    //constructor
    public Step_Strafe(Integer distanceEncoderCounts, DcMotor.Direction direction) {
        this.distanceEncoderCounts = distanceEncoderCounts;
        this.direction = direction;
    }


    @Override
    public String getLogKey() {
        return "Step_Strafe";
    }

    @Override
    public void runStep() {

        resetMotors();

        if (isStillWaiting()) {
            return;
        }

        initializeMotors();

        if (isStillWaiting()) {
            return;
        }

        double motorPower = determinePower();

        robot.motorRightFront.setPower(-motorPower);
        robot.motorRightRear.setPower(motorPower);
        robot.motorLeftFront.setPower(motorPower);
        robot.motorLeftRear.setPower(-motorPower);

    }


    private double determinePower() {


        int remainingDistance = getRemainingDistance();


        if (remainingDistance < VERY_SLOW_MODE_REMAINING_DISTANCE) {

            return 0.5;
        }
//hi
        if (remainingDistance < SLOW_MODE_REMAINING_DISANCE) {

            return this.motorPowerEnum.getValue() * remainingDistance / SLOW_MODE_REMAINING_DISANCE;
        }


        return this.motorPowerEnum.getValue();

    }


    private DcMotor getEncoderMotor() {

        return robot.motorRightRear;

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

            robot.resetEncodersAndStopMotors();

            setLoopDelay();
            resetMotors = true;
        }

    }


    private void initializeMotors() {


        if (initializedMotors == false) {

            initializeMotorDirection();

            robot.runWithoutEncoders();

            DcMotor encoderMotor = getEncoderMotor();
            encoderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            encoderMotor.setTargetPosition(distanceEncoderCounts);

            StringBuilder sb = new StringBuilder();

            sb.append("initializeMotors ...");
            sb.append(" StartPosition:" + encoderMotor.getCurrentPosition());
            sb.append(" TargetPosition" + encoderMotor.getTargetPosition());
            Log.i(getLogKey(), sb.toString());

            setLoopDelay();
            initializedMotors = true;
        }
    }

    private void initializeMotorDirection() {

        if (this.direction.equals(DcMotor.Direction.FORWARD)) {

            robot.setDriveMotorForwardDirection();

        } else {

            robot.setDriveMotorReverseDirection();

        }


    }

    private int getRemainingDistance() {

        DcMotor encoderMotor = getEncoderMotor();

        return Math.abs(encoderMotor.getTargetPosition() - encoderMotor.getCurrentPosition());
    }


    @Override
    public boolean isStepDone() {

        if (isStillWaiting() || resetMotors == false || initializedMotors == false) {
            return false;
        }


        if (isDistanceDone()) {

            logIt("isStepDone:");

            robot.motorLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorLeftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorRightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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


    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
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
