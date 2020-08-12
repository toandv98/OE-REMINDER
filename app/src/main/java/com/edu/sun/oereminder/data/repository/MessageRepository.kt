package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message

interface MessageRepository {

    fun getMessages(callback: SourceCallback<List<Message>>)

    fun getUpdatedMessage(messageId: Long, callback: SourceCallback<List<Message>>)

    fun sendMessage(message: String, callback: SourceCallback<Long>)

    fun updateMessage(messageId: Long, message: String, callback: SourceCallback<Long>)

    fun deleteMessage(messageId: Long, callback: SourceCallback<List<Message>>)
}
