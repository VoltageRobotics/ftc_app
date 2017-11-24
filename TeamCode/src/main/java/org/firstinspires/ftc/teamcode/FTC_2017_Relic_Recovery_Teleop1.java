package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@TeleOp(name="FTC_2017_Relic_Recovery_Teleop", group="Linear Opmode")
//@Disabled
public class FTC_2017_Relic_Recovery_Teleop1 extends LinearOpMode {
    //Add opmode members here
    public static final String TAG = "Vuforia VuMark Sample";
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    //declare motors
    DcMotor motorLeft = null;
    DcMotor motorRight = null;
    DcMotor  armUp = null;
    DcMotor motorArm = null;

    Servo ServoLeft = null;
    Servo ServoRight = null;

    final double LEFT_OPEN_POSITION = -1.0;
    final double LEFT_CLOSED_POSITION = 0.450;
    final double RIGHT_OPEN_POSITION = 1.0;
    final double RIGHT_CLOSED_POSITION = 0.645;
    private ElapsedTime runtime = new ElapsedTime();

    @Override public void runOpMode() {


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AaU/7fP/////AAAAGVrLpq60qUfOmy46Mx9XDjlcVUX9C23X6gEozlFookn+7wqDI4lfLwdPyIK4Zj5Z7cYTkpjrEiYTVqKMKEFkIqYFGyY2+EJ2hUDJlzomLp7TUegSM89kgzIaAlX4q+1OXT9Rr6W7TTsUzn3yNWQC3eol7HiVz6TGMqixQw7nMry1XHBhaEiNOv3IztAEEBMIf4w4lApDYNkwkEQKT82fQxBGRbPhR4k1w15fEX2z0l0fpieCj1bPM1mxSJT+s1kjDppiq+lACVcNUUNC5GmHxOm4eZOI0NO458XzUwhk1I98Hs8HA2BO4f0qjSyIljKD4/cnuXorDFboLcUclVbag9Kc4btnSlYd39DDPxl7BfcY";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        //Init motors
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        armUp = hardwareMap.dcMotor.get("armUp");
        motorArm = hardwareMap.dcMotor.get("motorArm");

        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorRight.setDirection(DcMotorSimple.Direction.FORWARD);

        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armUp.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        int position = armUp.getCurrentPosition();
        telemetry.addData("Encoder Position", position);

        ServoLeft = hardwareMap.servo.get("ServoLeft");
        ServoRight = hardwareMap.servo.get("ServoRight");


        runtime.reset();

        relicTrackables.activate();

        while (opModeIsActive()) {


            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {


                telemetry.addData("VuMark", "%s visible", vuMark);


                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));


                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");

                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.update();
            }

            //Drive code
            //Drive Train
            motorLeft.setPower(-gamepad1.left_stick_y);
            motorRight.setPower(-gamepad1.right_stick_y);

            //Arm lift 1st position
            if (gamepad1.a)
            {
                armUp.setTargetPosition(115);
                armUp.setPower(0.3);
            }
            //Second deploy stage
             if (gamepad1.b)
            {
                armUp.setTargetPosition(224);
                armUp.setPower(0.3);
            }

            //Third deploy stage
             if (gamepad1.y){
                armUp.setTargetPosition(460);
                armUp.setPower(0.3);
            }
             if (gamepad1.x){
                 armUp.setTargetPosition(550);
                 armUp.setPower(0.3);
             }
            //else
            {
              //  armUp.setTargetPosition(560);
                //armUp.setPower(0.2);
                //armUp.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                
            }

            //Servo code
            if (gamepad2.a){
            ServoLeft.setPosition(LEFT_OPEN_POSITION);
            ServoRight.setPosition(RIGHT_OPEN_POSITION);
        }

            if (gamepad2.b){
                ServoLeft.setPosition(LEFT_CLOSED_POSITION);
                ServoRight.setPosition(RIGHT_CLOSED_POSITION);
            }
            //Lift extender
            if (gamepad2.dpad_up){
                motorArm.setPower(0.4);
            }
            else if (gamepad2.dpad_down){
                motorArm.setPower(-0.4);
            }
            else {
                motorArm.setPower(0.0);
            }


        }
            telemetry.update();
        }


    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
