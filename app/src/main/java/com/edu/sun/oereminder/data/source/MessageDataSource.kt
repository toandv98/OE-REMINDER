package com.edu.sun.oereminder.data.source

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message

interface MessageDataSource {

    interface Local {
        fun getMessages(): List<Message>

        fun updateMessages(messages: List<Message>)

        fun updateMessage(message: Message)

        fun addMessage(message: Message)

        fun deleteMessage(message: Message)
    }

    interface Remote {

        fun getMessage(messageId: String, callback: SourceCallback<Message>)

        fun getMessages(callback: SourceCallback<List<Message>>)

        fun sendMessage(message: String, callback: SourceCallback<String>)

        fun updateMessage(messageId: Long, content: String, callback: SourceCallback<String>)

        fun deleteMessage(messageId: Long, callback: SourceCallback<String>)
    }
}
