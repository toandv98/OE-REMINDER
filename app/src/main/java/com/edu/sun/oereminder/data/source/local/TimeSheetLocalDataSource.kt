package com.edu.sun.oereminder.data.source.local

import com.edu.sun.oereminder.data.model.TimeRecord
import com.edu.sun.oereminder.data.source.TimeSheetDataSource
import com.edu.sun.oereminder.data.source.local.dbutils.Predicate
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.AND
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.ASC
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.BETWEEN
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.DESC
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.GREATER
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.NOT_EQUAL
import com.edu.sun.oereminder.utils.*
import com.edu.sun.oereminder.utils.ColumnName.PART_OF_DAY
import com.edu.sun.oereminder.utils.ColumnName.WORK_DATE
import com.edu.sun.oereminder.utils.WorkTime.OFF
import java.util.*

class TimeSheetLocalDataSource(private val dbHelper: DatabaseHelper) : TimeSheetDataSource.Local {

    init {
        val last = dbHelper.get(TimeRecord::class, orderBy = "$WORK_DATE$DESC", limit = 1)
        val lastMillis = last.firstOrNull()?.dateCalendar?.timeInMillis ?: 0
        dbHelper.run {
            if (lastMillis < firstMillisOfMonth()) {
                insert(newTimeSheet(false))
                insert(newTimeSheet(true))
            } else if (lastMillis <= lastMillisOfMonth()) {
                insert(newTimeSheet(true))
            }
        }
    }

    override fun getNextTimeRecords() = dbHelper.get(
        TimeRecord::class,
        Predicate("$WORK_DATE$GREATER", System.currentTimeMillis().toString())
    )

    override fun getTimeRecords(from: Long, to: Long, isEdit: Boolean) =
        dbHelper.get(
            kClass = TimeRecord::class,
            where = if (isEdit) {
                Predicate("$WORK_DATE$BETWEEN", listOf(from.toString(), to.toString()))
            } else {
                Predicate(
                    "$WORK_DATE$BETWEEN$AND$PART_OF_DAY$NOT_EQUAL",
                    listOf(from.toString(), to.toString(), OFF)
                )
            },
            orderBy = "$WORK_DATE${if (isEdit) ASC else DESC}"
        )

    override fun updateTimeRecord(timeRecord: TimeRecord) = dbHelper.update(timeRecord)

    override fun addTimeSheet(timeRecords: List<TimeRecord>) = dbHelper.insert(timeRecords)

    override fun updateTimeSheet(timeRecords: List<TimeRecord>) = dbHelper.update(timeRecords)

    private fun newTimeSheet(isNextMonth: Boolean) = mutableListOf<TimeRecord>().apply {
        val calendar =
            GregorianCalendar().apply { if (isNextMonth) plus(Calendar.MONTH, 1) }.firstDayOfMonth()
        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek in Calendar.TUESDAY..Calendar.FRIDAY) {
            for (i in Calendar.SUNDAY..Calendar.SATURDAY) {
                calendar.minus(Calendar.DATE, 1)
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                if (dayOfWeek == Calendar.MONDAY) break
            }
        }
        repeat(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek in Calendar.MONDAY..Calendar.FRIDAY) {
                add(TimeRecord(workDate = calendar.timeInMillis))
            }
            calendar.plus(Calendar.DAY_OF_MONTH, 1)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: TimeSheetDataSource.Local? = null

        fun getInstance(dbHelper: DatabaseHelper) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TimeSheetLocalDataSource(dbHelper).also { INSTANCE = it }
            }
    }
}
