package com.edu.sun.oereminder.ui.settings

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.data.repository.UserRepository

class RoomPresenter(
    private val userRepository: UserRepository,
    private val view: RoomContract.View
) : RoomContract.Presenter {

    private var currentRoom: Room? = null
    private var roomId = 0L

    override fun loadScreen() {
        userRepository.getRooms(object : SourceCallback<List<Room>> {
            override fun onSuccess(data: List<Room>) {
                data.find { it.roomId == roomId } ?: data.firstOrNull()
                    ?.let { view.displayCurrentRoom(it) }
                view.updateRecyclerView(data)
            }

            override fun onError(e: Exception) {
                view.showError(e.message.toString())
            }
        })
    }

    override fun onItemClick(room: Room) {
        currentRoom = room
        view.displayCurrentRoom(room)
    }

    override fun onSaveClick() {
        currentRoom?.let { view.updateResult(it) }
    }
}
