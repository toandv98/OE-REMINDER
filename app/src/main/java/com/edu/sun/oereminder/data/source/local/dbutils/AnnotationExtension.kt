package com.edu.sun.oereminder.data.source.local.dbutils

import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

@Target(AnnotationTarget.CLASS)
annotation class Table(val tableName: String = "")

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(
    val columnName: String,
    val primaryKey: Boolean = false,
    val nonNull: Boolean = false,
    val autoIncrease: Boolean = false
)

fun KProperty1<*, *>.getColumn() = javaField?.annotations?.find { it is Column } as? Column

fun KProperty1<*, *>.isPrimaryKey() = getColumn()?.primaryKey ?: false

fun KProperty1<*, *>.isNonNull() = getColumn()?.nonNull ?: false

fun KProperty1<*, *>.isAutoIncrease() = getColumn()?.autoIncrease ?: false

fun KClass<*>.getTableName() = findAnnotation<Table>()?.tableName ?: ""

fun Any.getColumnValue(key: String) =
    this::class.memberProperties.find { it.getColumn()?.columnName == key }?.getter?.call(this)

fun KClass<*>.getColumnInfo() = HashMap<String, KProperty1<*, *>>().apply {
    this@getColumnInfo.memberProperties.forEach { info ->
        info.getColumn()?.let {
            this@apply[it.columnName] = info
        }
    }
}

fun KClass<*>.getSqlColumnType() = when (this) {
    String::class -> "TEXT"
    Boolean::class -> "BOOLEAN"
    Char::class -> "TEXT"
    Byte::class -> "INTEGER"
    Short::class -> "INTEGER"
    Int::class -> "INTEGER"
    Long::class -> "INTEGER"
    Float::class -> "REAL"
    Double::class -> "REAL"
    ByteArray::class -> "BLOB"
    BigDecimal::class -> "NUMERIC"
    else -> ""
}
