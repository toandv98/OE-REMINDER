package com.edu.sun.oereminder.ui.statistics

import com.edu.sun.oereminder.data.repository.MessageRepository
import com.edu.sun.oereminder.data.repository.TimeSheetRepository
import com.edu.sun.oereminder.ui.base.BasePresenterImpl
import com.edu.sun.oereminder.ui.statistics.StatisticsContract.Presenter
import com.edu.sun.oereminder.ui.statistics.StatisticsContract.View

class StatisticsPresenter(
    private val messageRepository: MessageRepository,
    private val timeSheetRepository: TimeSheetRepository
) : BasePresenterImpl<View>(), Presenter {
}
