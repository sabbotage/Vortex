package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_FindColorLine implements StepInterface {

    private static final int COLOR_SIGNAL_RED_VALUE = 40;
    private static final int COLOR_SIGNAL_BLUE_VALUE = 30;
    private static final int COLOR_SIGNAL_WHITE_VALUE = 80;

    private Robot.MotorPowerEnum motorPowerEnum = Robot.MotorPowerEnum.LowLow;
    private Robot robot;


    private boolean initializedStep_DoneFlag = false;

    private boolean foundLineFrontSensor_Done_Flag = false;

    private Robot.ColorEnum colorEnum;

    // Constructor, called to create an instance of this class.
    public Step_FindColorLine(Robot.ColorEnum colorEnum) {

        this.colorEnum = colorEnum;

    }


    @Override
    public String getLogKey() {
        return "Step_FindColorLine";
    }

    @Override
    public void runStep() {

        initializeStep();

        if (robot.isStillWaiting()) {
            return;
        }


        double motorPower = this.motorPowerEnum.getValue();
        robot.motorRightFront.setPower(motorPower);
        robot.motorRightRear.setPower(motorPower);
        robot.motorLeftFront.setPower(motorPower);
        robot.motorRightRear.setPower(motorPower);


        logIt("runStep: ");
    }

    private void initializeStep() {

        if (initializedStep_DoneFlag == false) {

            robot.runWithoutEncoders();
            robot.setDriveMotorForwardDirection();
            robot.colorSensorFloor.enableLed(true);

            initializedStep_DoneFlag = true;

            robot.setLoopDelay();
        }

    }

    private boolean hasFoundRedLine() {


        if (robot.colorSensorFloor.red() > COLOR_SIGNAL_RED_VALUE) {

            return true;
        }

        return false;

    }

    private boolean hasFoundWhiteLine() {


        if (robot.colorSensorFloor.green() > COLOR_SIGNAL_WHITE_VALUE) {

            return true;
        }

        return false;

    }

    private boolean hasFoundBlueLine() {


        if (robot.colorSensorFloor.blue() > COLOR_SIGNAL_BLUE_VALUE) {

            return true;
        }

        return false;

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
            robot.motorLeftRear.setPower(0);


            logIt("isStepDone: ");

            return true;
        }

        return false;
    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + robot.loopCounter);
        sb.append("  , Results:" + "ColorSensor White:" + robot.colorSensorFloor.green());


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

