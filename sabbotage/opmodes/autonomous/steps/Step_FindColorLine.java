package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_FindColorLine implements StepInterface {

    private static final int COLOR_SIGNAL_RED_VALUE = 40;
    private static final int COLOR_SIGNAL_BLUE_VALUE = 30;
    private static final int COLOR_SIGNAL_WHITE_VALUE = 80;
    private static final double MOTOR_POWER = .25;
    private Robot robot;


    private boolean encodersReset = false;
    private boolean initialized = false;
    private int delayUntilLoopCount = 0;

    private boolean achievedColorRight = false;
    private boolean achievedColorLeft = false;

    private Robot.ColorEnum colorEnum;

    //constructor
    public Step_FindColorLine(Robot.ColorEnum colorEnum) {

        this.colorEnum = colorEnum;

    }


    @Override
    public String getLogKey() {
        return "Step_FindColorLine";
    }

    @Override
    public void runStep() {

        resetEncodersAndSetMotorDirectionOnlyOnce();

        if (isStillWaiting()) {
            return;
        }


        initializeStep();


        if (isStillWaiting()) {
            return;
        }

        robot.motorRightFront.setPower(MOTOR_POWER);
        robot.motorRightRear.setPower(MOTOR_POWER);
        robot.motorLeftFront.setPower(MOTOR_POWER);
        robot.motorRightRear.setPower(MOTOR_POWER);


        logIt("runStep: ");
    }


    private boolean hasFoundRedLine() {


        if (robot.colorSensor.red() > COLOR_SIGNAL_RED_VALUE

                ) {

            return true;
        }

        return false;

    }

    private boolean hasFoundWhiteLine() {


        if (robot.colorSensor.green() > COLOR_SIGNAL_WHITE_VALUE

                ) {

            return true;
        }

        return false;

    }

    private boolean hasFoundBlueLine() {


        if (robot.colorSensor.blue() > COLOR_SIGNAL_BLUE_VALUE

                ) {

            return true;
        }

        return false;

    }

    private boolean isStillWaiting() {

        if (delayUntilLoopCount > robot.loopCounter) {
            Log.i(getLogKey(), "Waiting..." + robot.loopCounter);
            return true;
        }
        return false;
    }

    private void initializeStep() {

        if (initialized == false) {
            Log.w(getLogKey(), "initializeStep..." + robot.loopCounter);

            robot.colorSensor.enableLed(true);
            setLoopDelay();
            initialized = true;
        }

    }

    private void resetEncodersAndSetMotorDirectionOnlyOnce() {

        if (encodersReset == false) {
            Log.w(getLogKey(), "resetEncodersAndSetMotorDirectionOnlyOnce..." + robot.loopCounter);


            robot.motorRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorRightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorRightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            setLoopDelay();
            encodersReset = true;
        }

    }

    private void setLoopDelay() {

        this.delayUntilLoopCount = robot.loopCounter + robot.HARDWARE_DELAY;
    }

    @Override
    public boolean isStepDone() {


        if (colorEnum.equals(Robot.ColorEnum.RED)
                && hasFoundRedLine()
                ||

                colorEnum.equals(Robot.ColorEnum.BLUE)
                        && hasFoundBlueLine()
                ||

                colorEnum.equals(Robot.ColorEnum.WHITE)
                        && hasFoundWhiteLine())

        {


            robot.motorRightFront.setPower(0);
            robot.motorRightRear.setPower(0);
            robot.motorLeftFront.setPower(0);
            robot.motorRightRear.setPower(0);


            logIt("isStepDone: ");

            return true;
        }

        return false;
    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);
        sb.append("  , Results:" + "ColorSensor Blue:" + robot.colorSensor.blue());
        sb.append("  , ML" + robot.motorLeftFront.getPower() + " MR:" + +robot.motorRightFront.getPower());


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

