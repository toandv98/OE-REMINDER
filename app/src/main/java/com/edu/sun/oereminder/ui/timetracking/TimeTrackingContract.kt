package com.edu.sun.oereminder.ui.timetracking

import com.edu.sun.oereminder.ui.base.BasePresenter
import com.edu.sun.oereminder.ui.base.BaseView

interface TimeTrackingContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {

    }
}
