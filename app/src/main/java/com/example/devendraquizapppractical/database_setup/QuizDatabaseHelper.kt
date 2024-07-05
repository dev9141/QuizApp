package com.example.devendraquizapppractical.database_setup

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuizDatabaseHelper(mContext: Context): SQLiteOpenHelper(mContext, databaseName, null, 1) {
    companion object {
        const val databaseName = "QuizDatabase.db"
        private var dbHelper: QuizDatabaseHelper? = null

        var count:Int = 0

        fun getInstance(context: Context): QuizDatabaseHelper {
            if (dbHelper == null) {
                dbHelper = QuizDatabaseHelper(context)
            }
            return dbHelper!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbRepo.CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}