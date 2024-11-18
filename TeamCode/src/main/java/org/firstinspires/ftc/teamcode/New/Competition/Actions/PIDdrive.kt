package org.firstinspires.ftc.teamcode.New.Competition.Actions

import CommonUtilities.AngleRange
import android.util.Log
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.Vector2d
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.New.Angle
import org.firstinspires.ftc.teamcode.New.Controller
import org.firstinspires.ftc.teamcode.New.FindNearestPoint.findNearestPoint
import org.firstinspires.ftc.teamcode.New.Opmodes.Auto.PIDTunerDrive
import org.firstinspires.ftc.teamcode.New.PIDParams
import org.firstinspires.ftc.teamcode.New.PinpointLocalizer.Localizer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

//todo bring in PIDF class

class PIDdrive(hwMap: HardwareMap) {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: PIDdrive
    }

    //constants
    val Xpid = Controller(PIDParams(0.0, 0.0, 0.0, 0.0))
    val Ypid = Controller(PIDParams(0.0, 0.0, 0.0, 0.0))
    val Rpid = Controller(PIDParams(0.0, 0.0, 0.0, 0.0))

    val rightBack: DcMotor = hwMap.get(DcMotorEx::class.java, "rightBack")
    val leftFront: DcMotor = hwMap.get(DcMotorEx::class.java, "leftFront")
    val rightFront: DcMotor = hwMap.get(DcMotorEx::class.java, "rightFront")
    val leftBack: DcMotor = hwMap.get(DcMotorEx::class.java, "leftBack")

    fun setPID(p: DoubleArray, i: DoubleArray, d: DoubleArray) {
        Log.d("PID", "setting PID")
        val controllers = listOf(Xpid, Ypid, Rpid)
        controllers.forEachIndexed { index, controller ->
            controller.params = PIDParams(p[index], i[index], d[index], 0.0)
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


}

/**
 * Runs to the target provided, based on the robots extension
 */
class RunToNearest(private val targetVector: Vector2d) : Action {
    override fun run(p: TelemetryPacket): Boolean {
        val motors = PIDdrive.instance

        val currentVector = Localizer.pose
        val heading =  Localizer.pose.heading
        val target = findNearestPoint(targetVector, Vector2d(currentVector.x, currentVector.y))

        val axialError = target.y - currentVector.y
        val lateralError = target.x - currentVector.x
        val headingError = Angle.wrap(target.heading - heading)

        val axial = motors.Ypid.calculate(axialError)
        val lateral = motors.Xpid.calculate(lateralError)
        val turn = motors.Rpid.calculate(headingError)

        val rotX = axial * cos(heading) - lateral * sin(heading)
        val rotY = axial * sin(heading) + lateral * cos(heading)


        motors.leftFront.power = (rotY - rotX + turn)
        motors.leftBack.power = (rotY + rotX - turn)
        motors.rightFront.power = (rotY + rotX + turn)
        motors.rightBack.power = (rotY - rotX - turn)


        return arrayListOf(axialError, lateralError).all { abs(it) <= 2.0 } &&
                abs(headingError) <= Math.toRadians(11.0)
    }



}