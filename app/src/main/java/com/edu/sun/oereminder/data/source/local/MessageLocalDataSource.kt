package com.edu.sun.oereminder.data.source.local

import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.source.MessageDataSource
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.DESC
import com.edu.sun.oereminder.utils.ColumnName.SEND_TIME
import com.edu.sun.oereminder.utils.SQLiteConst.REPORT_LIMIT

class MessageLocalDataSource(private var dbHelper: DatabaseHelper) : MessageDataSource.Local {

    override fun getMessages() =
        dbHelper.get(Message::class, orderBy = "$SEND_TIME$DESC", limit = REPORT_LIMIT)

    override fun updateMessages(messages: List<Message>) = dbHelper.replace(messages)

    override fun updateMessage(message: Message) = dbHelper.update(message)

    override fun addMessage(message: Message) = dbHelper.insert(message)

    override fun deleteMessage(message: Message) = dbHelper.delete(message)

    companion object {
        @Volatile
        private var INSTANCE: MessageLocalDataSource? = null

        fun getInstance(dbHelper: DatabaseHelper) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MessageLocalDataSource(dbHelper).also { INSTANCE = it }
            }
    }
}
