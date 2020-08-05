package com.edu.sun.oereminder.utils

import android.content.Context
import com.edu.sun.oereminder.AppExecutors
import com.edu.sun.oereminder.data.repository.MessageRepositoryImpl
import com.edu.sun.oereminder.data.repository.TimeSheetRepositoryImpl
import com.edu.sun.oereminder.data.repository.UserRepositoryImpl
import com.edu.sun.oereminder.data.source.local.DatabaseHelper
import com.edu.sun.oereminder.data.source.local.MessageLocalDataSource
import com.edu.sun.oereminder.data.source.local.TimeSheetLocalDataSource
import com.edu.sun.oereminder.data.source.local.UserLocalDataSource
import com.edu.sun.oereminder.data.source.preferences.PreferencesHelperImpl
import com.edu.sun.oereminder.data.source.remote.MessageRemoteDataSource
import com.edu.sun.oereminder.data.source.remote.UserRemoteDataSource

object InjectorUtils {

    fun getMessageRepository(context: Context) =
        MessageRepositoryImpl.getInstance(
            MessageLocalDataSource.getInstance(DatabaseHelper.getInstance(context)),
            MessageRemoteDataSource.getInstance(PreferencesHelperImpl.getInstance(context)),
            AppExecutors.getInstance()
        )

    fun getUserRepository(context: Context) =
        UserRepositoryImpl.getInstance(
            UserLocalDataSource.getInstance(DatabaseHelper.getInstance(context)),
            UserRemoteDataSource.getInstance(PreferencesHelperImpl.getInstance(context)),
            AppExecutors.getInstance()
        )

    fun getTimeSheetRepository(context: Context) =
        TimeSheetRepositoryImpl.getInstance(
            TimeSheetLocalDataSource.getInstance(DatabaseHelper.getInstance(context)),
            AppExecutors.getInstance()
        )
}
