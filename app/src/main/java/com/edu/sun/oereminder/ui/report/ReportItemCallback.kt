package com.edu.sun.oereminder.ui.report

import androidx.recyclerview.widget.DiffUtil
import com.edu.sun.oereminder.data.model.Message

class ReportItemCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message) =
        oldItem.messageId == newItem.messageId

    override fun areContentsTheSame(oldItem: Message, newItem: Message) =
        oldItem == newItem
}
