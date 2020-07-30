package com.edu.sun.oereminder.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.edu.sun.oereminder.utils.toast

abstract class BaseFragment<V : BaseView, T : BasePresenterImpl<V>> : Fragment(), BaseView {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.attachView(this as V)
        setupView(savedInstanceState)
        initListener()
    }

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract val presenter: T?

    abstract fun setupView(instanceState: Bundle?)

    abstract fun initListener()

    override fun showMessage(msg: String) {
        context?.toast(msg)
    }

    override fun showMessage(resId: Int) {
        context?.toast(getString(resId))
    }
}
