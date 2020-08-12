package com.edu.sun.oereminder.ui.timetracking

import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.ui.base.BasePresenter
import com.edu.sun.oereminder.ui.base.BaseView

interface TimeTrackingContract {
    interface View : BaseView {

        fun updateFab(isShow: Boolean, isCheckIn: Boolean = true)

        fun updateRecyclerView(timeRecords: List<TimeRecord>)

        fun updateRecyclerView(firstItem: TimeRecord)

        fun navigateToCheckIn(isCheckIn: Boolean)

        fun navigateToSendReport(isPlan: Boolean)
    }

    interface Presenter : BasePresenter<View> {

        fun loadScreen()

        fun onFabClick()

        fun selectedDate(from: Long, to: Long)

        fun onCheckResult(code: Int)
    }
}
