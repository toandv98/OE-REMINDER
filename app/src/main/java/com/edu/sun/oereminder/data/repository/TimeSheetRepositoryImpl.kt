package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.AppExecutors
import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.data.source.TimeSheetDataSource
import com.edu.sun.oereminder.utils.firstMillisOfDay
import com.edu.sun.oereminder.utils.from
import com.edu.sun.oereminder.utils.lastMillisOfDay
import java.util.*

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

    override fun getTimeRecords(
        from: Long,
        to: Long,
        callback: SourceCallback<List<TimeRecord>>
    ) {
        with(appExecutors) {
            diskIO.execute {
                val timeRecords = local.getTimeRecords(from, to)
                mainThread.execute { callback.onSuccess(timeRecords) }
            }
        }
    }

    override fun getTimeRecord(date: Long, callback: SourceCallback<TimeRecord>) {
        with(appExecutors) {
            diskIO.execute {
                val calendar = GregorianCalendar().from(date)
                val timeRecords =
                    local.getTimeRecords(calendar.firstMillisOfDay(), calendar.lastMillisOfDay())
                        .firstOrNull()
                mainThread.execute {
                    timeRecords?.let(callback::onSuccess) ?: callback.onError(Exception())
                }
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
            INSTANCE ?: TimeSheetRepositoryImpl(local, appExecutors).also { INSTANCE = it }
        }
    }
}
