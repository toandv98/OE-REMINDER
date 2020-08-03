package com.edu.sun.oereminder.ui.main

import android.os.Bundle
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.source.preferences.PreferencesHelperImpl
import com.edu.sun.oereminder.ui.base.BaseActivity
import com.edu.sun.oereminder.ui.main.MainContract.View
import com.edu.sun.oereminder.ui.report.ReportFragment
import com.edu.sun.oereminder.ui.settings.SettingsFragment
import com.edu.sun.oereminder.ui.statistics.StatisticsFragment
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingFragment
import com.edu.sun.oereminder.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<View, MainPresenter>(), View {

    override val styleRes get() = R.style.AppTheme

    override val layoutRes get() = R.layout.activity_main

    override val presenter by lazy { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferencesHelperImpl.getInstance(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupView(instanceState: Bundle?) {
        if (instanceState == null) {
            replaceFragment(TimeTrackingFragment(), R.id.main_container)
        }
    }

    override fun initListener() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_time_tracking -> {
                    replaceFragment(TimeTrackingFragment(), R.id.main_container)
                    true
                }
                R.id.item_plan_report -> {
                    replaceFragment(ReportFragment(), R.id.main_container)
                    true
                }
                R.id.item_statistics -> {
                    replaceFragment(StatisticsFragment(), R.id.main_container)
                    true
                }
                R.id.item_settings -> {
                    replaceFragment(SettingsFragment(), R.id.main_container)
                    true
                }
                else -> false
            }
        }
    }
}
