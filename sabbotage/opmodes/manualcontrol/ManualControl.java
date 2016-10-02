
package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.manualcontrol;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;


public class ManualControl extends OpMode {

    private static final String KEY = "Manual";

    private Robot robot = new Robot(hardwareMap);

    private DcMotor motorRightFront;
    private DcMotor motorRightRear;
    private DcMotor motorLeftFront;
    private DcMotor motorLeftRear;


    private int loopCounter = 0;


    public ManualControl() {
    }


    @Override
    public void init() {


    }


    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        loopCounter = loopCounter + 1;

        driver_controlDriveMotors();


    }


    private void driver_controlDriveMotors() {


//        FrontLeft = Ch3 + Ch1 + Ch4
//        RearLeft = Ch3 + Ch1 - Ch4
//        FrontRight = Ch3 - Ch1 - Ch4
//        RearRight = Ch3 - Ch1 + Ch4

//        float forward_leftY = -gamepad1.left_stick_y;
//        float turn_rightX = gamepad1.right_stick_x;
//        float strafing = gamepad1.left_stick_x;


        float weightFactor_Forward = 0.33f;
        float weightFactor_TankTurn = 0.33f;
        float weightFactor_Strafing = 0.33f;

        float forward =  -gamepad1.left_stick_y * weightFactor_Forward ;
        float tankTurn = gamepad1.right_stick_x * weightFactor_TankTurn;
        float strafing = gamepad1.left_stick_x * weightFactor_Strafing;


        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        float frontLeft = (float) forward + tankTurn + strafing;
        float rearLeft = (float) forward + tankTurn - strafing;
        float frontRight = (float) forward - tankTurn - strafing;
        float rearRight = (float) forward - tankTurn + strafing;

// tankTurn ONLY
//        float frontLeft = (float) tankTurn_rightX;
//        float rearLeft = (float) tankTurn_rightX;
//        float frontRight = (float) -tankTurn_rightX;
//        float rearRight = (float) -tankTurn_rightX;
//        Log.i(KEY, " tankTurn_rightX: " + String.format("%.2f", tankTurn_rightX) + " output " + String.format("%.2f", motorRightFront.getPower()));

//        forward_backward ONLY
//        float frontLeft = (float) forward_backward_leftY;
//        float rearLeft = (float) forward_backward_leftY;
//        float frontRight = (float) forward_backward_leftY;
//        float rearRight = (float) forward_backward_leftY;
//        Log.i(KEY, " forward_backward_leftY: " + String.format("%.2f", forward_backward_leftY) + " output " + String.format("%.2f", motorRightFront.getPower()));

        // write the values to the motors
        robot.motorRightFront.setPower(limitValue(frontRight));
        robot.motorRightRear.setPower(limitValue(rearRight));
        robot.motorLeftFront.setPower(limitValue(frontLeft));
        robot.motorLeftRear.setPower(limitValue(rearLeft));


//        Log.i(KEY, " tankTurn_rightX: " + String.format("%.2f", tankTurn_rightX) + " output " + String.format("%.2f", motorRightFront.getPower()));
//        Log.i(KEY, " motorRightFront: " + String.format("%.2f", motorRightFront.getPower()));
//        Log.i(KEY, " motorRightRear: " + String.format("%.2f", motorRightRear.getPower()));
//        Log.i(KEY, " motorLeftFront: " + String.format("%.2f", motorLeftFront.getPower()));
//        Log.i(KEY, " motorLeftRear: " + String.format("%.2f", motorLeftRear.getPower()));

    }


    private float limitValue(float input) {

        if (input > 1.0) {

            return 1.0f;
        } else if (input < -1.0) {
            return -1.0f;
        }
        return input;
    }


    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

        telemetry.addData("TextStop", "***Stop happened**" + loopCounter);

    }

    /*
     * This method scales the joystick inputValue so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleOutput(double inputValue, double[] scaleArray) {


        // get the corresponding index for the scaleOutput array.
        int index = (int) (inputValue * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (inputValue < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
