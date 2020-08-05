package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.data.source.local.dbutils.Column
import com.edu.sun.oereminder.data.source.local.dbutils.Table
import com.edu.sun.oereminder.utils.ColumnName.ACCOUNT_ID
import com.edu.sun.oereminder.utils.ColumnName.AVATAR_URL
import com.edu.sun.oereminder.utils.ColumnName.LOGIN_MAIL
import com.edu.sun.oereminder.utils.ColumnName.NAME
import com.edu.sun.oereminder.utils.SQLiteConst.TABLE_NAME_ACCOUNT
import org.json.JSONException
import org.json.JSONObject

@Table(tableName = TABLE_NAME_ACCOUNT)
data class Account(
    @Column(columnName = ACCOUNT_ID, primaryKey = true) val accountId: Long,
    @Column(columnName = AVATAR_URL) val avatarUrl: String,
    @Column(columnName = LOGIN_MAIL) val loginMail: String,
    @Column(columnName = NAME) val name: String
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getLong(ACCOUNT_ID),
        jsonObject.getString(AVATAR_URL),
        jsonObject.getString(LOGIN_MAIL),
        jsonObject.getString(NAME)
    )
}
