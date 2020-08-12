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
import com.edu.sun.oereminder.utils.from
import org.json.JSONException
import org.json.JSONObject
import java.util.*

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

    val sendCalendar get() = GregorianCalendar().from(sendTime * 1000)

    val updateCalendar get() = GregorianCalendar().from(updateTime * 1000)

    val isPlan get() = body.contains(PLAN_REGEX.toRegex())

    val isReport get() = body.contains(REPORT_REGEX.toRegex())

    val messageTitle
        get() = body.replace(MESSAGE_TO_REGEX.toRegex(), "")
            .replace(PLAN_REGEX.toRegex(), "").trim()

    val messageReport: String
        get() = PLAN_REGEX.toRegex().find(body)?.value?.run {
            replace(INFO_NOTATION.toRegex(), "")
            replace(FIRST_LAST_END_LINE.toRegex(), "")
            replace(END_LINE, HTML_END_LINE)
            replace(TITLE_OPEN_NOTATION.toRegex(), TAG_B_OPEN)
            replace(TITLE_CLOSE_NOTATION.toRegex(), TAG_B_CLOSE)
            trim()
        } ?: ""

    companion object {
        const val MESSAGE_TO_REGEX = "\\[To:.*?\\n"
        const val PLAN_REGEX = "\\[info]\\n\\[title]1. Plan[\\s\\S]*"
        const val REPORT_REGEX = "\\[title]2. Actual[\\s\\S]*"
        const val TITLE_OPEN_NOTATION = "\\[title]"
        const val TITLE_CLOSE_NOTATION = "\\[/title]"
        const val INFO_NOTATION = "\\[\\S?info]"
        const val TAG_B_OPEN = "<font color='#000'><b>"
        const val TAG_B_CLOSE = "</b></font>"
        const val FIRST_LAST_END_LINE = "^\\n|\\n\$"
        const val HTML_END_LINE = "<br>"
        const val END_LINE = "\n"
    }
}
