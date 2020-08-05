package com.edu.sun.oereminder.ui.timetracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.ui.timetracking.TimeRecordAdapter.ViewHolder
import com.edu.sun.oereminder.utils.toDate
import com.edu.sun.oereminder.utils.toTime
import kotlinx.android.synthetic.main.item_time_record.view.*

class TimeRecordAdapter : ListAdapter<TimeRecord, ViewHolder>(TimeRecordItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(timeRecord: TimeRecord) {
            with(itemView) {
                with(timeRecord) {
                    textTimeIn.text = timeInCalendar.toTime()
                    textTimeOut.text = timeOutCalendar.toTime()
                    textDate.text = dateCalendar.toDate()
                    textStatus.text = if (status.isBlank()) {
                        context.getString(R.string.status_pending)
                    } else status
                    textPartOfDay.text = partOfDay
                }
            }
        }
    }

}

