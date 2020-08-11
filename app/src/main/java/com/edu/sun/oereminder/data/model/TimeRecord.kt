package com.edu.sun.oereminder.data.model

import com.edu.sun.oereminder.data.source.local.dbutils.Column
import com.edu.sun.oereminder.data.source.local.dbutils.Table
import com.edu.sun.oereminder.utils.ColumnName.PART_OF_DAY
import com.edu.sun.oereminder.utils.ColumnName.RECORD_ID
import com.edu.sun.oereminder.utils.ColumnName.STATUS
import com.edu.sun.oereminder.utils.ColumnName.TIME_IN
import com.edu.sun.oereminder.utils.ColumnName.TIME_OUT
import com.edu.sun.oereminder.utils.ColumnName.WORK_DATE
import com.edu.sun.oereminder.utils.SQLiteConst.TABLE_NAME_TIME_RECORDS
import com.edu.sun.oereminder.utils.from
import java.util.*

@Table(tableName = TABLE_NAME_TIME_RECORDS)
data class TimeRecord(
    @Column(columnName = RECORD_ID, primaryKey = true, autoIncrease = true) val id: Long = 0,
    @Column(columnName = WORK_DATE) var workDate: Long = 0,
    @Column(columnName = TIME_IN) var timeIn: Long = 0,
    @Column(columnName = TIME_OUT) var timeOut: Long = 0,
    @Column(columnName = PART_OF_DAY) var partOfDay: String = "",
    @Column(columnName = STATUS) var status: String = ""
) {

    constructor(
        workCalendar: GregorianCalendar,
        partOfDay: String
    ) : this(
        workDate = workCalendar.timeInMillis,
        partOfDay = partOfDay
    )

    val dateCalendar get() = GregorianCalendar().from(workDate)

    val timeInCalendar get() = GregorianCalendar().from(timeIn)

    val timeOutCalendar get() = GregorianCalendar().from(timeOut)
}
