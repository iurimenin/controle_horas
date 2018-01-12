package io.github.iurimenin.horastrabalhadas

import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Created by Iuri Menin on 12/01/2018.
 */
class ExtensionsTest {

    @Test
    fun deveRetonarCalendarComHoraEMinuto() {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 25)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val calendarFuture = Calendar.getInstance()
        calendarFuture.setLeaveTime("17:25")

        Assert.assertEquals(calendar.time, calendarFuture.time)
    }
}