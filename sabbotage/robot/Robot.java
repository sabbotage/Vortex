package org.firstinspires.ftc.teamcode.vortex.sabbotage.robot;

import android.util.Log;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Robot {


    public int HARDWARE_DELAY = 10;

    public int loopCounter;
    public Telemetry telemetry;


    public DcMotor motorRightFront;
    public DcMotor motorRightRear;
    public DcMotor motorLeftFront;
    public DcMotor motorLeftRear;

    public ModernRoboticsI2cGyro gyroSensor;
    public ColorSensor colorSensor;


    private HardwareMap hardwareMap;


    public Robot(HardwareMap hardwareMap) {

        this.hardwareMap = hardwareMap;

        if (this.hardwareMap == null) {

            Log.i("ROBOT", "hardwareMap is null");

        }


        this.motorLeftFront = this.hardwareMap.dcMotor.get("motorLeftFront");
        this.motorLeftRear = this.hardwareMap.dcMotor.get("motorLeftRear");
        this.motorRightFront = this.hardwareMap.dcMotor.get("motorRightFront");
        this.motorRightRear = this.hardwareMap.dcMotor.get("motorRightRear");

        resetHardwarePositions();
    }


    private void resetHardwarePositions() {

        this.motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        this.motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        this.motorRightFront.setDirection(DcMotor.Direction.FORWARD);
        this.motorRightRear.setDirection(DcMotor.Direction.FORWARD);


    }

    public static enum ColorEnum {

        RED,

        BLUE,

        WHITE


    }

    public enum TurnEnum {

        RIGHT,

        LEFT


    }

    public enum MotorPowerEnum {

        LowLow(0.1),

        Low(0.2),

        Med(0.6),

        High(0.8),

        FTL(1.0);

        private double motorPower;

        private MotorPowerEnum(double motorPower) {
            this.motorPower = motorPower;

        }

        public double getValue() {
            return this.motorPower;

        }

    }


}


