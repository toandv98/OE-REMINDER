package com.edu.sun.oereminder.ui.timetracking

import androidx.recyclerview.widget.DiffUtil
import com.edu.sun.oereminder.data.model.TimeRecord

class TimeRecordItemCallBack : DiffUtil.ItemCallback<TimeRecord>() {
    override fun areItemsTheSame(oldItem: TimeRecord, newItem: TimeRecord) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TimeRecord, newItem: TimeRecord) =
        oldItem == newItem
}
