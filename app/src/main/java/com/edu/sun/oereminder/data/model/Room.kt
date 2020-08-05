package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.data.source.local.dbutils.Column
import com.edu.sun.oereminder.data.source.local.dbutils.Table
import com.edu.sun.oereminder.utils.ColumnName.FILE_NUM
import com.edu.sun.oereminder.utils.ColumnName.ICON_PATH
import com.edu.sun.oereminder.utils.ColumnName.LAST_UPDATE_TIME
import com.edu.sun.oereminder.utils.ColumnName.MESSAGE_NUM
import com.edu.sun.oereminder.utils.ColumnName.NAME
import com.edu.sun.oereminder.utils.ColumnName.ROLE
import com.edu.sun.oereminder.utils.ColumnName.ROOM_ID
import com.edu.sun.oereminder.utils.ColumnName.STICKY
import com.edu.sun.oereminder.utils.ColumnName.TYPE
import com.edu.sun.oereminder.utils.ColumnName.UNREAD_NUM
import com.edu.sun.oereminder.utils.SQLiteConst.TABLE_NAME_ROOM
import org.json.JSONException
import org.json.JSONObject

@Table(tableName = TABLE_NAME_ROOM)
data class Room(
    @Column(columnName = ROOM_ID, primaryKey = true) val roomId: Long,
    @Column(columnName = FILE_NUM) val fileNum: Int,
    @Column(columnName = ICON_PATH) val iconPath: String,
    @Column(columnName = LAST_UPDATE_TIME) val lastUpdateTime: Long,
    @Column(columnName = MESSAGE_NUM) val messageNum: Int,
    @Column(columnName = NAME) val name: String,
    @Column(columnName = ROLE) val role: String,
    @Column(columnName = STICKY) val sticky: Boolean,
    @Column(columnName = TYPE) val type: String,
    @Column(columnName = UNREAD_NUM) val unreadNum: Int
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getLong(ROOM_ID),
        jsonObject.getInt(FILE_NUM),
        jsonObject.getString(ICON_PATH),
        jsonObject.getLong(LAST_UPDATE_TIME),
        jsonObject.getInt(MESSAGE_NUM),
        jsonObject.getString(NAME),
        jsonObject.getString(ROLE),
        jsonObject.getBoolean(STICKY),
        jsonObject.getString(TYPE),
        jsonObject.getInt(UNREAD_NUM)
    )
}
