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
                        diskIO.execute {
                            local.updateMessages(data)
                            val localData = local.getMessages()
                            mainThread.execute { callback.onSuccess(localData) }
                        }
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun getUpdatedMessage(messageId: Long, callback: SourceCallback<List<Message>>) {
        with(appExecutors) {
            networkIO.execute {
                remote.getMessage(messageId.toString(), object : SourceCallback<Message> {

                    override fun onSuccess(data: Message) {
                        diskIO.execute {
                            local.updateMessages(listOf(data))
                            val localData = local.getMessages()
                            mainThread.execute { callback.onSuccess(localData) }
                        }
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun sendMessage(message: String, callback: SourceCallback<Long>) {
        with(appExecutors) {
            networkIO.execute {
                remote.sendMessage(message, object : SourceCallback<String> {

                    override fun onSuccess(data: String) {
                        mainThread.execute { callback.onSuccess(data.toLongOrNull() ?: 0L) }
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun updateMessage(messageId: Long, message: String, callback: SourceCallback<Long>) {
        with(appExecutors) {
            networkIO.execute {
                remote.updateMessage(messageId, message, object : SourceCallback<String> {

                    override fun onSuccess(data: String) {
                        mainThread.execute { callback.onSuccess(data.toLongOrNull() ?: 0L) }
                    }

                    override fun onError(e: Exception) {
                        mainThread.execute { callback.onError(e) }
                    }
                })
            }
        }
    }

    override fun deleteMessage(messageId: Long, callback: SourceCallback<List<Message>>) {
        with(appExecutors) {
            networkIO.execute {
                remote.deleteMessage(messageId, object : SourceCallback<String> {

                    override fun onSuccess(data: String) {
                        val msgId = data.toLongOrNull() ?: 0L
                        diskIO.execute {
                            local.deleteMessage(Message(msgId))
                            val updateList = local.getMessages()
                            mainThread.execute { callback.onSuccess(updateList) }
                        }
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
