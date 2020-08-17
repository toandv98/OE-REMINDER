package com.edu.sun.oereminder.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.edu.sun.oereminder.R
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.utils.*
import com.edu.sun.oereminder.utils.WorkTime.AFTERNOON
import com.edu.sun.oereminder.utils.WorkTime.MORNING
import com.edu.sun.oereminder.utils.WorkTime.OFF
import com.edu.sun.oereminder.utils.WorkTime.WHOLE_DAY
import com.google.android.material.transition.MaterialFade
import kotlinx.android.synthetic.main.dialog_time_sheet.*
import kotlinx.android.synthetic.main.toolbar_app.*
import java.util.*
import java.util.Calendar.*

class TimeSheetDialog : DialogFragment() {

    private val currentTimeSheet = mutableListOf<TimeRecord>()
    private val nextTimeSheet = mutableListOf<TimeRecord>()
    private var isNextMonth = false
    private val adapter by lazy { TimeSheetAdapter() }
    private val timeSheetRepository by lazy { InjectorUtils.getTimeSheetRepository(requireContext()) }

    override fun getTheme() = R.style.MessageDialogStyle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_time_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupTimeSheet()
        initListener()
    }

    private fun setupView() {
        recyclerTimeSheet.adapter = adapter
        toolbar.apply {
            setTitle(R.string.title_time_sheet)
            inflateMenu(R.menu.menu_time_sheet)
            setNavigationIcon(R.drawable.ic_round_arrow_back)
        }
        GregorianCalendar().getDisplayNames(DAY_OF_WEEK, STYLE_NO_TITLE, Locale.getDefault())?.run {
            textMonday.text = getFirstKey(MONDAY)
            textTuesday.text = getFirstKey(TUESDAY)
            textWednesday.text = getFirstKey(WEDNESDAY)
            textThursday.text = getFirstKey(THURSDAY)
            textFriday.text = getFirstKey(FRIDAY)
        }
        GregorianCalendar().run {
            switchMonth.run {
                textOff = getDisplayName(MONTH, STYLE_NO_FRAME, Locale.getDefault()).capitalize()
                textOn = plus(MONTH, 1).getDisplayName(MONTH, STYLE_NO_FRAME, Locale.getDefault())
                    .capitalize()
            }
        }
    }

    private fun initListener() {
        toolbar.run {
            setNavigationOnClickListener { dismiss() }
            setOnMenuItemClickListener {
                if (it.itemId == R.id.item_save) {
                    progressLoad.isVisible = true
                    updateTimeSheet()
                    true
                } else {
                    false
                }
            }
        }
        adapter.setOnCheckedListener { timeRecord, i ->
            getCurrentList()[i] = timeRecord
        }
        switchMonth.setOnCheckedChangeListener { _, isCheck ->
            isNextMonth = isCheck
            constraint.beginTransition(MaterialFade(), R.id.imageNext, R.id.imagePrevious)
            imagePrevious.isVisible = isCheck
            imageNext.isVisible = !isCheck
            adapter.submitList(getCurrentList())
        }
        chipGroupSelectAll.setOnCheckedChangeListener { _, checkedId ->
            getCurrentList().filter { it.dateCalendar.after(GregorianCalendar()) }.run {
                when (checkedId) {
                    R.id.chipAfternoon -> forEach { it.partOfDay = AFTERNOON }
                    R.id.chipMorning -> forEach { it.partOfDay = MORNING }
                    R.id.chipWhole -> forEach { it.partOfDay = WHOLE_DAY }
                    else -> forEach { it.partOfDay = OFF }
                }
                getCurrentList().let { adapter.notifyItemRangeChanged(it.size - size, it.size) }
            }
        }
    }

    private fun getCurrentList() = if (isNextMonth) nextTimeSheet else currentTimeSheet
    private fun Map<String, Int>.getFirstKey(value: Int) = filterValues { it == value }.keys.first()

    private fun setupTimeSheet() {
        timeSheetRepository.getTimeRecords(firstMillisOfMonth(), lastMillisOfMonth(), true,
            object : SourceCallback<List<TimeRecord>> {
                override fun onSuccess(data: List<TimeRecord>) {
                    currentTimeSheet.addAll(data)
                    progressLoad.isVisible = false
                    adapter.submitList(getCurrentList())
                }

                override fun onError(e: Exception) {
                    context?.toast(e.message.toString())
                }
            })

        GregorianCalendar().plus(MONTH, 1).run {
            timeSheetRepository.getTimeRecords(
                firstDayOfMonth().firstMillisOfDay(),
                lastDayOfMonth().lastMillisOfDay(),
                true,
                object : SourceCallback<List<TimeRecord>> {
                    override fun onSuccess(data: List<TimeRecord>) {
                        nextTimeSheet.addAll(data)
                    }

                    override fun onError(e: Exception) {
                        context?.toast(e.message.toString())
                    }
                })
        }
    }

    private fun updateTimeSheet() {
        val updateList = mutableListOf<TimeRecord>().apply {
            addAll(currentTimeSheet)
            addAll(nextTimeSheet)
        }
        timeSheetRepository.updateTimeSheet(updateList, object : SourceCallback<Int> {
            override fun onSuccess(data: Int) {
                progressLoad.isVisible = false
                dismiss()
            }

            override fun onError(e: Exception) {
                progressLoad.isVisible = false
                context?.toast(e.message.toString())
                dismiss()
            }
        })
    }
}
