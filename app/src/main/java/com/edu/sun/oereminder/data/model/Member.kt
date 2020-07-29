package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.utils.JsonKey.ACCOUNT_ID
import com.edu.sun.oereminder.utils.JsonKey.AVATAR_IMAGE_URL
import com.edu.sun.oereminder.utils.JsonKey.NAME
import com.edu.sun.oereminder.utils.JsonKey.ROLE
import org.json.JSONException
import org.json.JSONObject

data class Member(
    val accountId: Int,
    val avatarUrl: String,
    val name: String,
    val role: String
) {
    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(ACCOUNT_ID),
        jsonObject.getString(AVATAR_IMAGE_URL),
        jsonObject.getString(NAME),
        jsonObject.getString(ROLE)
    )
}
