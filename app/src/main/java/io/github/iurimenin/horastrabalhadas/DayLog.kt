package io.github.iurimenin.horastrabalhadas

import android.content.Context
import android.util.Log
import br.com.softfocus.dateutils.DateUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Iuri Menin on 15/12/2017.
 */
data class DayLog(var date: String,
                  var morningArrivalTime: String,
                  var morningLeaveTime: String,
                  var afternoonArrivalTime: String,
                  var afternoonLeaveTime: String,
                  var estimatedLeaveTime: String,
                  var workedTime : String,
                  var workedTimeMilis : Long) {

    //Firebase needs this
    constructor() : this("01/01/1070",
            "",
            "",
            "",
            "",
            "",
            "",
            0)

    constructor(date: String, morningArrivalTime: String) : this(date,
            morningArrivalTime,
            "",
            "",
            "",
            "",
            "",
            0)

    companion object {

        fun logNow(context: Context){

            val dateNow = Calendar.getInstance().time
            val ref = FirebaseUtils.instance.dayLogReference()

            ref.child(dateNow.dateStringFirebase)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot?) {

                    if (snapshot?.hasChildren() == true) {

                        val dayLog = snapshot.getValue(DayLog::class.java)

                        dayLog?.let {
                            when {
                                it.morningArrivalTime.isBlank() -> {
                                    dayLog.morningArrivalTime =
                                            DateUtils.format(dateNow, "HH:mm")
                                    ref.child(dateNow.dateStringFirebase).setValue(dayLog)
                                }
                                it.morningLeaveTime.isBlank() -> {
                                    dayLog.morningLeaveTime = DateUtils.format(dateNow, "HH:mm")
                                    dayLog.calculateWorkedTime()
                                    ref.child(dateNow.dateStringFirebase).setValue(dayLog)
                                }
                                it.afternoonArrivalTime.isBlank() -> {
                                    dayLog.afternoonArrivalTime =
                                            DateUtils.format(dateNow, "HH:mm")
                                    dayLog.calculateLeaveTime()
                                    ref.child(dateNow.dateStringFirebase).setValue(dayLog)
                                    NotificationUtils
                                            .Companion
                                            .notifyAfternoonLeaveTime(dayLog, context)
                                }
                                it.afternoonLeaveTime.isBlank() -> {
                                    dayLog.afternoonLeaveTime =
                                            DateUtils.format(dateNow, "HH:mm")
                                    dayLog.calculateWorkedTime()
                                    ref.child(dateNow.dateStringFirebase).setValue(dayLog)
                                    NotificationUtils
                                            .Companion
                                            .notifyWorkedTime(dayLog, context)

                                }
                                else -> {
                                    Log.d("DayLog", "Else do when")
                                }
                            }
                        }
                    } else {

                        val dayLog = DayLog(
                                DateUtils.format(dateNow, DateUtils.DayMonthYearFormat),
                                DateUtils.format(dateNow, "HH:mm"))

                        ref.child(dateNow.dateStringFirebase).setValue(dayLog)
                    }
                }
                override fun onCancelled(error: DatabaseError?) {
                    Log.e("DayLog", "onCancelled", error?.toException()?.cause)
                }
            })
            
        }
    }

    fun calculateLeaveTime() {

        val calendarMorningArrival = Calendar.getInstance()
        calendarMorningArrival.time = DateUtils
                .convertStringForDate(morningArrivalTime, "HH:mm")

        val calendarMorningLeave = Calendar.getInstance()
        calendarMorningLeave.time = DateUtils
                .convertStringForDate(morningLeaveTime, "HH:mm")

        val calendarAfternoonArrival = Calendar.getInstance()
        calendarAfternoonArrival.time = DateUtils
                .convertStringForDate(afternoonArrivalTime, "HH:mm")

        val morningTime =  calendarMorningLeave.time.time - calendarMorningArrival.time.time
        val millisecondToFinish = (28800000 - morningTime)
        val secondsToFinish = millisecondToFinish / 1000

        val calendarAfternoonLeave = Calendar.getInstance()
        calendarAfternoonLeave.time = calendarAfternoonArrival.time
        calendarAfternoonLeave.add(Calendar.SECOND, secondsToFinish.toInt())

        estimatedLeaveTime = DateUtils.format(calendarAfternoonLeave.time, "HH:mm")
        workedTime = ""
    }

    fun calculateWorkedTime() {

        val calendarMorningArrival = Calendar.getInstance()
        calendarMorningArrival.time = DateUtils
                .convertStringForDate(morningArrivalTime, "HH:mm")

        val calendarMorningLeave = Calendar.getInstance()
        calendarMorningLeave.time = DateUtils
                .convertStringForDate(morningLeaveTime, "HH:mm")

        var afternoonTime = 0L
        if (afternoonArrivalTime.isNotBlank() && afternoonLeaveTime.isNotBlank()) {
            val calendarAfternoonArrival = Calendar.getInstance()
            calendarAfternoonArrival.time = DateUtils
                    .convertStringForDate(afternoonArrivalTime, "HH:mm")

            val calendarAfternoonLeave = Calendar.getInstance()
            calendarAfternoonLeave.time = DateUtils
                    .convertStringForDate(afternoonLeaveTime, "HH:mm")

            afternoonTime = calendarAfternoonLeave.time.time - calendarAfternoonArrival.time.time
        }

        val morningTime = calendarMorningLeave.time.time - calendarMorningArrival.time.time
        var totalTime = morningTime + afternoonTime

        workedTimeMilis = totalTime

        val hours = TimeUnit.MILLISECONDS.toHours(totalTime)
        totalTime -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime)
        totalTime -= TimeUnit.MINUTES.toMillis(minutes)

        workedTime = String.format("%02d:%02d", hours, minutes)
    }
}