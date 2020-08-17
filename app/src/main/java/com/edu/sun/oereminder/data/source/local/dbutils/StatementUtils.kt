package com.edu.sun.oereminder.data.source.local.dbutils

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

object StatementConst {
    const val CREATE_TABLE = "CREATE TABLE "
    const val DROP_TABLE = "DROP TABLE "
    const val IF_NOT_EXIST = "IF NOT EXISTS "
    const val IF_EXISTS = "IF EXISTS "
    const val NOT_NULL = "NOT NULL "
    const val PRIMARY_KEY = "PRIMARY KEY "
    const val AUTO_INCREMENT = "AUTOINCREMENT "
    const val EQUAL = " =? "
    const val NOT_EQUAL = " !=? "
    const val GREATER = " >? "
    const val LESS = " <? "
    const val GREATER_OR_EQUAL = " >=? "
    const val LESS_OR_EQUAL = " <=? "
    const val BETWEEN = " between ? and ? "
    const val DESC = " desc "
    const val ASC = " asc "
    const val AND = " and "
}

data class DatabaseSchema(
    val tableName: String,
    val columnInfo: HashMap<String, KProperty1<*, *>>
) {
    constructor(entityClass: KClass<*>) : this(
        entityClass.getTableName(),
        entityClass.getColumnInfo()
    )
}

data class Predicate(
    val selection: String,
    val selectionArgs: List<String>
) {
    constructor(fieldName: String, value: String) : this(
        fieldName,
        listOf<String>(value)
    )

    val whereClause get() = selection

    val args get() = selectionArgs.toTypedArray()
}
