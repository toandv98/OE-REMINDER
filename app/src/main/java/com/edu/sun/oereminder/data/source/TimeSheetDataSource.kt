package com.edu.sun.oereminder.data.source

import com.edu.sun.oereminder.data.model.TimeRecord

interface TimeSheetDataSource {
    interface Local {

        fun getNextTimeRecords(): List<TimeRecord>

        fun getTimeRecords(from: Long, to: Long, isEdit: Boolean = true): List<TimeRecord>

        fun updateTimeRecord(timeRecord: TimeRecord)

        fun addTimeSheet(timeRecords: List<TimeRecord>)

        fun updateTimeSheet(timeRecords: List<TimeRecord>)
    }

    interface Remote
}
