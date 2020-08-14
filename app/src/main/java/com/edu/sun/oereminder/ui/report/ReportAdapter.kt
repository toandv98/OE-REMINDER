package com.edu.sun.oereminder.ui.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.ui.report.ReportAdapter.ViewHolder
import com.edu.sun.oereminder.utils.toDate
import com.edu.sun.oereminder.utils.toTime
import kotlinx.android.synthetic.main.item_daily_report.view.*

class ReportAdapter : ListAdapter<Message, ViewHolder>(ReportItemCallback()) {

    private var onItemClick: ((Message) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_report, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItemClickListener(listener: (Message) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(itemView: View, onItemClick: ((Message) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {

        private var msg: Message? = null

        init {
            if (onItemClick != null) itemView.setOnClickListener { msg?.let { onItemClick(it) } }
        }

        fun bind(message: Message) = itemView.run {
            with(message) {
                msg = this
                textDate.text = sendCalendar.toDate()
                textSendTime.text =
                    if (updateTime == 0L) sendCalendar.toTime() else updateCalendar.toTime()
                textMessage.text = messageTitle
                textReport.text = HtmlCompat.fromHtml(messageReport, FROM_HTML_MODE_LEGACY)
            }
        }
    }
}
