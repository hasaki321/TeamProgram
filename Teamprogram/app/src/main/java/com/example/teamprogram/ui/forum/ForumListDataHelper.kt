package com.example.teamprogram.ui.forum

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class ForumListDataHelper (val context: Context, val name:String, val version: Int):
    SQLiteOpenHelper(context,name,null,version) {

    private val createList = "create table ${name} (" +
            "id integer primary key  autoincrement," +
            "username text," +
            "title text," +
            "content text)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createList)
        Toast.makeText(context,"table created", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}