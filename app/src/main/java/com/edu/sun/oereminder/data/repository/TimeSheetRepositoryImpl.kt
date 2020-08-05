package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.AppExecutors
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.data.source.TimeSheetDataSource

class TimeSheetRepositoryImpl(
    private val local: TimeSheetDataSource.Local,
    private val appExecutors: AppExecutors
) : TimeSheetRepository {

    override fun getNextTimeRecords(callback: SourceCallback<List<TimeRecord>>) {
        with(appExecutors) {
            diskIO.execute {
                val timeRecords = local.getNextTimeRecords()
                mainThread.execute { callback.onSuccess(timeRecords) }
            }
        }
    }

    override fun getCheckedTimeRecords(
        from: Long,
        to: Long,
        callback: SourceCallback<List<TimeRecord>>
    ) {
        with(appExecutors) {
            diskIO.execute {
                val timeRecords = local.getCheckedTimeRecords(from, to)
                mainThread.execute { callback.onSuccess(timeRecords) }
            }
        }
    }

    override fun updateTimeRecord(timeRecord: TimeRecord) {
        appExecutors.diskIO.execute {
            local.updateTimeRecord(timeRecord)
        }
    }

    override fun addTimeSheet(timeRecords: List<TimeRecord>) {
        appExecutors.diskIO.execute {
            local.addTimeSheet(timeRecords)
        }
    }

    override fun updateTimeSheet(timeRecords: List<TimeRecord>) {
        appExecutors.diskIO.execute {
            local.updateTimeSheet(timeRecords)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TimeSheetRepository? = null

        fun getInstance(
            local: TimeSheetDataSource.Local,
            appExecutors: AppExecutors
        ) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: TimeSheetRepositoryImpl(local, appExecutors)
        }
    }
}
