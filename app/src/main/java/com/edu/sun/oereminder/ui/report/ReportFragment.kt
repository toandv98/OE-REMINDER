package com.edu.sun.oereminder.ui.report

import android.os.Bundle
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.ui.base.BaseFragment
import com.edu.sun.oereminder.ui.report.ReportContract.View
import com.edu.sun.oereminder.utils.InjectorUtils

class ReportFragment : BaseFragment<View, ReportPresenter>(), View {

    override val layoutRes get() = R.layout.fragment_report

    override val presenter by lazy {
        ReportPresenter(
            InjectorUtils.getMessageRepository(requireContext())
        )
    }

    override fun setupView(instanceState: Bundle?) {

    }

    override fun initListener() {

    }

}
