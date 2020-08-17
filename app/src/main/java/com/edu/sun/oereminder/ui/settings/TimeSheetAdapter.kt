package com.edu.sun.oereminder.ui.settings

import android.graphics.Color
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.ui.settings.TimeSheetAdapter.ViewHolder
import com.edu.sun.oereminder.ui.timetracking.TimeRecordItemCallback
import com.edu.sun.oereminder.utils.WorkTime.AFTERNOON
import com.edu.sun.oereminder.utils.WorkTime.DATE_PATTERN
import com.edu.sun.oereminder.utils.WorkTime.MORNING
import com.edu.sun.oereminder.utils.WorkTime.OFF
import com.edu.sun.oereminder.utils.WorkTime.WHOLE_DAY
import com.edu.sun.oereminder.utils.toDate
import kotlinx.android.synthetic.main.item_time_picker.view.*
import java.util.*

class TimeSheetAdapter : ListAdapter<TimeRecord, ViewHolder>(TimeRecordItemCallback()) {

    private var onChecked: ((TimeRecord, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_time_picker, parent, false),
            onChecked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnCheckedListener(listener: ((TimeRecord, Int) -> Unit)) {
        onChecked = listener
    }

    class ViewHolder(itemView: View, onChecked: ((TimeRecord, Int) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {

        private var record: TimeRecord? = null

        init {
            if (onChecked != null) {
                itemView.chipGroupDate.setOnCheckedChangeListener { _, checkedId ->
                    record?.run {
                        partOfDay = when (checkedId) {
                            R.id.chipA -> AFTERNOON
                            R.id.chipM -> MORNING
                            R.id.chipW -> WHOLE_DAY
                            else -> OFF
                        }
                        onChecked(this, adapterPosition)
                    }
                }
            }
        }

        fun bind(timeRecord: TimeRecord) = with(timeRecord) {
            record = this
            itemView.run {
                textDate.text = dateCalendar.toDate(DATE_PATTERN)
                if (dateCalendar.before(GregorianCalendar())) disable() else disable(false)
                chipGroupDate.check(
                    when (partOfDay) {
                        WHOLE_DAY -> R.id.chipW
                        MORNING -> R.id.chipM
                        AFTERNOON -> R.id.chipA
                        else -> View.NO_ID
                    }
                )
            }
        }

        private fun View.disable(isDisable: Boolean = true) {
            textDate.background.colorFilter =
                if (isDisable) PorterDuffColorFilter(Color.GRAY, SRC_ATOP) else null
            chipA.isEnabled = !isDisable
            chipM.isEnabled = !isDisable
            chipW.isEnabled = !isDisable
        }
    }
}
