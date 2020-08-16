package com.edu.sun.oereminder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.bumptech.glide.Glide
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.utils.InjectorUtils
import com.edu.sun.oereminder.utils.toast
import kotlinx.android.synthetic.main.dialog_pick_room.*
import kotlinx.android.synthetic.main.toolbar_app.*

class ChatRoomDialog : DialogFragment(), RoomContract.View {

    private val adapter by lazy { RoomAdapter() }
    private val presenter by lazy {
        RoomPresenter(InjectorUtils.getUserRepository(requireContext()), this)
    }

    override fun getTheme(): Int = R.style.MessageDialogStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_pick_room, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initListener()
    }

    private fun setupView() {
        recyclerRoom.adapter = adapter
        toolbar.run {
            setTitle(R.string.title_room)
            setNavigationIcon(R.drawable.ic_round_arrow_back)
            inflateMenu(R.menu.menu_chat_room)
        }
        presenter.loadScreen()
    }

    private fun initListener() {
        adapter.setOnclickListener {
            presenter.onItemClick(it)
        }
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.item_save) {
                presenter.onSaveClick()
                true
            } else {
                context?.toast(getString(R.string.summary_room))
                false
            }
        }
    }

    override fun showError(msg: String) {
        context?.toast(msg)
    }

    override fun displayCurrentRoom(room: Room) {
        textRoomName.run {
            text = room.name
            visibility = View.VISIBLE
        }
        context?.run { Glide.with(this).load(room.iconPath).circleCrop().into(imageRoom) }
        layoutRoomImage.visibility = View.VISIBLE
    }

    override fun updateRecyclerView(data: List<Room>) {
        adapter.submitList(data)
        progressLoad.isVisible = false
    }

    override fun updateResult(room: Room) {
        setFragmentResult(REQUEST_ROOM, bundleOf(RESULT_ROOM_ID to room.roomId))
        dismiss()
    }

    companion object {
        const val REQUEST_ROOM = "request_room"
        const val RESULT_ROOM_ID = "room_id"

        fun newInstance(roomId: Long) = ChatRoomDialog().apply {
            arguments = bundleOf(RESULT_ROOM_ID to roomId)
        }
    }
}
