package com.edu.sun.oereminder.data.source.local

import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.data.source.TimeSheetDataSource
import com.edu.sun.oereminder.data.source.local.dbutils.Predicate
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.BETWEEN
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.DESC
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.GREATER
import com.edu.sun.oereminder.utils.ColumnName.WORK_DATE

class TimeSheetLocalDataSource(private val dbHelper: DatabaseHelper) : TimeSheetDataSource.Local {

    override fun getNextTimeRecords() = dbHelper.get(
        TimeRecord::class,
        Predicate("$WORK_DATE$GREATER", System.currentTimeMillis().toString())
    )

    override fun getTimeRecords(from: Long, to: Long) = dbHelper.get(
        TimeRecord::class,
        Predicate("$WORK_DATE$BETWEEN", listOf(from.toString(), to.toString())),
        "$WORK_DATE$DESC"
    )

    override fun updateTimeRecord(timeRecord: TimeRecord) = dbHelper.update(timeRecord)

    override fun addTimeSheet(timeRecords: List<TimeRecord>) = dbHelper.insert(timeRecords)

    override fun updateTimeSheet(timeRecords: List<TimeRecord>) = dbHelper.update(timeRecords)

    companion object {
        @Volatile
        private var INSTANCE: TimeSheetDataSource.Local? = null

        fun getInstance(dbHelper: DatabaseHelper) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TimeSheetLocalDataSource(dbHelper).also { INSTANCE = it }
            }
    }
}
