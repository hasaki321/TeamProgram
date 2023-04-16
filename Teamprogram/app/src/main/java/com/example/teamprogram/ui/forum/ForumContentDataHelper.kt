package com.example.teamprogram.ui.forum

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class ForumContentDataHelper (val context: Context, val name:String, val version: Int):
    SQLiteOpenHelper(context,name,null,version) {

    private val createContentList = "create table ${name} (" +
            "id integer primary key," +
            "username text," +
            "content text)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createContentList)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}