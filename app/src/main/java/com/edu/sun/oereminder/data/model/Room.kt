package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.utils.JsonKey.FILE_NUM
import com.edu.sun.oereminder.utils.JsonKey.ICON_PATH
import com.edu.sun.oereminder.utils.JsonKey.LAST_UPDATE_TIME
import com.edu.sun.oereminder.utils.JsonKey.MENTION_NUM
import com.edu.sun.oereminder.utils.JsonKey.MESSAGE_NUM
import com.edu.sun.oereminder.utils.JsonKey.NAME
import com.edu.sun.oereminder.utils.JsonKey.ROLE
import com.edu.sun.oereminder.utils.JsonKey.ROOM_ID
import com.edu.sun.oereminder.utils.JsonKey.STICKY
import com.edu.sun.oereminder.utils.JsonKey.TASK_NUM
import com.edu.sun.oereminder.utils.JsonKey.TYPE
import com.edu.sun.oereminder.utils.JsonKey.UNREAD_NUM
import org.json.JSONException
import org.json.JSONObject

data class Room(
    val roomId: Int,
    val fileNum: Int,
    val iconPath: String,
    val lastUpdateTime: Int,
    val mentionNum: Int,
    val messageNum: Int,
    val name: String,
    val role: String,
    val sticky: Boolean,
    val taskNum: Int,
    val type: String,
    val unreadNum: Int
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(ROOM_ID),
        jsonObject.getInt(FILE_NUM),
        jsonObject.getString(ICON_PATH),
        jsonObject.getInt(LAST_UPDATE_TIME),
        jsonObject.getInt(MENTION_NUM),
        jsonObject.getInt(MESSAGE_NUM),
        jsonObject.getString(NAME),
        jsonObject.getString(ROLE),
        jsonObject.getBoolean(STICKY),
        jsonObject.getInt(TASK_NUM),
        jsonObject.getString(TYPE),
        jsonObject.getInt(UNREAD_NUM)
    )
}
