package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.data.source.local.dbutils.Column
import com.edu.sun.oereminder.data.source.local.dbutils.Table
import com.edu.sun.oereminder.utils.ColumnName.ACCOUNT
import com.edu.sun.oereminder.utils.ColumnName.ACCOUNT_ID
import com.edu.sun.oereminder.utils.ColumnName.AVATAR_URL
import com.edu.sun.oereminder.utils.ColumnName.BODY
import com.edu.sun.oereminder.utils.ColumnName.MESSAGE_ID
import com.edu.sun.oereminder.utils.ColumnName.NAME
import com.edu.sun.oereminder.utils.ColumnName.SEND_TIME
import com.edu.sun.oereminder.utils.ColumnName.UPDATE_TIME
import com.edu.sun.oereminder.utils.SQLiteConst.TABLE_NAME_MESSAGE
import org.json.JSONException
import org.json.JSONObject

@Table(TABLE_NAME_MESSAGE)
data class Message(
    @Column(columnName = MESSAGE_ID, primaryKey = true) val messageId: Long,
    @Column(columnName = BODY) val body: String,
    @Column(columnName = SEND_TIME) val sendTime: Long,
    @Column(columnName = UPDATE_TIME) val updateTime: Long,
    @Column(columnName = ACCOUNT_ID) val accountId: Long,
    @Column(columnName = AVATAR_URL) val avatarUrl: String,
    @Column(columnName = NAME) val name: String
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getLong(MESSAGE_ID),
        jsonObject.getString(BODY),
        jsonObject.getLong(SEND_TIME),
        jsonObject.getLong(UPDATE_TIME),
        jsonObject.getJSONObject(ACCOUNT).getLong(ACCOUNT_ID),
        jsonObject.getJSONObject(ACCOUNT).getString(AVATAR_URL),
        jsonObject.getJSONObject(ACCOUNT).getString(NAME)
    )
}
