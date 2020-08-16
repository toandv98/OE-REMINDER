package com.edu.sun.oereminder.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.ui.settings.RoomAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_simple.view.*

class RoomAdapter : ListAdapter<Room, ViewHolder>(RoomItemCallback()) {

    private var onItemClick: ((Room) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_simple, parent, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnclickListener(listener: (Room) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(itemView: View, onItemClick: ((Room) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        private var chatRoom: Room? = null

        init {
            if (onItemClick != null) itemView.setOnClickListener { chatRoom?.let { onItemClick(it) } }
        }

        fun bind(room: Room) = room.let {
            itemView.run {
                chatRoom = it
                textName.text = it.name
                Glide.with(context).load(it.iconPath).circleCrop().into(imageAvatar)
            }
        }
    }
}

