package io.github.iurimenin.horastrabalhadas

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Created by Iuri Menin on 18/12/2017.
 */
class DayLogTest {

    @Test
    fun mustReturn17_46ToCalculateAndSaveLeaveTime() {

        val dayLog = DayLog("11/12/2017",
                "08:43",
                "12:06",
                "12:50",
                "",
                "",
                "",
                0L)

        dayLog.calculateLeaveTime()

        assertEquals("17:27", dayLog.estimatedLeaveTime)
    }

    @Test
    fun mustReturn17_11ToCalculateAndSaveLeaveTime() {

        val dayLog = DayLog("11/12/2017",
                "08:20",
                "12:01",
                "12:44",
                "",
                "",
                "",
                0L)

        dayLog.calculateLeaveTime()

        assertEquals("17:03", dayLog.estimatedLeaveTime)
    }

    @Test
    fun mustReturn17_35ToCalculateAndSaveLeaveTime() {

        val dayLog = DayLog("16/12/2017",
                "08:43",
                "12:03",
                "12:55",
                "",
                "",
                "",
                0L)

        dayLog.calculateLeaveTime()

        assertEquals("17:35", dayLog.estimatedLeaveTime)
    }

    @Test
    fun mustReturn08_19TocalculateWorkedTime() {

        val dayLog = DayLog("11/12/2017",
                "08:43",
                "12:06",
                "12:50",
                "17:46",
                "",
                "",
                0L)

        dayLog.calculateWorkedTime()

        assertEquals("08:19", dayLog.workedTime)
    }

    @Test
    fun mustReturn08_08ToCalculateAndSaveLeaveTime() {

        val dayLog = DayLog("11/12/2017",
                "08:20",
                "12:01",
                "12:44",
                "17:11",
                "",
                "",
                0L)

        dayLog.calculateWorkedTime()

        assertEquals("08:08", dayLog.workedTime)
    }

    @Test
    fun mustReturn08_00TocalculateWorkedTime() {

        val dayLog = DayLog("16/12/2017",
                "08:43",
                "12:03",
                "12:55",
                "17:35",
                "",
                "",
                0L)

        dayLog.calculateWorkedTime()

        assertEquals("08:00", dayLog.workedTime)
    }

}