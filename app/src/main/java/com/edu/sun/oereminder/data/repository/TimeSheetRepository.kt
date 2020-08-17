package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.TimeRecord

interface TimeSheetRepository {

    fun getNextTimeRecords(callback: SourceCallback<List<TimeRecord>>)

    fun getTimeRecords(
        from: Long,
        to: Long,
        isEdit: Boolean = true,
        callback: SourceCallback<List<TimeRecord>>
    )

    fun getTimeRecord(date: Long, callback: SourceCallback<TimeRecord>)

    fun updateTimeRecord(timeRecord: TimeRecord, callback: SourceCallback<Int>)

    fun addTimeSheet(timeRecords: List<TimeRecord>, callback: SourceCallback<Int>)

    fun updateTimeSheet(timeRecords: List<TimeRecord>, callback: SourceCallback<Int>)
}
