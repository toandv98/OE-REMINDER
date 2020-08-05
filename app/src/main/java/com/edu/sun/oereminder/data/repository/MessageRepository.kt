package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message

interface MessageRepository {

    fun getMessages(callback: SourceCallback<List<Message>>)

    fun sendMessage(message: String, callback: SourceCallback<Message>)

    fun updateMessage(messageId: Int, message: String, callback: SourceCallback<Message>)

    fun deleteMessage(messageId: Int, callback: SourceCallback<Long>)
}
