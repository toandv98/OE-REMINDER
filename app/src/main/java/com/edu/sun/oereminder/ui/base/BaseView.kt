package com.edu.sun.oereminder.ui.base

import androidx.annotation.StringRes

interface BaseView {
    fun showMessage(@StringRes resId: Int)
    fun showMessage(msg: String)
}
