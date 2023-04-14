package com.example.teamprogram.ui.notifications

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class LoginDataBase(
    val context: Context, name:String, version: Int): SQLiteOpenHelper(context,name,null,version) {

    private val createLogin = "create table ${name}(" +
            "id integer primary key autoincrement," +
            "dmkj_uname text," +
            "dmkj_pass text," +
            "moodle_uname text," +
            "moodle_pass text," +
            "xxt_uname text," +
            "xxt_pass text)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createLogin)
        Toast.makeText(context,"Created succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}

//val dmkj_uname:String,val dmkj_pass:String,val moodle_uname:String,val moodle_pass:String,val xxt_uname:String,val xxt_pass:String,
//dmkj_uname,dmkj_pass,moodle_uname,moodle_pass,xxt_uname,xxt_pass