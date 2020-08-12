package com.edu.sun.oereminder.ui.timetracking

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.data.repository.TimeSheetRepository
import com.edu.sun.oereminder.ui.base.BasePresenterImpl
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.Presenter
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.View
import com.edu.sun.oereminder.utils.FragmentConst.CODE_CHECK_IN
import com.edu.sun.oereminder.utils.FragmentConst.CODE_CHECK_OUT
import com.edu.sun.oereminder.utils.FragmentConst.CODE_SEND_REPORT
import com.edu.sun.oereminder.utils.WorkTime.LATE
import com.edu.sun.oereminder.utils.WorkTime.ON_TIME
import com.edu.sun.oereminder.utils.WorkTime.START_HOUR
import com.edu.sun.oereminder.utils.WorkTime.START_MINUS
import com.edu.sun.oereminder.utils.millisUtil
import com.edu.sun.oereminder.utils.now

class TimeTrackingPresenter(private val timeSheetRepository: TimeSheetRepository) :
    BasePresenterImpl<View>(), Presenter {

    private var isCheckIn = true
    private var isShowFab = false
    private var currentTimeRecord: TimeRecord? = null

    override fun loadScreen() = timeSheetRepository.getTimeRecord(
        now(), object : SourceCallback<TimeRecord> {
            override fun onSuccess(data: TimeRecord) {
                data.run {
                    currentTimeRecord = this
                    isCheckIn = timeIn == 0L
                    isShowFab = timeIn == 0L || timeOut == 0L
                }
                view?.updateFab(isShowFab, isCheckIn)
            }

            override fun onError(e: Exception) {
                isShowFab = false
                view?.updateFab(isShowFab, isCheckIn)
            }
        })

    override fun selectedDate(from: Long, to: Long) = timeSheetRepository.getTimeRecords(
        from, to,
        object : SourceCallback<List<TimeRecord>> {
            override fun onSuccess(data: List<TimeRecord>) {
                view?.updateRecyclerView(data)
            }

            override fun onError(e: Exception) {
                view?.showMessage(e.message.toString())
            }
        })

    override fun onFabClick() {
        view?.navigateToCheckIn(isCheckIn)
    }

    override fun onCheckResult(code: Int) {
        currentTimeRecord?.run {
            when (code) {
                CODE_CHECK_IN -> {
                    timeIn = now()
                    status = if (timeIn <= millisUtil(START_HOUR, START_MINUS)) ON_TIME else LATE
                    isCheckIn = false
                }
                CODE_CHECK_OUT -> {
                    timeOut = now()
                    isShowFab = false
                }
                CODE_SEND_REPORT -> view?.navigateToSendReport(isCheckIn)
            }
            timeSheetRepository.updateTimeRecord(this)
            view?.updateRecyclerView(this)
            view?.updateFab(isShowFab, isCheckIn)
        }
    }
}
