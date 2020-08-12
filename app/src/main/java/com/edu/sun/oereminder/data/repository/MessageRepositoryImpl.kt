package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.AppExecutors
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.source.MessageDataSource

class MessageRepositoryImpl private constructor(
    private val local: MessageDataSource.Local,
    private val remote: MessageDataSource.Remote,
    private val appExecutors: AppExecutors
) : MessageRepository {

    override fun getMessages(callback: SourceCallback<List<Message>>) {
        with(appExecutors) {
            diskIO.execute {
                val data = local.getMessages()
                mainThread.execute { callback.onSuccess(data) }
            }
            networkIO.execute {
                remote.getMessages(object : SourceCallback<List<Message>> {

                    override fun onSuccess(data: List<Message>) {
                        mainThread.execute { callback.onSuccess(data.reversed()) }
                        diskIO.execute { local.updateMessages(data) }
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun sendMessage(message: String, callback: SourceCallback<Message>) {
        with(appExecutors) {
            networkIO.execute {
                remote.sendMessage(message, object : SourceCallback<String> {

                    override fun onSuccess(data: String) {
                        remote.getMessage(data, object : SourceCallback<Message> {
                            override fun onSuccess(data: Message) {
                                diskIO.execute {
                                    local.addMessage(data)
                                    mainThread.execute { callback.onSuccess(data) }
                                }
                            }

                            override fun onError(e: Exception) {
                                mainThread.execute { callback.onError(e) }
                            }
                        })
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun updateMessage(messageId: Int, message: String, callback: SourceCallback<Message>) {
        with(appExecutors) {
            networkIO.execute {
                remote.updateMessage(messageId, message, object : SourceCallback<String> {

                    override fun onSuccess(data: String) {
                        remote.getMessage(data, object : SourceCallback<Message> {
                            override fun onSuccess(data: Message) {
                                diskIO.execute {
                                    local.updateMessage(data)
                                    mainThread.execute { callback.onSuccess(data) }
                                }
                            }

                            override fun onError(e: Exception) {
                                mainThread.execute { callback.onError(e) }
                            }
                        })
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun deleteMessage(messageId: Int, callback: SourceCallback<Long>) {
        with(appExecutors) {
            networkIO.execute {
                remote.deleteMessage(messageId, object : SourceCallback<String> {

                    override fun onSuccess(data: String) {
                        remote.getMessage(data, object : SourceCallback<Message> {
                            override fun onSuccess(data: Message) {
                                diskIO.execute {
                                    local.deleteMessage(data)
                                    mainThread.execute { callback.onSuccess(data.messageId) }
                                }
                            }

                            override fun onError(e: Exception) {
                                mainThread.execute { callback.onError(e) }
                            }
                        })
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MessageRepository? = null

        fun getInstance(
            local: MessageDataSource.Local,
            remote: MessageDataSource.Remote,
            appExecutors: AppExecutors
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MessageRepositoryImpl(local, remote, appExecutors).also { INSTANCE = it }
        }
    }
}
