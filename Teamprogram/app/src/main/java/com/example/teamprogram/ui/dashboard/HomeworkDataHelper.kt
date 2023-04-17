package com.example.teamprogram.ui.dashboard

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HomeworkDataHelper(context:Context, name:String, version:Int):
    SQLiteOpenHelper(context,name,null,version,) {

    private val createTable = "create table ${name} (" +
            "id integer primary key," +
            "title text," +
            "content text," +
            "month integer," +
            "day integer," +
            "hour integer," +
            "minute integer)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createTable)
        val values1 = ContentValues().apply {
            put("title", "Math")
            put("content", "page 236")
            put("month", 4)
            put("day", 19)
            put("hour", 18)
            put("minute", 30)
        }
        val values2 = ContentValues().apply {
            put("title", "毛概")
            put("content", "抄写诗词")
            put("month", 4)
            put("day", 20)
            put("hour", 11)
            put("minute", 34)
        }
        val values3 = ContentValues().apply {
            put("title", "English")
            put("content", "page 125")
            put("month", 4)
            put("day", 20)
            put("hour", 15)
            put("minute", 20)
        }
        val values4 = ContentValues().apply {
            put("title", "English")
            put("content", "page 125")
            put("month", 4)
            put("day", 22)
            put("hour", 15)
            put("minute", 20)
        }
        val values5 = ContentValues().apply {
            put("title", "Math")
            put("content", "11.3节")
            put("month", 4)
            put("day", 21)
            put("hour", 8)
            put("minute", 1)
        }
        val values6 = ContentValues().apply {
            put("title", "Introduction to Information Systems")
            put("content", "第四章测试题")
            put("month", 4)
            put("day", 24)
            put("hour", 12)
            put("minute", 11)
        }
        val values7 = ContentValues().apply {
            put("title", "Introduction to Information Systems")
            put("content", "第二章测试题")
            put("month", 4)
            put("day", 22)
            put("hour", 16)
            put("minute", 7)
        }
        val values8 = ContentValues().apply {
            put("title", "军事理论")
            put("content", "简答题")
            put("month", 4)
            put("day", 21)
            put("hour", 14)
            put("minute", 7)
        }
        val values9 = ContentValues().apply {
            put("title", "马原")
            put("content", "赏析歌词")
            put("month", 4)
            put("day", 22)
            put("hour", 20)
            put("minute", 38)
        }
        val values10 = ContentValues().apply {
            put("title", "English")
            put("content", "听说1")
            put("month", 4)
            put("day", 25)
            put("hour", 21)
            put("minute", 3)
        }
        val values11 = ContentValues().apply {
            put("title", "English")
            put("content", "写作2")
            put("month", 4)
            put("day", 23)
            put("hour", 22)
            put("minute", 47)
        }
        val values12 = ContentValues().apply {
            put("title", "计算机编程")
            put("content", "第五章课后题")
            put("month", 4)
            put("day", 22)
            put("hour", 16)
            put("minute", 19)
        }
        val values13 = ContentValues().apply {
            put("title", "计算机编程")
            put("content", "第六章课后题")
            put("month", 4)
            put("day", 22)
            put("hour", 13)
            put("minute", 54)
        }
        val values14 = ContentValues().apply {
            put("title", "计算机编程")
            put("content", "第七章课后题")
            put("month", 4)
            put("day", 22)
            put("hour", 14)
            put("minute", 59)
        }

        p0?.apply {
            insert("homeworklist",null,values1)
            insert("homeworklist",null,values2)
            insert("homeworklist",null,values3)
            insert("homeworklist",null,values4)
            insert("homeworklist",null,values5)
            insert("homeworklist",null,values6)
            insert("homeworklist",null,values7)
            insert("homeworklist",null,values8)
            insert("homeworklist",null,values9)
            insert("homeworklist",null,values10)
            insert("homeworklist",null,values11)
            insert("homeworklist",null,values12)
            insert("homeworklist",null,values13)
            insert("homeworklist",null,values14)
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}