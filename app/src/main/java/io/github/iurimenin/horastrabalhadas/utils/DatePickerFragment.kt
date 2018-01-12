package io.github.iurimenin.horastrabalhadas.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.widget.DatePicker
import io.github.iurimenin.horastrabalhadas.R
import java.util.*


/**
 * Created by Iuri Menin on 29/12/2017.
 */
class DatePickerFragment : DialogFragment(),
        DatePickerDialog.OnDateSetListener {

    val mCalendar = Calendar.getInstance()
    lateinit var mListener: OnConfirmDatePicker

    fun setOnDateSetListener(listener : OnConfirmDatePicker) {
        mListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(activity!!,
                R.style.AppTheme_DialogTheme,
                this, mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun onPause() {

        try {
            this.dismiss()
        } catch (ignored: Exception) { }

        super.onPause()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        mListener.onConfirmDateSelection(calendar.time)
    }

    fun setDate(date: Date) {
        mCalendar.time = date
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, "datePicker")
    }

    interface OnConfirmDatePicker {
        fun onConfirmDateSelection(date: Date)
    }
}