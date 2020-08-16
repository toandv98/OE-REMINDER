package com.edu.sun.oereminder.ui.settings

import com.edu.sun.oereminder.data.model.Member

interface MentionContract {
    interface View {
        fun updateRecyclerView(data: List<Member>, index: Int? = null)
        fun updateResult(results: List<Member>)
    }

    interface Presenter {
        fun loadScreen()
        fun onItemClick(member: Member, index: Int)
        fun onSaveClick()
    }
}
