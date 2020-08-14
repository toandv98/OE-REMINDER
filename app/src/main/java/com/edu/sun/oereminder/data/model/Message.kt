package com.edu.sun.oereminder.data.model

import android.os.Parcelable
import com.edu.sun.oereminder.data.source.local.dbutils.Column
import com.edu.sun.oereminder.data.source.local.dbutils.Table
import com.edu.sun.oereminder.utils.*
import com.edu.sun.oereminder.utils.ColumnName.ACCOUNT
import com.edu.sun.oereminder.utils.ColumnName.ACCOUNT_ID
import com.edu.sun.oereminder.utils.ColumnName.AVATAR_URL
import com.edu.sun.oereminder.utils.ColumnName.BODY
import com.edu.sun.oereminder.utils.ColumnName.MESSAGE_ID
import com.edu.sun.oereminder.utils.ColumnName.NAME
import com.edu.sun.oereminder.utils.ColumnName.SEND_TIME
import com.edu.sun.oereminder.utils.ColumnName.UPDATE_TIME
import com.edu.sun.oereminder.utils.SQLiteConst.TABLE_NAME_MESSAGE
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.util.*

@Parcelize
@Table(TABLE_NAME_MESSAGE)
data class Message(
    @Column(columnName = MESSAGE_ID, primaryKey = true) val messageId: Long = 0,
    @Column(columnName = BODY) var body: String = "",
    @Column(columnName = SEND_TIME) val sendTime: Long = 0,
    @Column(columnName = UPDATE_TIME) val updateTime: Long = 0,
    @Column(columnName = ACCOUNT_ID) val accountId: Long = 0,
    @Column(columnName = AVATAR_URL) val avatarUrl: String = "",
    @Column(columnName = NAME) val name: String = ""
) : Parcelable {
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

    val messageTitle get() = body.remove(MESSAGE_TO_REGEX).remove(PLAN_REGEX).trim()

    val messageReport: String
        get() = PLAN_REGEX.toRegex().find(body)?.value?.run {
            remove(INFO_NOTATION)
                .trimEndLine()
                .toHtmlEndLine()
                .replaceWithRegex(TITLE_OPEN_NOTATION, TAG_B_OPEN)
                .replaceWithRegex(TITLE_CLOSE_NOTATION, TAG_B_CLOSE)
                .trim()
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
        private const val PLAN_TITLE_REGEX = "<font color='#000'><b>1. Plan:</b></font>"
        private const val ACTUAL_TITLE_REGEX = "<font color='#000'><b>2. Actual:</b></font>"
        private const val NEXT_TITLE_REGEX = "<font color='#000'><b>3. Next:</b></font>"
        private const val ISSUE_TITLE_REGEX = "<font color='#000'><b>4. Issue:</b></font>"
        const val PLAN_CONTENT_REGEX = "<font color='#000'><b>1. Plan [\\s\\S]*</b></font>"
        const val REPORT_PLAN_REGEX = "$PLAN_TITLE_REGEX|$ACTUAL_TITLE_REGEX[\\s\\S]*"
        const val ACTUAL_CONTENT_REGEX = "[\\s\\S]*$ACTUAL_TITLE_REGEX|$NEXT_TITLE_REGEX[\\s\\S]*"
        const val NEXT_CONTENT_REGEX = "[\\s\\S]*$NEXT_TITLE_REGEX|$ISSUE_TITLE_REGEX[\\s\\S]*"
        const val ISSUE_CONTENT_REGEX = "[\\s\\S]*$ISSUE_TITLE_REGEX"
    }
}
