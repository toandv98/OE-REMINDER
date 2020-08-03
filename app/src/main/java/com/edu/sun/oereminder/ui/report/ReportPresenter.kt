package com.edu.sun.oereminder.ui.report

import com.edu.sun.oereminder.data.repository.MessageRepository
import com.edu.sun.oereminder.ui.base.BasePresenterImpl
import com.edu.sun.oereminder.ui.report.ReportContract.Presenter
import com.edu.sun.oereminder.ui.report.ReportContract.View

class ReportPresenter(private val messageRepository: MessageRepository) :
    BasePresenterImpl<View>(), Presenter {

}
