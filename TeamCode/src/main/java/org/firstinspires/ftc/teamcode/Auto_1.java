package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ellen.estep.one on 11/17/2017.
 */

@Autonomous(name = "Autonomous_TurnLeft" , group = "AutonomousOpMode")
public class Auto_1 extends LinearOpMode {

    // Declare Opmode Members
    DcMotor motorLeft = null;
    DcMotor motorRight = null;


    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize Motors
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");


        // Set Motor Direction
        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorRight.setDirection(DcMotorSimple.Direction.FORWARD);


        // Set Motor Channel Modes
        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        motorLeft.setPower(-1.0);
        motorRight.setPower(1.0);

        ElapsedTime eTime = new ElapsedTime();
        eTime.reset();

        while (eTime.time() < 2.5){}

        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
}





