/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps.StepInterface;
import org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps.Step_Straight;
import org.firstinspires.ftc.teamcode.vortex.sabbotage.opmodes.autonomous.steps.Step_TurnRight;
import org.firstinspires.ftc.teamcode.vortex.sabbotage.robot.Robot;

import java.util.ArrayList;

@Autonomous(name = "Tester", group = "Red")
public class Tester extends AutonomousOp {


    protected ArrayList<StepInterface> definedStepList() {


        // Here are our steps (in order) that make up our rescue plan.
        ArrayList<StepInterface> definedStepList = new ArrayList<StepInterface>();

        definedStepList.add(new Step_Straight(2000, Robot.DirectionEnum.FORWARD));
//        definedStepList.add(new Step_Strafe(1000, DcMotorSimple.Direction.FORWARD));
        definedStepList.add(new Step_TurnRight(180));
//        definedStepList.add(new Step_Strafe(1000, DcMotorSimple.Direction.REVERSE));
//        definedStepList.add(new Step_TurnLeft(0));


        return definedStepList;
    }


}