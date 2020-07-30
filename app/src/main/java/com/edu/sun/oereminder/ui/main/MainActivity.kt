package com.edu.sun.oereminder.ui.main

import android.os.Bundle
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.ui.base.BaseActivity
import com.edu.sun.oereminder.ui.main.MainContract.View

class MainActivity : BaseActivity<View, MainPresenter>(), View {

    override val styleRes get() = R.style.AppTheme

    override val layoutRes get() = R.layout.activity_main

    override val presenter = MainPresenter()

    override fun setupView(instanceState: Bundle?) {

    }

    override fun initListener() {

    }

}
