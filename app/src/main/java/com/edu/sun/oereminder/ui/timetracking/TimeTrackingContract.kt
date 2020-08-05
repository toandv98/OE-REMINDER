package com.edu.sun.oereminder.ui.timetracking

import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.ui.base.BasePresenter
import com.edu.sun.oereminder.ui.base.BaseView

interface TimeTrackingContract {
    interface View : BaseView {

        fun showOrHideFab(isShow: Boolean)

        fun updateRecyclerView(timeRecords: List<TimeRecord>)

        fun navigateToCheckIn()
    }

    interface Presenter : BasePresenter<View> {

        fun load()

        fun onDateSelected(from: Long, to: Long)
    }
}
