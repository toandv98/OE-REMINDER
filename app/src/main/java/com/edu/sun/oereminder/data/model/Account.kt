package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.utils.JsonKey.ACCOUNT_ID
import com.edu.sun.oereminder.utils.JsonKey.AVATAR_IMAGE_URL
import com.edu.sun.oereminder.utils.JsonKey.LOGIN_MAIL
import com.edu.sun.oereminder.utils.JsonKey.NAME
import org.json.JSONException
import org.json.JSONObject

data class Account(
    var accountId: Int,
    var avatarUrl: String,
    var loginMail: String,
    var name: String
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(ACCOUNT_ID),
        jsonObject.getString(AVATAR_IMAGE_URL),
        jsonObject.getString(LOGIN_MAIL),
        jsonObject.getString(NAME)
    )
}
