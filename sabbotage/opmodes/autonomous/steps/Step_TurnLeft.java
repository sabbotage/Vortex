package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps;

public class Step_TurnLeft extends Step_TurnRight {


    public Step_TurnLeft(double angleDegrees) {
        super(angleDegrees);
    }


    @Override
    public String getLogKey() {
        return "Step_TurnLeft";
    }


    @Override
    protected double remainingAngle() {


        return this.targetAngle - robot.gyroSensor.getIntegratedZValue();
    }


    @Override
    protected double determinePower() {

        return -super.determinePower();

    }

}
