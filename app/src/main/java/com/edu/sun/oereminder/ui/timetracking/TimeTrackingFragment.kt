package com.edu.sun.oereminder.ui.timetracking

import android.os.Bundle
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.ui.base.BaseFragment
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.View
import com.edu.sun.oereminder.utils.InjectorUtils

class TimeTrackingFragment : BaseFragment<View, TimeTrackingPresenter>(), View {

    override val layoutRes get() = R.layout.fragment_time_tracking

    override val presenter by lazy {
        TimeTrackingPresenter(
            InjectorUtils.getTimeSheetRepository(requireContext())
        )
    }

    override fun setupView(instanceState: Bundle?) {

    }

    override fun initListener() {

    }
}
