package com.edu.sun.oereminder.ui.timetracking

import android.os.Bundle
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.ui.base.BaseFragment
import com.edu.sun.oereminder.ui.checkin.CheckInDialogFragment
import com.edu.sun.oereminder.ui.checkin.CheckInDialogFragment.Companion.REQUEST_CHECK_IN
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.View
import com.edu.sun.oereminder.utils.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_time_tracking.*
import kotlinx.android.synthetic.main.toolbar_app.*
import java.util.*

class TimeTrackingFragment : BaseFragment<View, TimeTrackingPresenter>(), View {

    private var picker: MaterialDatePicker<Pair<Long, Long>>? = null
    private val timeTrackingAdapter by lazy { TimeRecordAdapter() }

    override val layoutRes get() = R.layout.fragment_time_tracking

    override val presenter by lazy {
        TimeTrackingPresenter(
            InjectorUtils.getTimeSheetRepository(requireContext())
        )
    }

    override fun setupView(instanceState: Bundle?) {

        toolbar.setTitle(R.string.title_item_time_tracking)
        presenter.loadScreen()

        GregorianCalendar().run {
            btnDatePicker.text = getString(
                R.string.date_range_format,
                firstDayOfMonth().toDate(DATE_FORMAT),
                lastDayOfMonth().toDate(DATE_FORMAT)
            )
        }
        presenter.selectedDate(firstMillisOfMonth(), lastMillisOfDay())

        picker = MaterialDatePicker.Builder.dateRangePicker().apply {
            setCalendarConstraints(
                CalendarConstraints.Builder().setEnd(System.currentTimeMillis()).build()
            )
        }.build()

        recyclerTimeTracking.adapter = timeTrackingAdapter
    }

    override fun initListener() {

        btnDatePicker.setOnClickListener {
            activity?.let { picker?.show(it.supportFragmentManager, picker.toString()) }
        }

        picker?.addOnPositiveButtonClickListener {
            btnDatePicker.text = picker?.headerText
            val from = it.first
            val to = it.second
            if (from != null && to != null) presenter.selectedDate(from, to)
        }

        fabCheckIn.setOnClickListener { presenter.onFabClick() }

        setFragmentResultListener(REQUEST_CHECK_IN) { _, bundle ->
            presenter.onCheckResult(bundle.getInt(REQUEST_CHECK_IN))
        }
    }

    override fun updateFab(isShow: Boolean, isCheckIn: Boolean) {
        fabCheckIn.run {
            isVisible = isShow
            setText(if (isCheckIn) R.string.text_button_check_in else R.string.text_button_check_out)
        }
    }

    override fun updateRecyclerView(timeRecords: List<TimeRecord>) {
        timeTrackingAdapter.submitList(timeRecords)
    }

    override fun updateRecyclerView(firstItem: TimeRecord) {
        timeTrackingAdapter.submitFirstItem(firstItem)
    }

    override fun navigateToCheckIn(isCheckIn: Boolean) {
        CheckInDialogFragment.newInstance(isCheckIn)
            .show(parentFragmentManager, CheckInDialogFragment::class.simpleName)
    }

    override fun navigateToSendReport(isPlan: Boolean) {

    }
}
