package com.edu.sun.oereminder.utils

import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.model.Room
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
