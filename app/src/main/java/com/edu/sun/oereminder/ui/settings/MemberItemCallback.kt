package com.edu.sun.oereminder.ui.settings

import androidx.recyclerview.widget.DiffUtil
import com.edu.sun.oereminder.data.model.Member

class MemberItemCallback : DiffUtil.ItemCallback<Member>() {
    override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
        oldItem.accountId == newItem.accountId

    override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean =
        oldItem == newItem
}
