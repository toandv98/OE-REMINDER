package com.edu.sun.oereminder.ui.main

import com.edu.sun.oereminder.ui.base.BasePresenter
import com.edu.sun.oereminder.ui.base.BaseView

interface MainContract {

    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}
