package com.edu.sun.oereminder.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.edu.sun.oereminder.utils.toast

abstract class BaseActivity<V : BaseView, T : BasePresenterImpl<V>> : AppCompatActivity(),
    BaseView {

    @get:StyleRes
    abstract val styleRes: Int

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract val presenter: T?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(styleRes)
        setContentView(layoutRes)
        presenter?.attachView(this as V)
        setupView(savedInstanceState)
        initListener()
    }

    abstract fun setupView(instanceState: Bundle?)

    abstract fun initListener()

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun showMessage(msg: String) {
        toast(msg)
    }

    override fun showMessage(resId: Int) {
        toast(getString(resId))
    }
}
