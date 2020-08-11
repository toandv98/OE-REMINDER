package com.edu.sun.oereminder.ui.report

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.repository.MessageRepository
import com.edu.sun.oereminder.ui.base.BasePresenterImpl
import com.edu.sun.oereminder.ui.report.ReportContract.Presenter
import com.edu.sun.oereminder.ui.report.ReportContract.View
import com.edu.sun.oereminder.utils.toDate

class ReportPresenter(private val messageRepository: MessageRepository) :
    BasePresenterImpl<View>(), Presenter {

    private var unfilteredList = listOf<Message>()

    override fun loadScreen() {
        var callbackCount = 0
        messageRepository.getMessages(object : SourceCallback<List<Message>> {
            override fun onSuccess(data: List<Message>) {
                view?.run {
                    unfilteredList = data
                    updateRecyclerView(data)
                    showProgress(callbackCount == 0)
                    callbackCount++
                }
            }

            override fun onError(e: Exception) {
                view?.run {
                    showMessage(e.message.toString())
                    showProgress(false)
                }
            }
        })
    }

    override fun onFabClick() {

    }

    override fun onItemClick() {

    }

    override fun onQueryChanged(text: String) {
        val filterList = mutableListOf<Message>()
        if (text.isNotBlank()) {
            filterList.addAll(unfilteredList.filter {
                it.body.contains(text, true) || it.sendCalendar.toDate().contains(text, true)
            })
        } else {
            filterList.addAll(unfilteredList)
        }
        view?.updateRecyclerView(filterList)
    }
}
