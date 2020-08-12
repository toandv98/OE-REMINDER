package com.edu.sun.oereminder.data.repository

import com.edu.sun.oereminder.data.SourceCallback
import com.edu.sun.oereminder.data.model.TimeRecord

interface TimeSheetRepository {

    fun getNextTimeRecords(callback: SourceCallback<List<TimeRecord>>)

    fun getTimeRecords(from: Long, to: Long, callback: SourceCallback<List<TimeRecord>>)

    fun getTimeRecord(date: Long, callback: SourceCallback<TimeRecord>)

    fun updateTimeRecord(timeRecord: TimeRecord)

    fun addTimeSheet(timeRecords: List<TimeRecord>)

    fun updateTimeSheet(timeRecords: List<TimeRecord>)
}
