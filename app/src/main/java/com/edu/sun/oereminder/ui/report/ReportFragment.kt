package com.edu.sun.oereminder.ui.report

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.ui.base.BaseFragment
import com.edu.sun.oereminder.ui.report.ReportContract.View
import com.edu.sun.oereminder.utils.InjectorUtils
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.android.synthetic.main.toolbar_app.*

class ReportFragment : BaseFragment<View, ReportPresenter>(), View {

    private var searchView: SearchView? = null
    private val adapter by lazy { ReportAdapter() }

    override val layoutRes get() = R.layout.fragment_report

    override val presenter by lazy {
        ReportPresenter(InjectorUtils.getMessageRepository(requireContext()))
    }

    override fun setupView(instanceState: Bundle?) {
        toolbar.run {
            setTitle(R.string.title_item_plan_report)
            inflateMenu(R.menu.menu_report)
            searchView = menu.findItem(R.id.item_search_report).actionView as SearchView
            searchView?.queryHint = getString(R.string.hint_search_report)
        }
        presenter.loadScreen()
        recyclerReport.adapter = adapter
    }

    override fun initListener() {
        fabDailyPlan.setOnClickListener { presenter.onFabClick() }
        adapter.setItemClickListener { presenter.onItemClick() }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { presenter.onQueryChanged(it) }
                return false
            }
        })
    }

    override fun updateRecyclerView(messages: List<Message>) {
        adapter.submitList(messages)
    }

    override fun navigateToMessage(isPlan: Boolean, isEdit: Boolean) {

    }

    override fun showFab(isShow: Boolean, isPlan: Boolean) {
        fabDailyPlan.run {
            isVisible = isShow
            setText(if (isPlan) R.string.text_daily_plan else R.string.text_daily_report)
        }
    }

    override fun showProgress(isShow: Boolean) {
        progressLoad.isVisible = isShow
    }
}
