package com.edu.sun.oereminder.ui.report

import com.edu.sun.oereminder.ui.base.BasePresenter
import com.edu.sun.oereminder.ui.base.BaseView

interface ReportContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}
