package com.edu.sun.oereminder.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.edu.sun.oereminder.data.model.*
import com.edu.sun.oereminder.data.source.local.dbutils.BaseSQLiteHelper
import com.edu.sun.oereminder.utils.SQLiteConst.DATABASE_NAME
import com.edu.sun.oereminder.utils.SQLiteConst.DATABASE_VERSION

class DatabaseHelper private constructor(context: Context) :
    BaseSQLiteHelper(context, DATABASE_NAME, version = DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase) {
        database.createTables(
            Account::class,
            Member::class,
            Message::class,
            Room::class,
            TimeRecord::class
        )
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTables(
            Account::class,
            Member::class,
            Message::class,
            Room::class,
            TimeRecord::class
        )
        onCreate(database)
    }

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DatabaseHelper(context)
            }
    }
}
