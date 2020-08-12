package com.edu.sun.oereminder.ui.report

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.repository.MessageRepository
import com.edu.sun.oereminder.ui.base.BasePresenterImpl
import com.edu.sun.oereminder.ui.message.MessageDialogFragment.Companion.CODE_DELETE_REPORT
import com.edu.sun.oereminder.ui.message.MessageDialogFragment.Companion.CODE_NEW_REPORT
import com.edu.sun.oereminder.ui.report.ReportContract.Presenter
import com.edu.sun.oereminder.ui.report.ReportContract.View
import com.edu.sun.oereminder.utils.firstMillisOfDay
import com.edu.sun.oereminder.utils.toDate

class ReportPresenter(private val messageRepository: MessageRepository) :
    BasePresenterImpl<View>(), Presenter {

    private var unfilteredList = listOf<Message>()
    private var isPlan = true
    private var currentMessage: Message? = null

    override fun loadScreen() {
        var callbackCount = 0
        messageRepository.getMessages(object : SourceCallback<List<Message>> {
            override fun onSuccess(data: List<Message>) {
                handleRefreshData(data, callbackCount == 0)
                callbackCount++
            }

            override fun onError(e: Exception) {
                showError(e)
            }
        })
    }

    override fun onFabClick() {
        view?.navigateToMessage(isPlan, message = currentMessage)
    }

    override fun onItemClick(message: Message) {
        currentMessage = message
        view?.navigateToMessage(message.isReport.not(), true, message)
    }

    override fun onDialogResult(code: Int, messageBody: String) {
        view?.showProgress(true)
        when (code) {
            CODE_DELETE_REPORT -> deleteReport()
            CODE_NEW_REPORT -> addNewReport(messageBody)
            else -> editReport(messageBody)
        }
    }

    private fun handleRefreshData(data: List<Message>, showProgress: Boolean) {
        unfilteredList = data
        currentMessage = unfilteredList.firstOrNull()
        currentMessage?.run {
            if (1000 * sendTime > firstMillisOfDay()) {
                this@ReportPresenter.isPlan = isReport
                view?.showFab(!isReport, isReport)
            } else {
                view?.showFab(true)
            }
        }
        view?.run {
            updateRecyclerView(data)
            showProgress(showProgress)
            if (data.isEmpty()) {
                showFab(true)
                isPlan = true
            }
        }
    }

    private fun deleteReport() {
        currentMessage?.run {
            messageRepository.deleteMessage(messageId, object : SourceCallback<List<Message>> {
                override fun onSuccess(data: List<Message>) {
                    handleRefreshData(data, false)
                }

                override fun onError(e: Exception) {
                    showError(e)
                }
            })
        }
    }

    private fun addNewReport(messageBody: String) {
        messageRepository.sendMessage(messageBody, object : SourceCallback<Long> {
            override fun onSuccess(data: Long) {
                messageRepository.getUpdatedMessage(
                    data,
                    object : SourceCallback<List<Message>> {
                        override fun onSuccess(data: List<Message>) {
                            handleRefreshData(data, false)
                        }

                        override fun onError(e: Exception) {
                            showError(e)
                        }
                    })
            }

            override fun onError(e: Exception) {
                showError(e)
            }
        })
    }

    private fun editReport(messageBody: String) {
        currentMessage?.run {
            messageRepository.updateMessage(messageId, messageBody, object : SourceCallback<Long> {
                override fun onSuccess(data: Long) {
                    messageRepository.getUpdatedMessage(data,
                        object : SourceCallback<List<Message>> {
                            override fun onSuccess(data: List<Message>) {
                                handleRefreshData(data, false)
                            }

                            override fun onError(e: Exception) {
                                showError(e)
                            }
                        })
                }

                override fun onError(e: Exception) {
                    showError(e)
                }
            })
        }
    }

    private fun showError(e: Exception) {
        view?.run {
            showMessage(e.message.toString())
            showProgress(false)
        }
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
