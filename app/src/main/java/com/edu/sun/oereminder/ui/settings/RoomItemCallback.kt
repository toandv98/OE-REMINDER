package com.edu.sun.oereminder.ui.settings

import androidx.recyclerview.widget.DiffUtil
import com.edu.sun.oereminder.data.model.Room

class RoomItemCallback : DiffUtil.ItemCallback<Room>() {
    override fun areItemsTheSame(oldItem: Room, newItem: Room) =
        oldItem.roomId == newItem.roomId

    override fun areContentsTheSame(oldItem: Room, newItem: Room) =
        oldItem == newItem
}
