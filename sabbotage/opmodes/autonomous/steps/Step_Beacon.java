package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;


import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

public class Step_Beacon implements StepInterface {


    private Robot.TeamEnum teamEnum;
    private Robot robot;
    private Long waitTimeMillSec;
    private Long endTimeMillSeconds;
    private boolean buttonHasBeenPressed = false;

    //constructor
    public Step_Beacon(Robot.TeamEnum teamEnum) {

        this.teamEnum = teamEnum;


    }

    @Override
    public String getLogKey() {
        return "Step_Wait";
    }


    @Override
    public void runStep() {

        Robot.TeamEnum beaconColor = determineBeaconColor();


        if (beaconColor.equals(this.teamEnum)) {

            pressButtonA();

        } else {
            pressButtonA();
        }


        if (this.endTimeMillSeconds == null) {
            this.endTimeMillSeconds = System.currentTimeMillis() + this.waitTimeMillSec;
        }

    }


    private Robot.TeamEnum determineBeaconColor() {

        //todo, return the stronger measured color.


        return Robot.TeamEnum.BLUE;
    }


    private void pressButtonA() {
//TODO
    }


    private void pressButtonB() {
//TODO
    }

    @Override
    public boolean isStepDone() {

        if (this.buttonHasBeenPressed) {
            return true;
        }

        return false;
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
