package com.edu.sun.oereminder.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.ui.settings.MemberAdapter.ViewHolder
import com.edu.sun.oereminder.ui.settings.MentionDialog.Companion.ROLE_TRAINER
import kotlinx.android.synthetic.main.item_mention.view.*

class MemberAdapter : ListAdapter<Member, ViewHolder>(MemberItemCallback()) {

    private var onItemClick: ((Member, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mention, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnclickListener(listener: (Member, Int) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(itemView: View, onItemClick: ((Member, Int) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        private var member: Member? = null

        init {
            if (onItemClick != null) itemView.setOnClickListener {
                member?.let { onItemClick(it, absoluteAdapterPosition) }
            }
        }

        fun bind(member: Member) = member.let {
            itemView.run {
                this@ViewHolder.member = it
                textName.text = it.name
                imageCheck.isVisible = it.role == ROLE_TRAINER
                Glide.with(context).load(it.avatarUrl).circleCrop().into(imageAvatar)
            }
        }
    }
}
