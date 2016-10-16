package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class X_Step_AngleForward implements StepInterface {

    private final Integer distanceEncoderCounts;
    private final DcMotor.Direction direction;
    private final Robot.MotorPowerEnum motorPowerEnum = Robot.MotorPowerEnum.FTL;

    private Robot robot;

    private boolean initializedMotors = false;
    private boolean encodersReset = false;

    private int delayUntilLoopCount = 0;
    private final int DONE_TOLERANCE = 600;
    private static final double MOTOR_POWER_BALANCE_FACTOR = 1.0;

    //constructor
    public X_Step_AngleForward(Integer distanceEncoderCounts, DcMotor.Direction direction) {
        this.distanceEncoderCounts = distanceEncoderCounts + DONE_TOLERANCE;
        this.direction = direction;
    }


    @Override
    public String getLogKey() {
        return "Step_Straight";
    }

    @Override
    public void runStep() {


        resetEncodersAndSetMotorDirectionOnlyOnce();

        if (isStillWaiting()) {
            return;
        }


        initializeMotors();

        double motorPower = determinePower();
        robot.motorRightFront.setPower(0);
        robot.motorRightRear.setPower(motorPower);
        robot.motorLeftFront.setPower(motorPower);
        robot.motorLeftRear.setPower(0);


//        Log.w(getLogKey(), "remaining:" + rightRemainingDistance() + " motor power..." + robot.motorRightFront.getPower() + "    " + robot.loopCounter);

        logIt("Loop:");
    }


    private double determinePower() {

        return .50;

//        int remainingDistance = rightRemainingDistance();
//
//        if (remainingDistance > 1500) {
//
//            return this.motorPowerEnum.getValue();
//        }
//
//        if (remainingDistance < 200) {
//
//            return 0.1;
//        }
//
//
//        return this.motorPowerEnum.getValue() * rightRemainingDistance() / 1500;

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
            robot.motorLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorLeftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorRightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            if (direction.equals(DcMotor.Direction.FORWARD)) {

                robot.motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
                robot.motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
                robot.motorRightFront.setDirection(DcMotor.Direction.FORWARD);
                robot.motorRightRear.setDirection(DcMotor.Direction.FORWARD);


            } else {

                robot.motorLeftFront.setDirection(DcMotor.Direction.FORWARD);
                robot.motorLeftRear.setDirection(DcMotor.Direction.FORWARD);
                robot.motorRightFront.setDirection(DcMotor.Direction.REVERSE);
                robot.motorRightRear.setDirection(DcMotor.Direction.REVERSE);
            }

            setLoopDelay();
            encodersReset = true;
        }

    }


    private void initializeMotors() {


        if (initializedMotors == false) {

            Log.w(getLogKey(), "initializeMotors..." + robot.loopCounter);

            robot.motorLeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorLeftFront.setTargetPosition(distanceEncoderCounts);

            robot.motorLeftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            robot.motorRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            robot.motorRightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


            initializedMotors = true;
            setLoopDelay();


            StringBuilder sb = new StringBuilder();
            sb.append("initializeMotors" + robot.loopCounter);
            sb.append(" , CurrentPosition:" + robot.motorLeftFront.getCurrentPosition() + " , " + robot.motorRightFront.getCurrentPosition());
            sb.append(" ,TargetPosition:" + robot.motorLeftFront.getTargetPosition() + " , " + robot.motorRightFront.getTargetPosition());
            sb.append(" , Delta:" + (rightRemainingDistance() - getLeftRemainingPosition()));
            sb.append(" , LeftRemaining :" + getLeftRemainingPosition());
            sb.append(" , RightRemaining:" + rightRemainingDistance());


            Log.i(getLogKey(), sb.toString());

        }
    }

    private int rightRemainingDistance() {

        return Math.abs(robot.motorRightFront.getTargetPosition() - robot.motorRightFront.getCurrentPosition());
    }


    private int getLeftRemainingPosition() {

        return Math.abs(robot.motorLeftFront.getTargetPosition() - robot.motorLeftFront.getCurrentPosition());
    }


    @Override
    public boolean isStepDone() {

        if (isStillWaiting() || encodersReset == false || initializedMotors == false) {
            return false;
        }


        if (isMotorLeftDone()) {

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

    private boolean isMotorLeftDone() {

        return Math.abs(getLeftRemainingPosition()) <= DONE_TOLERANCE;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);
//        sb.append(" , RightRemaining:" + (robot.motorRightFront.getTargetPosition() - robot.motorRightFront.getCurrentPosition()));
//        sb.append(" , Delta:" + (rightRemainingDistance() - getLeftRemainingPosition()));
        sb.append(" , LeftRemaining :" + (robot.motorLeftFront.getTargetPosition() - robot.motorLeftFront.getCurrentPosition()));
        sb.append(" , LeftCurrent :" + (robot.motorLeftFront.getCurrentPosition()) + " rightCurrent:" + robot.motorRightFront.getCurrentPosition());
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
