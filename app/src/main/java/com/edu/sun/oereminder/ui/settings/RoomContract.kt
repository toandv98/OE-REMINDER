package com.edu.sun.oereminder.ui.settings

import com.edu.sun.oereminder.data.model.Room

interface RoomContract {
    interface View {
        fun showError(msg: String)
        fun displayCurrentRoom(room: Room)
        fun updateRecyclerView(data: List<Room>)
        fun updateResult(room: Room)
    }

    interface Presenter {
        fun loadScreen()
        fun onItemClick(room: Room)
        fun onSaveClick()
    }
}
