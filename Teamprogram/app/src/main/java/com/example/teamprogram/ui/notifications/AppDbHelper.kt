package com.example.teamprogram.ui.notifications

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class AppDbHelper (val context: Context, name: String, version: Int) :
SQLiteOpenHelper(context, name, null, version) {
    private val createDb = "create table AppList (" +
            "id integer primary key autoincrement," +
            "name text," +
            "url text," +
            "image text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createDb)
        Log.d("appDb","created")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}
