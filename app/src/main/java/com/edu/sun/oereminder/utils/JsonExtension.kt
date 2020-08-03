package com.edu.sun.oereminder.utils

import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.utils.JsonKey.ACCOUNT_ID
import com.edu.sun.oereminder.utils.JsonKey.AVATAR_IMAGE_URL
import com.edu.sun.oereminder.utils.JsonKey.BODY
import com.edu.sun.oereminder.utils.JsonKey.FILE_NUM
import com.edu.sun.oereminder.utils.JsonKey.ICON_PATH
import com.edu.sun.oereminder.utils.JsonKey.LAST_UPDATE_TIME
import com.edu.sun.oereminder.utils.JsonKey.LOGIN_MAIL
import com.edu.sun.oereminder.utils.JsonKey.MENTION_NUM
import com.edu.sun.oereminder.utils.JsonKey.MESSAGE_ID
import com.edu.sun.oereminder.utils.JsonKey.MESSAGE_NUM
import com.edu.sun.oereminder.utils.JsonKey.NAME
import com.edu.sun.oereminder.utils.JsonKey.ROLE
import com.edu.sun.oereminder.utils.JsonKey.ROOM_ID
import com.edu.sun.oereminder.utils.JsonKey.SEND_TIME
import com.edu.sun.oereminder.utils.JsonKey.STICKY
import com.edu.sun.oereminder.utils.JsonKey.TASK_NUM
import com.edu.sun.oereminder.utils.JsonKey.TYPE
import com.edu.sun.oereminder.utils.JsonKey.UNREAD_NUM
import com.edu.sun.oereminder.utils.JsonKey.UPDATE_TIME
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Throws(JSONException::class)
inline fun <reified T> String.parseJsonArray() = with(JSONArray(this)) {
    List(length()) {
        with(getJSONObject(it)) {
            when (T::class) {
                Message::class -> Message(this) as T
                Member::class -> Member(this) as T
                Room::class -> Room(this) as T
                Account::class -> Account(this) as T
                else -> throw JSONException("Type not supported")
            }
        }
    }
}

@Throws(JSONException::class)
inline fun <reified T> String.parseJsonObject() = with(JSONObject(this)) {
    when (T::class) {
        Message::class -> Message(this) as T
        Member::class -> Member(this) as T
        Room::class -> Room(this) as T
        Account::class -> Account(this) as T
        else -> throw JSONException("Type not supported")
    }
}
