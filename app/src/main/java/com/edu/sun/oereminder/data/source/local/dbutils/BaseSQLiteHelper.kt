package com.edu.sun.oereminder.data.source.local.dbutils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.database.sqlite.SQLiteOpenHelper
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.AUTO_INCREMENT
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.CREATE_TABLE
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.DROP_TABLE
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.IF_EXISTS
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.IF_NOT_EXIST
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.NOT_NULL
import com.edu.sun.oereminder.data.source.local.dbutils.StatementConst.PRIMARY_KEY
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

abstract class BaseSQLiteHelper(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    protected fun SQLiteDatabase.createTables(vararg tableClasses: KClass<*>) {
        tableClasses.forEach { tableClass ->
            execSQL(getTableStatements(tableClass))
        }
    }

    protected fun SQLiteDatabase.dropTables(vararg tableClasses: KClass<*>) {
        tableClasses.forEach { tableClass ->
            execSQL("$DROP_TABLE$IF_EXISTS${tableClass.getTableName()}")
        }
    }

    fun replace(obj: Any) = when (obj) {
        is Collection<*> -> {
            obj.forEach { single ->
                single?.let { replaceObject(it) }
            }
        }
        else -> replaceObject(obj)
    }

    fun insert(obj: Any) = when (obj) {
        is Collection<*> -> {
            obj.forEach { single ->
                single?.let { insertObject(it) }
            }
        }
        else -> insertObject(obj)
    }

    fun update(obj: Any, where: Predicate) {
        if (obj !is Collection<*>) {
            updateObject(obj, where)
        }
    }

    fun update(obj: Any) = when (obj) {
        is Collection<*> -> {
            obj.forEach { single ->
                single?.let { updateObject(it) }
            }
        }
        else -> updateObject(obj)
    }

    fun <T : Any> delete(kClass: KClass<T>, where: Predicate? = null) {
        writableDatabase.delete(
            getValidEntity(kClass::class).tableName,
            where?.whereClause,
            where?.args
        )
        closeDatabase()
    }

    fun delete(obj: Any) = when (obj) {
        is Collection<*> -> {
            obj.forEach { single ->
                single?.let { deleteObject(it) }
            }
        }
        else -> deleteObject(obj)
    }

    private fun insertObject(obj: Any) {
        val (tableName, columnInfo) = getValidEntity(obj::class)
        val contentValues = ContentValues().apply {
            columnInfo.keys.forEach { key ->
                if (columnInfo[key]?.isAutoIncrease() == false) {
                    obj.getColumnValue(key)?.let { put(key, it) }
                }
            }
        }
        writableDatabase.insertWithOnConflict(
            tableName,
            null,
            contentValues,
            CONFLICT_IGNORE
        )
        closeDatabase()
    }

    private fun replaceObject(obj: Any) {
        val (tableName, columnInfo) = getValidEntity(obj::class)
        val contentValues = ContentValues().apply {
            columnInfo.keys.forEach { key ->
                if (columnInfo[key]?.isAutoIncrease() == false) {
                    obj.getColumnValue(key)?.let { put(key, it) }
                }
            }
        }
        writableDatabase.replace(
            tableName,
            null,
            contentValues
        )
        closeDatabase()
    }

    private fun updateObject(obj: Any, where: Predicate? = null) {
        val (tableName, columnInfo) = getValidEntity(obj::class)
        var clause: Predicate? = where
        val contentValues = ContentValues()
        columnInfo.keys.forEach { key ->
            obj.getColumnValue(key)?.let { value ->
                columnInfo[key]?.run {
                    if (isPrimaryKey() && clause == null) {
                        clause = getColumn()?.columnName?.let {
                            Predicate("$it=?", value.toString())
                        }
                        return@forEach
                    }
                }
                contentValues.put(key, value)
            }
        }
        writableDatabase.update(
            tableName,
            contentValues,
            clause?.whereClause,
            clause?.args
        )
        closeDatabase()
    }

    private fun deleteObject(obj: Any) {
        val (tableName, columnInfo) = getValidEntity(obj::class)
        var where: Predicate? = null
        columnInfo.keys.forEach { key ->
            columnInfo[key]?.run {
                obj.getColumnValue(key)?.let { value ->
                    if (isPrimaryKey()) {
                        where = getColumn()?.columnName?.let {
                            Predicate("$it=?", value.toString())
                        }
                    }
                    return@forEach
                }
            }
        }
        writableDatabase.delete(tableName, where?.whereClause, where?.args)
        closeDatabase()
    }

    fun <T : Any> get(
        kClass: KClass<T>,
        where: Predicate? = null,
        orderBy: String? = null,
        limit: Int? = null
    ): List<T> {
        val (tableName, columnInfo) = getValidEntity(kClass)
        val columnArray = columnInfo.keys.toTypedArray()
        val cursor = readableDatabase.query(
            tableName,
            columnArray,
            where?.whereClause,
            where?.args,
            null,
            null,
            orderBy,
            limit?.toString()
        )
        val result = getObjectsFromCursor(kClass, cursor)
        cursor.close()
        closeDatabase()
        return result
    }

    fun <T : Any> execRawSQL(objClass: KClass<T>? = null, sql: String): MutableList<T> {
        val result = when (objClass) {
            null -> {
                val cursor = writableDatabase.rawQuery(sql, null)
                cursor.close()
                mutableListOf()
            }
            else -> {
                val cursor = readableDatabase.rawQuery(sql, null)
                val results = getObjectsFromCursor(objClass, cursor)
                cursor.close()
                results
            }
        }
        closeDatabase()
        return result
    }

    private fun closeDatabase() {
        when {
            writableDatabase.isOpen -> {
                writableDatabase.close()
            }
            readableDatabase.isOpen -> {
                readableDatabase.close()
            }
        }
    }

    private fun getTableStatements(tableClass: KClass<*>) = StringBuilder().apply {
        val (tableName, columnInfo) = getValidEntity(tableClass)
        append("$CREATE_TABLE$IF_NOT_EXIST$tableName(")
        columnInfo.keys.forEachIndexed { index, key ->
            columnInfo[key]?.run {
                val sqlType = returnType.jvmErasure.getSqlColumnType()
                if (sqlType.isNotEmpty()) {
                    append("$key $sqlType ")
                    if (isPrimaryKey()) append(PRIMARY_KEY)
                    if (isAutoIncrease()) append(AUTO_INCREMENT)
                    if (isNonNull()) append(NOT_NULL)
                    if (index != columnInfo.size - 1) append(", ")
                }
            }
        }
        append(");")
    }.toString()

    private fun <T : Any> getObjectsFromCursor(objClass: KClass<T>, cursor: Cursor) =
        mutableListOf<T>().apply {
            while (cursor.moveToNext()) {
                val paramsMap = hashMapOf<KParameter, Any?>()
                objClass.primaryConstructor?.run {
                    objClass.memberProperties.forEach { property ->
                        if (property.getColumn()?.columnName?.isNotEmpty() == true) {
                            findParameterByName(property.name)?.let { kParams ->
                                paramsMap[kParams] = cursor.get(property)
                            }
                        }
                    }
                    add(callBy(paramsMap))
                }
            }
        }

    private fun getValidEntity(kClass: KClass<*>): DatabaseSchema {
        with(kClass) {
            when {
                !isData -> throw IllegalArgumentException("Invalid data class")
                getTableName().isEmpty() -> throw IllegalArgumentException("Table Name not found")
                getColumnInfo().isEmpty() -> throw IllegalArgumentException("Field not found")
                else -> return DatabaseSchema(kClass)
            }
        }
    }

    private fun Cursor.get(property: KProperty1<*, *>): Any? {
        property.getColumn()?.columnName.run {
            return when (property.returnType.jvmErasure) {
                String::class -> getString(getColumnIndex(this))
                Boolean::class -> getInt(getColumnIndex(this)) == 1
                Char::class -> getString(getColumnIndex(this))
                Byte::class -> getInt(getColumnIndex(this))
                Short::class -> getInt(getColumnIndex(this))
                Int::class -> getInt(getColumnIndex(this))
                Long::class -> getLong(getColumnIndex(this))
                Float::class -> getFloat(getColumnIndex(this))
                Double::class -> getDouble(getColumnIndex(this))
                ByteArray::class -> getBlob(getColumnIndex(this))
                BigDecimal::class -> BigDecimal(getDouble(getColumnIndex(this)))
                else -> null
            }
        }
    }

    private fun ContentValues.put(key: String, value: Any) {
        when (value) {
            is String -> this.put(key, value)
            is Boolean -> this.put(key, value)
            is Char -> this.put(key, value.toString())
            is Byte -> this.put(key, value)
            is Short -> this.put(key, value)
            is Int -> this.put(key, value)
            is Long -> this.put(key, value)
            is Float -> this.put(key, value)
            is Double -> this.put(key, value)
            is ByteArray -> this.put(key, value)
            is BigDecimal -> this.put(key, value.toDouble())
        }
    }
}
