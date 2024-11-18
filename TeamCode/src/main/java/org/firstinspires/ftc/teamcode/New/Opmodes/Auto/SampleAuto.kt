package org.firstinspires.ftc.teamcode.New.Opmodes.Auto

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.teamcode.New.Competition.Actions.Bromine
import org.firstinspires.ftc.teamcode.New.Competition.Actions.Positions
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.Claw
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.ClawRotater
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.ColorSensor
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.ShoudlerJohn
import org.firstinspires.ftc.teamcode.New.Competition.subsystems.WristJohn
import org.firstinspires.ftc.teamcode.New.PinpointLocalizer.Localizer

class SampleAuto : LinearOpMode() {

    override fun runOpMode() {

        val localizer = Localizer(hardwareMap, Localizer.Poses(0.0, 0.0, 0.0))
        val bromine = Bromine(hardwareMap);
        waitForStart()

        runBlocking(
            ParallelAction(
                Action {
                    localizer.update()
                    false
                },
                SequentialAction(
                    bromine.prepareSpecimenDeposit,
                    Positions.BlueSpecieminBar.runToNearest,
                    bromine.fullSpecimenDeposit,

                    //grabbing the specimens and giving them to Human PLayer x3
                    bromine.prepareSampleIntake,
                    Positions.Blueleftbrick1.runToExact,
                    bromine.SampleIntake,
                    bromine. prepForHPdrop,
                    Positions.BlueHumanIntake.runToNearest,
                    bromine.Drop,
                    bromine.prepareSampleIntake,
                    Positions.Blueleftbrick2.runToExact,
                    bromine.SampleIntake,
                    bromine.prepForHPdrop,
                    Positions.BlueHumanIntake.runToNearest,
                    bromine.Drop,
                    bromine.prepareSampleIntake,
                    Positions.Blueleftbrick3.runToExact,
                    bromine.SampleIntake,
                    bromine.prepForHPdrop,
                    Positions.BlueHumanIntake.runToNearest,
                    bromine.Drop,

                    //wall intakes and deposits
                    bromine.prepareSpecimenWallIntake,
                    Positions.BlueHumanIntake.runToNearest,
                    bromine.SpecimenWallIntake,
                    Positions.BlueHumanIntake.runToNearest,
                    bromine.prepareSpecimenDeposit,
                    Positions.BlueSpecieminBar.runToNearest,
                    bromine.fullSpecimenDeposit,
                    bromine.prepareSpecimenWallIntake,
                    bromine.SpecimenWallIntake,
                    bromine.prepareSpecimenDeposit,
                    bromine.fullSpecimenDeposit,
                    bromine.prepareSpecimenWallIntake,
                    bromine.SpecimenWallIntake,
                    bromine.prepareSpecimenDeposit,
                    bromine.fullSpecimenDeposit,
                    Positions.BlueHumanIntake.runToExact,


                )
            )
        )

    }


}
