package com.example.teamprogram.ui.notifications

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.teamprogram.ui.dashboard.HomeworkDataHelper

class AppDbHelper (val context: Context, name: String, version: Int) :
SQLiteOpenHelper(context, name, null, version) {
    private val createDb = "create table AppList (" +
            "id integer primary key autoincrement," +
            "name text," +
            "url text," +
            "image text)"

    val value1 = ContentValues().apply {
        put("name","搜索图书")
        put("url","http://www.lzuhrs.cn:3006/library")
        put("image","")
    }

    val value2 = ContentValues().apply {
        put("name","搜索图片来源")
        put("url","http://www.lzuhrs.cn:3007/image")
        put("image","")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createDb)
        db.insert("homeworklist",null,value1)
        db.insert("homeworklist",null,value2)
        Log.d("appDb","created")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}
