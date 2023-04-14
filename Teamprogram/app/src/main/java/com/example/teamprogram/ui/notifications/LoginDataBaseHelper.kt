package com.example.teamprogram.ui.notifications

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class LoginDataBaseHelper(val context: Context,val name:String,val version: Int):
    SQLiteOpenHelper(context,name,null,version) {

    private val createLogin = "create table ${name} (" +
            "id integer primary key," +
            "dmkj_uname text," +
            "dmkj_pass text," +
            "moodle_uname text," +
            "moodle_pass text," +
            "xxt_uname text," +
            "xxt_pass text)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createLogin)
        Toast.makeText(context,"table created",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}