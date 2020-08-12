package com.edu.sun.oereminder.ui.report

import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.ui.base.BasePresenter
import com.edu.sun.oereminder.ui.base.BaseView

interface ReportContract {
    interface View : BaseView {
        fun updateRecyclerView(messages: List<Message>)
        fun navigateToMessage(isPlan: Boolean, isEdit: Boolean = false)
        fun showFab(isShow: Boolean, isPlan: Boolean = true)
        fun showProgress(isShow: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun loadScreen()
        fun onFabClick()
        fun onItemClick()
        fun onQueryChanged(text: String)
    }
}
