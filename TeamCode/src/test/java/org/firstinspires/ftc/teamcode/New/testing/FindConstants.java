package org.firstinspires.ftc.teamcode.New.testing;

import org.firstinspires.ftc.teamcode.New.PIDTuner.Constants;
import org.junit.Test;

public class FindConstants {

    @Test
    public void test() {

        Constants constants = new Constants();
        constants.sim.getPIDFConstants();

    }

}
