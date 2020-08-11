package com.edu.sun.oereminder.data.source.remote

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Message
import com.edu.sun.oereminder.data.source.MessageDataSource
import com.edu.sun.oereminder.data.source.preferences.PreferencesHelper
import com.edu.sun.oereminder.utils.ApiEndPoint.BASE_URL
import com.edu.sun.oereminder.utils.ApiEndPoint.ENDPOINT_MESSAGES
import com.edu.sun.oereminder.utils.ApiEndPoint.ENDPOINT_ROOMS
import com.edu.sun.oereminder.utils.NetConst.FORCE_MESSAGE
import com.edu.sun.oereminder.utils.NetConst.KEY_BODY
import com.edu.sun.oereminder.utils.NetConst.KEY_TOKEN_CHATWORK
import com.edu.sun.oereminder.utils.NetConst.METHOD_DELETE
import com.edu.sun.oereminder.utils.NetConst.METHOD_GET
import com.edu.sun.oereminder.utils.NetConst.METHOD_POST
import com.edu.sun.oereminder.utils.NetConst.METHOD_PUT
import com.edu.sun.oereminder.utils.parseJsonArray
import com.edu.sun.oereminder.utils.parseJsonObject
import org.json.JSONException

class MessageRemoteDataSource private constructor(private val prefsHelper: PreferencesHelper) :
    MessageDataSource.Remote {

    override fun getMessage(messageId: String, callback: SourceCallback<Message>) {
        HttpUtils.requestApi(
            METHOD_GET,
            "$BASE_URL$ENDPOINT_ROOMS/${prefsHelper.getRoomId()}$ENDPOINT_MESSAGES/$messageId",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    try {
                        callback.onSuccess(data.parseJsonObject())
                    } catch (e: JSONException) {
                        callback.onError(e)
                    }
                }

                override fun onError(e: Exception) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun getMessages(callback: SourceCallback<List<Message>>) {
        HttpUtils.requestApi(
            METHOD_GET,
            "$BASE_URL$ENDPOINT_ROOMS/${prefsHelper.getRoomId()}$ENDPOINT_MESSAGES$FORCE_MESSAGE",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    try {
                        val messages = data.parseJsonArray<Message>()
                            .filter { it.accountId == prefsHelper.getAccountId() && it.isPlan }
                        callback.onSuccess(messages)
                    } catch (e: JSONException) {
                        callback.onError(e)
                    }
                }

                override fun onError(e: Exception) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun sendMessage(message: String, callback: SourceCallback<String>) {
        HttpUtils.requestApi(
            METHOD_POST,
            "$BASE_URL$ENDPOINT_ROOMS/${prefsHelper.getRoomId()}$ENDPOINT_MESSAGES",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            mapOf(KEY_BODY to message),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    callback.onSuccess(data)
                }

                override fun onError(e: Exception) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun updateMessage(messageId: Int, content: String, callback: SourceCallback<String>) {
        HttpUtils.requestApi(
            METHOD_PUT,
            "$BASE_URL$ENDPOINT_ROOMS/${prefsHelper.getRoomId()}$ENDPOINT_MESSAGES/$messageId",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            mapOf(KEY_BODY to content),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    callback.onSuccess(data)
                }

                override fun onError(e: Exception) {
                    callback.onError(e)
                }
            }
        )
    }

    override fun deleteMessage(messageId: Int, callback: SourceCallback<String>) {
        HttpUtils.requestApi(
            METHOD_DELETE,
            "$BASE_URL$ENDPOINT_ROOMS/${prefsHelper.getRoomId()}$ENDPOINT_MESSAGES/$messageId",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    callback.onSuccess(data)
                }

                override fun onError(e: Exception) {
                    callback.onError(e)
                }
            }
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: MessageRemoteDataSource? = null

        fun getInstance(prefsHelper: PreferencesHelper) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MessageRemoteDataSource(prefsHelper).also { INSTANCE = it }
            }
    }
}
