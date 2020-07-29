package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.utils.JsonKey.ACCOUNT_ID
import com.edu.sun.oereminder.utils.JsonKey.AVATAR_IMAGE_URL
import com.edu.sun.oereminder.utils.JsonKey.BODY
import com.edu.sun.oereminder.utils.JsonKey.MESSAGE_ID
import com.edu.sun.oereminder.utils.JsonKey.NAME
import com.edu.sun.oereminder.utils.JsonKey.SEND_TIME
import com.edu.sun.oereminder.utils.JsonKey.UPDATE_TIME
import org.json.JSONException
import org.json.JSONObject

data class Message(
    val messageId: Int = 0,
    val body: String,
    val sendTime: Int,
    val updateTime: Int,
    val accountId: Int,
    val avatarUrl: String,
    val name: String
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(MESSAGE_ID),
        jsonObject.getString(BODY),
        jsonObject.getInt(SEND_TIME),
        jsonObject.getInt(UPDATE_TIME),
        jsonObject.getInt(ACCOUNT_ID),
        jsonObject.getString(AVATAR_IMAGE_URL),
        jsonObject.getString(NAME)
    )
}
