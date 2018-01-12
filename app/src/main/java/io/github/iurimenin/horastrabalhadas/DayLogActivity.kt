package io.github.iurimenin.horastrabalhadas

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.com.softfocus.dateutils.DateUtils
import io.github.iurimenin.horastrabalhadas.utils.DatePickerFragment
import io.github.iurimenin.horastrabalhadas.utils.TimePickerFragment
import kotlinx.android.synthetic.main.activity_day_log.*
import java.util.*

/**
 * Created by Iuri Menin on 29/12/2017.
 */
class DayLogActivity : AppCompatActivity() {

    lateinit var mDayLog: DayLog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_log)

        mDayLog = if (intent != null &&
                intent.extras != null &&
                intent.extras[DayLog::class.java.simpleName] != null) {
            intent.extras[DayLog::class.java.simpleName] as DayLog
        } else {
            DayLog()
        }

        this.evaluateTexts()

        image_button_datepicker.setOnClickListener {
            selectDate()
        }

        constraintLayout_date.setOnClickListener {
            selectDate()
        }

        image_button_timepicker_morning_arrival.setOnClickListener {
            selectMorningArrival()
        }

        constraintLayout_morning.setOnClickListener{
            selectMorningArrival()
        }

        image_button_timepicker_morning_leave.setOnClickListener {
            selectMorningLeave()
        }

        constraintLayout_morning_leave.setOnClickListener {
            selectMorningLeave()
        }

        image_button_timepicker_afternoon_arrival.setOnClickListener {
            selectAfternoonArrival()
        }

        constraintLayout_afternoon.setOnClickListener{
            selectAfternoonArrival()
        }

        image_button_timepicker_afternoon_leave.setOnClickListener {
            selectAfternoonLeave()
        }

        constraintLayout_afternoon_leave.setOnClickListener{
            selectAfternoonLeave()
        }
    }

    private fun evaluateTexts() {
        text_view_date.text = "..."
        text_view_morning_arrival.text = "..."
        text_view_morning_leave.text = "..."
        text_view_afternoon_arrival.text = "..."
        text_view_afternoon_leave.text = "..."

        if (mDayLog.date.isNotBlank()) {
            text_view_date.text = mDayLog.date
        }

        if (mDayLog.morningArrivalTime.isNotBlank()) {
            text_view_morning_arrival.text = mDayLog.morningArrivalTime
        }

        if (mDayLog.morningLeaveTime.isNotBlank()) {
            text_view_morning_leave.text = mDayLog.morningLeaveTime
        }

        if (mDayLog.afternoonArrivalTime.isNotBlank()) {
            text_view_afternoon_arrival.text = mDayLog.afternoonArrivalTime
        }

        if (mDayLog.afternoonLeaveTime.isNotBlank()) {
            text_view_afternoon_leave.text = mDayLog.afternoonLeaveTime
        }
    }

    private fun selectAfternoonLeave() {
        val timePickerFragment = TimePickerFragment()

        if(mDayLog.afternoonLeaveTime.isNotBlank())
            timePickerFragment.setTime(mDayLog.afternoonLeaveTime)

        timePickerFragment.show(supportFragmentManager)
        timePickerFragment
                .setOnTimeSetListener(object : TimePickerFragment.OnConfirmTimePicker{

                    override fun onConfirmTimeSelection(time: Calendar) {
                        val timeS = DateUtils.format(time.time, "HH:mm")
                        text_view_afternoon_leave.text = timeS
                        mDayLog.afternoonLeaveTime = timeS
                    }
                })
    }

    private fun selectAfternoonArrival() {
        val timePickerFragment = TimePickerFragment()

        if(mDayLog.afternoonArrivalTime.isNotBlank())
            timePickerFragment.setTime(mDayLog.afternoonArrivalTime)

        timePickerFragment.show(supportFragmentManager)
        timePickerFragment
                .setOnTimeSetListener(object : TimePickerFragment.OnConfirmTimePicker{

                    override fun onConfirmTimeSelection(time: Calendar) {
                        val timeS = DateUtils.format(time.time, "HH:mm")
                        text_view_afternoon_arrival.text = timeS
                        mDayLog.afternoonArrivalTime = timeS
                    }
                })
    }

    private fun selectMorningLeave() {
        val timePickerFragment = TimePickerFragment()

        if(mDayLog.morningLeaveTime.isNotBlank())
            timePickerFragment.setTime(mDayLog.morningLeaveTime)

        timePickerFragment.show(supportFragmentManager)
        timePickerFragment
                .setOnTimeSetListener(object : TimePickerFragment.OnConfirmTimePicker{

                    override fun onConfirmTimeSelection(time: Calendar) {
                        val timeS = DateUtils.format(time.time, "HH:mm")
                        text_view_morning_leave.text = timeS
                        mDayLog.morningLeaveTime = timeS
                    }
                })
    }

    private fun selectMorningArrival() {
        val timePickerFragment = TimePickerFragment()

        if(mDayLog.morningArrivalTime.isNotBlank())
            timePickerFragment.setTime(mDayLog.morningArrivalTime)

        timePickerFragment.show(supportFragmentManager)
        timePickerFragment
                .setOnTimeSetListener(object : TimePickerFragment.OnConfirmTimePicker {

                    override fun onConfirmTimeSelection(time: Calendar) {
                        val timeS = DateUtils.format(time.time, "HH:mm")
                        text_view_morning_arrival.text = timeS
                        mDayLog.morningArrivalTime = timeS
                    }
                })
    }

    private fun selectDate() {
        val datePickerFragment = DatePickerFragment()

        if(mDayLog.date.isNotBlank())
            datePickerFragment.setDate(DateUtils.convertStringForDate(mDayLog.date, DateUtils.DayMonthYearFormat))

        datePickerFragment.show(supportFragmentManager)
        datePickerFragment
                .setOnDateSetListener(object : DatePickerFragment.OnConfirmDatePicker {

                    override fun onConfirmDateSelection(date: Date) {
                        val dateS = DateUtils.format(date, DateUtils.DayMonthYearFormat)
                        text_view_date.text = dateS
                        mDayLog.date = dateS
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.daylog_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        item?.let {
            return when (it.itemId) {
                R.id.menu_item_save -> {
                    mDayLog.save()
                    finish()
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
        return true
    }
}
