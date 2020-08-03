package com.edu.sun.oereminder.ui.statistics

import android.os.Bundle
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.ui.base.BaseFragment
import com.edu.sun.oereminder.ui.statistics.StatisticsContract.View
import com.edu.sun.oereminder.utils.InjectorUtils

class StatisticsFragment : BaseFragment<View, StatisticsPresenter>(), View {

    override val layoutRes get() = R.layout.fragment_statistics

    override val presenter by lazy {
        StatisticsPresenter(
            InjectorUtils.getMessageRepository(requireContext()),
            InjectorUtils.getTimeSheetRepository(requireContext())
        )
    }

    override fun setupView(instanceState: Bundle?) {

    }

    override fun initListener() {

    }

}
