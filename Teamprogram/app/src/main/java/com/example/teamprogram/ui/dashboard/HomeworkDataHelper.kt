package com.example.teamprogram.ui.dashboard

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HomeworkDataHelper(context:Context, name:String, version:Int):
    SQLiteOpenHelper(context,name,null,version,) {

    private val createTable = "create table ${name} (" +
            "id integer primary key," +
            "title text," +
            "content text," +
            "time_left text)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}