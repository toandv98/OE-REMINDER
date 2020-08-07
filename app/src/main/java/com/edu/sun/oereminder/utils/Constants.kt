package com.edu.sun.oereminder.utils

import androidx.annotation.StringDef

object ColumnName {
    const val ROOM_ID = "room_id"
    const val FILE_NUM = "file_num"
    const val ICON_PATH = "icon_path"
    const val LAST_UPDATE_TIME = "last_update_time"
    const val MESSAGE_NUM = "message_num"
    const val NAME = "name"
    const val ROLE = "role"
    const val STICKY = "sticky"
    const val TYPE = "type"
    const val UNREAD_NUM = "unread_num"
    const val MESSAGE_ID = "message_id"
    const val BODY = "body"
    const val SEND_TIME = "send_time"
    const val UPDATE_TIME = "update_time"
    const val ACCOUNT_ID = "account_id"
    const val LOGIN_MAIL = "login_mail"
    const val AVATAR_URL = "avatar_image_url"
    const val ACCOUNT = "account"
    const val RECORD_ID = "record_id"
    const val WORK_DATE = "work_date"
    const val TIME_IN = "time_in"
    const val TIME_OUT = "time_out"
    const val PART_OF_DAY = "part_of_day"
    const val STATUS = "status"
}

object ApiEndPoint {
    const val BASE_URL = "https://api.chatwork.com/v2"
    const val ENDPOINT_ME = "/me"
    const val ENDPOINT_ROOMS = "/rooms"
    const val ENDPOINT_MEMBERS = "/members"
    const val ENDPOINT_MESSAGES = "/messages"
}

object NetConst {
    @StringDef(METHOD_GET, METHOD_DELETE, METHOD_POST, METHOD_PUT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RequestMethod

    const val METHOD_GET = "GET"
    const val METHOD_DELETE = "DELETE"
    const val METHOD_POST = "POST"
    const val METHOD_PUT = "PUT"

    const val CHARSET_UTF8 = "UTF-8"
    const val KEY_TOKEN_CHATWORK = "X-ChatWorkToken"
    const val FORCE_MESSAGE = "?force=1"
    const val CONNECT_TIMEOUT = 5000
    const val READ_TIMEOUT = 10000

    const val KEY_BODY = "body"
}

object PrefsConst {
    const val ENCRYPTED_PREFS_NAME = "secret_shared_prefs"
    const val KEY_API_TOKEN = "chat_work_token"
    const val KEY_ROOM_ID = "room_id"
}

object SQLiteConst {
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "app_db"
    const val TABLE_NAME_MESSAGE = "tb_message"
    const val TABLE_NAME_MEMBER = "tb_member"
    const val TABLE_NAME_ACCOUNT = "tb_account"
    const val TABLE_NAME_ROOM = "tb_room"
    const val TABLE_NAME_TIME_RECORDS = "tb_time_record"
}

object FragmentConst {
    const val CODE_SEND_REPORT = 2
    const val CODE_CHECK_IN = 0
    const val CODE_CHECK_OUT = 1
    const val REQUEST_CHECK_IN = "request_check_in"
    const val IS_CHECK_IN = "is_check_in"
}

object WorkTime {
    const val START_HOUR = 7L
    const val START_MINUS = 45L
    const val FINISH_HOUR = 16L
    const val FINISH_MINUS = 45L

    const val ON_TIME = "On Time"
    const val LATE = "Late"
}
