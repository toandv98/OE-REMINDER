package com.edu.sun.oereminder.ui.timetracking

import com.edu.sun.oereminder.data.repository.TimeSheetRepository
import com.edu.sun.oereminder.ui.base.BasePresenterImpl
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.Presenter
import com.edu.sun.oereminder.ui.timetracking.TimeTrackingContract.View

class TimeTrackingPresenter(private val timeSheetRepository: TimeSheetRepository) :
    BasePresenterImpl<View>(), Presenter {

}
