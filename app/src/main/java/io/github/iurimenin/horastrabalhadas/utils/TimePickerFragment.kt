package io.github.iurimenin.horastrabalhadas.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.format.DateFormat
import android.widget.TimePicker
import br.com.softfocus.dateutils.DateUtils
import java.util.*


/**
 * Created by Iuri Menin on 29/12/2017.
 */
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    val mCalendar = Calendar.getInstance()
    lateinit var mListener: OnConfirmTimePicker

    fun setOnTimeSetListener(listener : OnConfirmTimePicker) {
        mListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker

        val hour = mCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = mCalendar.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute,
                DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        mListener.onConfirmTimeSelection(calendar)
    }

    override fun onPause() {

        try {
            this.dismiss()
        } catch (ignored: Exception) { }

        super.onPause()
    }

    fun setTime(time: String) {
        mCalendar.time = DateUtils.convertStringForDate(time, "HH:mm")
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, "datePicker")
    }

    interface OnConfirmTimePicker {
        fun onConfirmTimeSelection(time: Calendar)
    }

}