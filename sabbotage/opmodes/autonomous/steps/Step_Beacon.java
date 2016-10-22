package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_Beacon implements StepInterface {


    private static final double SERVO_PRESS_BUTTON_A__PRESS_POSITION = 0.9;
    private static final double SERVO_PRESS_BUTTON_A__RESET_POSITION = 0.0;

    private static final double SERVO_PRESS_BUTTON_B__PRESS_POSITION = 0.9;
    private static final double SERVO_PRESS_BUTTON_B__RESET_POSITION = 0.0;


    private static final int RED_LIGHT_DETECTED_VALUE = 3;
    private static final int BLUE_LIGHT_DETECTED_VALUE = 3;

    private Robot.TeamEnum teamEnum;

    private Robot.TeamEnum detectedBeaconColor;
    private Robot robot;
    private Long targetWaitTimeMillSec;
    private boolean buttonHasBeenPressed_Done_Flag = false;

    //constructor
    public Step_Beacon(Robot.TeamEnum teamEnum) {

        this.teamEnum = teamEnum;
    }

    @Override
    public String getLogKey() {
        return "Step_Beacon";
    }


    @Override
    public void runStep() {

        if (buttonHasBeenPressed_Done_Flag) {
            logIt("NO MORE RUN MODE");
            return;
        }

        logIt("runStep");

        if (detectedBeaconColor()) {

            if (this.teamEnum.equals(this.detectedBeaconColor)) {

                pressButtonA();

            } else {

                pressButtonB();

            }
        }

    }


    private boolean detectedBeaconColor() {

        int red = robot.colorSensorBeacon.red();
        int blue = robot.colorSensorBeacon.blue();

        if (isOnlyRedLightDetected()) {
            logIt("FOUND COLOR RED! " + red);
            detectedBeaconColor = Robot.TeamEnum.RED;
            return true;
        }
        if (isOnlyBlueLightDetected()) {
            logIt("FOUND COLOR BLUE! " + blue);
            detectedBeaconColor = Robot.TeamEnum.BLUE;
            return true;
        }

        return false;

    }

    private boolean isOnlyRedLightDetected() {

        if (robot.colorSensorBeacon.red() >= RED_LIGHT_DETECTED_VALUE
                &&
                robot.colorSensorBeacon.blue() == 0) {

            Log.i(getLogKey() + "_T", "Beacon ColorSensor Red/Blue DONE BLUE:" + robot.colorSensorBeacon.red() + "/" + robot.colorSensorBeacon.blue());

            return true;
        }


        return false;
    }

    private boolean isOnlyBlueLightDetected() {


        if (robot.colorSensorBeacon.blue() >= BLUE_LIGHT_DETECTED_VALUE
                &&
                robot.colorSensorBeacon.red() == 0) {

            Log.i(getLogKey() + "_T", "Beacon ColorSensor Red/Blue DONE BLUE:" + robot.colorSensorBeacon.red() + "/" + robot.colorSensorBeacon.blue());

            return true;
        }


        return false;
    }

    private void pressButtonA() {

        if (buttonHasBeenPressed_Done_Flag == false) {
            robot.servoPressButtonA.setDirection(Servo.Direction.FORWARD);
            robot.servoPressButtonA.setPosition(SERVO_PRESS_BUTTON_A__PRESS_POSITION);
            setLoopTimeDelay();
            buttonHasBeenPressed_Done_Flag = true;

            logIt("pressButtonA");
        }
    }

    private void pressButtonB() {

        if (buttonHasBeenPressed_Done_Flag == false) {
            robot.servoPressButtonB.setDirection(Servo.Direction.FORWARD);
            robot.servoPressButtonB.setPosition(SERVO_PRESS_BUTTON_B__PRESS_POSITION);
            setLoopTimeDelay();
            buttonHasBeenPressed_Done_Flag = true;
            logIt("pressButtonB");
        }
    }

    private void setLoopTimeDelay() {

        targetWaitTimeMillSec = System.currentTimeMillis() + 3000;
    }


    private boolean isStillTimeWaiting() {

        if (targetWaitTimeMillSec > System.currentTimeMillis()) {
            logIt("STILL WAITING: " + (targetWaitTimeMillSec - System.currentTimeMillis()));
            return true;
        }
        return false;

    }

    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName + " " + robot.loopCounter);
        sb.append(" Red:" + robot.colorSensorBeacon.red());
        sb.append(" Blue:" + robot.colorSensorBeacon.blue());
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


    @Override
    public boolean isStepDone() {

        if (this.buttonHasBeenPressed_Done_Flag && isStillTimeWaiting() == false) {

            robot.servoPressButtonA.setPosition(SERVO_PRESS_BUTTON_A__RESET_POSITION);
            robot.servoPressButtonB.setPosition(SERVO_PRESS_BUTTON_B__RESET_POSITION);
            logIt("DONE");
            return true;
        }

        return false;
    }

}
