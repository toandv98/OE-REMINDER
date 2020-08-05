package com.edu.sun.oereminder.data.source.remote

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.Account
import com.edu.sun.oereminder.data.model.Member
import com.edu.sun.oereminder.data.model.Room
import com.edu.sun.oereminder.data.source.UserDataSource
import com.edu.sun.oereminder.data.source.preferences.PreferencesHelper
import com.edu.sun.oereminder.utils.ApiEndPoint.BASE_URL
import com.edu.sun.oereminder.utils.ApiEndPoint.ENDPOINT_ME
import com.edu.sun.oereminder.utils.ApiEndPoint.ENDPOINT_MEMBERS
import com.edu.sun.oereminder.utils.ApiEndPoint.ENDPOINT_ROOMS
import com.edu.sun.oereminder.utils.NetConst.KEY_TOKEN_CHATWORK
import com.edu.sun.oereminder.utils.NetConst.METHOD_GET
import com.edu.sun.oereminder.utils.parseJsonArray
import com.edu.sun.oereminder.utils.parseJsonObject
import org.json.JSONException

class UserRemoteDataSource private constructor(private val prefsHelper: PreferencesHelper) :
    UserDataSource.Remote {

    override fun getProfile(callback: SourceCallback<Account>) {
        HttpUtils.requestApi(
            METHOD_GET,
            "$BASE_URL$ENDPOINT_ME",
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

    override fun getRooms(callback: SourceCallback<List<Room>>) {
        HttpUtils.requestApi(
            METHOD_GET,
            "$BASE_URL$ENDPOINT_ROOMS",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    try {
                        callback.onSuccess(data.parseJsonArray())
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

    override fun getMembers(callback: SourceCallback<List<Member>>) {
        HttpUtils.requestApi(
            METHOD_GET,
            "$BASE_URL$ENDPOINT_ROOMS/${prefsHelper.getRoomId()}$ENDPOINT_MEMBERS",
            mapOf(KEY_TOKEN_CHATWORK to prefsHelper.getApiToken()),
            object : SourceCallback<String> {

                override fun onSuccess(data: String) {
                    try {
                        callback.onSuccess(data.parseJsonArray())
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

    companion object {
        @Volatile
        private var INSTANCE: UserRemoteDataSource? = null

        fun getInstance(prefsHelper: PreferencesHelper) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRemoteDataSource(prefsHelper)
            }
    }
}
