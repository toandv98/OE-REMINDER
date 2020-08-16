package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.data.source.local.dbutils.Column
import com.edu.sun.oereminder.data.source.local.dbutils.Table
import com.edu.sun.oereminder.utils.ColumnName.ACCOUNT_ID
import com.edu.sun.oereminder.utils.ColumnName.AVATAR_URL
import com.edu.sun.oereminder.utils.ColumnName.NAME
import com.edu.sun.oereminder.utils.ColumnName.ROLE
import com.edu.sun.oereminder.utils.SQLiteConst.TABLE_NAME_MEMBER
import org.json.JSONException
import org.json.JSONObject

@Table(tableName = TABLE_NAME_MEMBER)
data class Member(
    @Column(columnName = ACCOUNT_ID, primaryKey = true) val accountId: Long,
    @Column(columnName = AVATAR_URL) val avatarUrl: String,
    @Column(columnName = NAME) val name: String,
    @Column(columnName = ROLE) var role: String
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getLong(ACCOUNT_ID),
        jsonObject.getString(AVATAR_URL),
        jsonObject.getString(NAME),
        jsonObject.getString(ROLE)
    )
}
