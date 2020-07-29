package com.edu.sun.oereminder.ui.base

interface BasePresenter<V : BaseView> {

    fun attachView(view: V)

    fun detachView()
}
