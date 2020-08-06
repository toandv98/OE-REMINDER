package com.edu.sun.oereminder.ui.timetracking

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.util.Pair
import androidx.fragment.app.setFragmentResultListener
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.ui.base.BaseFragment
import com.edu.sun.oereminder.ui.checkin.CheckInDialogFragment
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.View
import com.edu.sun.oereminder.utils.*
import com.edu.sun.oereminder.utils.FragmentConst.KEY_CHECK_IN
import com.edu.sun.oereminder.utils.FragmentConst.KEY_SEND_REPORT
import com.edu.sun.oereminder.utils.FragmentConst.REQUEST_CHECK_IN
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_time_tracking.*
import kotlinx.android.synthetic.main.toolbar_app.*
import java.util.*

class TimeTrackingFragment : BaseFragment<View, TimeTrackingPresenter>(), View {

    private var picker: MaterialDatePicker<Pair<Long, Long>>? = null
    private val timeTrackingAdapter = TimeRecordAdapter()

    override val layoutRes get() = R.layout.fragment_time_tracking

    override val presenter by lazy {
        TimeTrackingPresenter(
            InjectorUtils.getTimeSheetRepository(requireContext())
        )
    }

    override fun setupView(instanceState: Bundle?) {

        toolbar.setTitle(R.string.title_item_time_tracking)
        presenter.load()

        GregorianCalendar().run {
            btnDatePicker.text =
                getString(
                    R.string.date_range_format,
                    firstDayOfMonth().toDate(DATE_FORMAT),
                    lastDayOfMonth().toDate(DATE_FORMAT)
                )
            presenter.onDateSelected(firstDayOfMonth().timeInMillis, lastDayOfMonth().timeInMillis)
        }

        picker = MaterialDatePicker.Builder.dateRangePicker().apply {
            setCalendarConstraints(
                CalendarConstraints.Builder().setEnd(GregorianCalendar().timeInMillis).build()
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
            it.first?.let { from ->
                it.second?.let { to ->
                    presenter.onDateSelected(from, to)
                }
            }
        }

        fabCheckIn.setOnClickListener {
            navigateToCheckIn()
        }

        setFragmentResultListener(REQUEST_CHECK_IN) { _, bundle ->
            when (bundle[REQUEST_CHECK_IN]) {
                KEY_SEND_REPORT -> {

                }
                KEY_CHECK_IN -> {

                }
            }
        }
    }

    override fun showOrHideFab(isShow: Boolean) {
        fabCheckIn.visibility = if (isShow) VISIBLE else GONE
    }

    override fun updateRecyclerView(timeRecords: List<TimeRecord>) {
        timeTrackingAdapter.submitList(timeRecords)
    }

    override fun navigateToCheckIn() {
        CheckInDialogFragment().show(parentFragmentManager, CheckInDialogFragment::class.simpleName)
    }
}
