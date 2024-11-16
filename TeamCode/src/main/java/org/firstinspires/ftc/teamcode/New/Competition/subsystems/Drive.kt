package org.firstinspires.ftc.teamcode.New.Competition.subsystems

import android.util.Log
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.New.PinpointLocalizer.Localizer
import org.firstinspires.ftc.teamcode.New.SmoothInput
import org.firstinspires.ftc.teamcode.New.SubSystems
import kotlin.math.cos
import kotlin.math.sin

//todo Make Java version
class Drive(hwMap: HardwareMap) : SubSystems {
    enum class States {
        Manual, Auto
    }

    override var state = States.Manual

    private val rightBack: DcMotor = hwMap.get(DcMotor::class.java, "rightBack")
    private val leftFront: DcMotor = hwMap.get(DcMotor::class.java, "leftFront")
    private val rightFront: DcMotor = hwMap.get(DcMotor::class.java, "rightFront")
    private val leftBack: DcMotor = hwMap.get(DcMotor::class.java, "leftBack")

    override fun update(gamepadInput: ArrayList<Float>) {

        when (state) {
            States.Auto -> {
                //leave empty
            }

            States.Manual -> {
                driveManual(gamepadInput)
            }
        }
    }

    init {
        leftBack.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftFront.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightBack.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightFront.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        leftBack.direction = DcMotorSimple.Direction.FORWARD
        leftFront.direction = DcMotorSimple.Direction.REVERSE
        rightFront.direction = DcMotorSimple.Direction.REVERSE
        rightBack.direction = DcMotorSimple.Direction.FORWARD

    }

    private fun driveManual(gamepadInput: ArrayList<Float>) {
        val input = gamepadInput.map{SmoothInput.gamepadStick(it.toDouble())}
        Log.d("f", input.toString())
        val (lateral, axial, turn) = input

        val h = Localizer.pose.heading
        val rotX = axial * cos(h) - lateral * sin(h)
        val rotY = axial * sin(h) + lateral * cos(h)

        leftFront.power = (rotY - rotX + turn)
        leftBack.power = (rotY + rotX - turn)
        rightFront.power = (rotY + rotX + turn)
        rightBack.power = (rotY - rotX - turn)
    }

}