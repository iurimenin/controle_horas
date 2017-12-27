package io.github.iurimenin.horastrabalhadas

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.common.ChangeEventType
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.single_day_log.view.*


/**
 * Created by Iuri Menin on 15/12/2017.
 */
class DayLogAdapter(private val mCallback : FirebaseRecyclerAdapterCallBack,
                    dayLogs : FirebaseRecyclerOptions<DayLog>) :
        FirebaseRecyclerAdapter<DayLog, DayLogAdapter.DayLogHolder>(dayLogs) {

    class DayLogHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayLogHolder {

        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.single_day_log, parent, false)
        return DayLogHolder(view)
    }

    override fun onBindViewHolder(holder: DayLogHolder, position: Int, model: DayLog) {
        holder.run {

            view.text_view_date.text = model.date
            view.text_view_morning_arrival.text = model.morningArrivalTime
            view.text_view_morning_leave.text = model.morningLeaveTime
            view.text_view_afternoon_arrival.text = model.afternoonArrivalTime
            view.text_view_afternoon_leave.text = model.afternoonLeaveTime

            view.text_view_worked_label.visibility = View.INVISIBLE
            view.text_view_worked.visibility = View.INVISIBLE

            view.text_view_estimated_leave_label.visibility = View.INVISIBLE
            view.text_view_estimated_leave.visibility = View.INVISIBLE

            when {

                model.workedTime.isNotBlank() -> {
                    view.text_view_worked_label.visibility = View.VISIBLE
                    view.text_view_worked.visibility = View.VISIBLE
                    view.text_view_worked.text = model.workedTime
                }

                model.estimatedLeaveTime.isNotBlank() -> {
                    view.text_view_estimated_leave_label.visibility = View.VISIBLE
                    view.text_view_estimated_leave.visibility = View.VISIBLE
                    view.text_view_estimated_leave.text = model.estimatedLeaveTime
                }
            }
        }
    }

    override fun onChildChanged(type: ChangeEventType, snapshot: DataSnapshot, newIndex: Int, oldIndex: Int) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        mCallback.onDataChanged(itemCount)
    }

    override fun onError(e: DatabaseError) {
        // Called when there is an error getting data. You may want to update
        // your UI to display an error message to the user.
        mCallback.onError(e.message)
    }
}