package org.firstinspires.ftc.teamcode.New.Competition.Actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.New.Competition.subsystems.Claw;
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.ClawRotater;
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.ColorSensor;
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.ShoudlerJohn;
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.WristJohn;

public class Bromine {
    public Claw claw;
    public ClawRotater clawRotater;
    public ShoudlerJohn shoulder;
    public WristJohn wrist;
    public ColorSensor colorSensor;
    public boolean isAuto = false;

    public Bromine(HardwareMap hardwareMap) {
        claw = new Claw(hardwareMap);
        clawRotater = new ClawRotater(hardwareMap);
        shoulder = new ShoudlerJohn(hardwareMap);
        wrist = new WristJohn(hardwareMap);
        colorSensor = new ColorSensor(hardwareMap);
    }

    //TODO have a way to calculate the yaw of the claw with camera

    public void teleUpdate(Gamepad gamepad, double looptime) {
        claw.update();
        clawRotater.updateTele(gamepad.left_stick_y);
        shoulder.update(looptime);
        wrist.update();
    }
    public void updateAuto(double looptime) {
        claw.update();
        clawRotater.update();
        shoulder.update(looptime);
        wrist.update();
    }

    public Action prepareSampleIntake = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.clawState = Claw.ClawState.OPEN;
            if (isAuto) {
                clawRotater.state = ClawRotater.State.ADJUSTING;
            } else {
                clawRotater.state = ClawRotater.State.INPUT;
            }
            wrist.state = WristJohn.State.SamplePrep;
            shoulder.state = ShoudlerJohn.State.SpecimenIntake;
            return false;
        }
    };

    public Action SampleIntake = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.state = WristJohn.State.Submersible;

            if (colorSensor.checkForRecognition()) {
                claw.clawState = Claw.ClawState.CLOSED;
                return false;
            } else {
                return true;
            }
        }
    };

    //runs to nearest is completed, simultaneously the shoulder is raised and claw is kept closed.

    public Action prepareBasketDeposit = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shoulder.state = ShoudlerJohn.State.BasketDeposit;
            wrist.state = WristJohn.State.Basket;
            clawRotater.state = ClawRotater.State.ZERO;
            return !shoulder.targetReached;
        }
    };

    Action fullDepositBasket = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.clawState = Claw.ClawState.OPEN;
            return false;
        }
    };

    public Action prepareSpecimenDeposit = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.state = WristJohn.State.Upwards;
            clawRotater.state = ClawRotater.State.ZERO;
            shoulder.state = ShoudlerJohn.State.SpecimenDepositPrep;
            return !shoulder.targetReached;
        }
    };

    public Action fullSpecimenDeposit = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.state = WristJohn.State.Upwards;
            clawRotater.state = ClawRotater.State.ZERO;
            shoulder.state = ShoudlerJohn.State.SpecimenDeposit;
            return !shoulder.targetReached;
        }
    };

    public Action prepareSpecimenWallIntake = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.state = WristJohn.State.WallIntake;
            shoulder.state = ShoudlerJohn.State.SpecimenIntake;
            clawRotater.state = ClawRotater.State.ZERO;
            return !shoulder.targetReached;
        }
    };

    public Action SpecimenWallIntake = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (colorSensor.checkForRecognition()) {
                claw.clawState = Claw.ClawState.CLOSED;
            }
            return !colorSensor.checkForRecognition();
        }
    };


    public Action prepForHPdrop = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            wrist.state = WristJohn.State.HpDrop;
            shoulder.state = ShoudlerJohn.State.HPdrop;
            return false;
        }
    };

    public Action HPdrop = new Action() {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            claw.clawState = Claw.ClawState.OPEN;
            return false;
        }
    };


}