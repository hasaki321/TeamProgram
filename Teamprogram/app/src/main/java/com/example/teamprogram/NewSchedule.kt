package com.example.teamprogram

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.teamprogram.databinding.ActivityForumConentBinding
import com.example.teamprogram.databinding.NewScheduleBinding
import com.example.teamprogram.ui.dashboard.HomeworkDataHelper
import com.example.teamprogram.ui.forum.ForumComment

class NewSchedule : AppCompatActivity(),View.OnClickListener {

    companion object{
        fun actionStart(
            context:Context,
            id:Int
        ){
            val intent = Intent(context, NewSchedule::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    private var _binding: NewScheduleBinding? = null
    private val binding get() = _binding!!
    private var id = 0
    lateinit var view: View
    lateinit var db: HomeworkDataHelper
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_schedule)
        _binding = NewScheduleBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        db = HomeworkDataHelper(this,"homeworklist",1)
        id = intent.getIntExtra("id",0)
        if (id!=0){
            val cursor = db.readableDatabase.rawQuery("select * from HomeworkList where id = ?", arrayOf("$id"))
            cursor.moveToFirst()
            val title = cursor.getString(cursor.getColumnIndex("title"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val _time_left = cursor.getString(cursor.getColumnIndex("time"))
            cursor.close()

            val regex = "(.*?)-(.*?)-(.*?)T(.*?):(.*)".toRegex()
            val matchResult = regex.find(_time_left)
            binding.addScheduleMonth.setText(matchResult!!.groupValues[2])
            binding.addScheduleDay.setText(matchResult.groupValues[3])
            binding.addScheduleHour.setText(matchResult!!.groupValues[4])
            binding.addScheduleMinute.setText(matchResult.groupValues[5])
            binding.addScheduleTitle.setText(title)
            binding.addScheduleContent.setText(content)
        }

        binding.addScheduleSubmit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        setContentView(view)
        when(p0) {
            binding.addScheduleSubmit -> {
                val month = binding.addScheduleMonth.text.toString()
                val day = binding.addScheduleDay.text.toString()
                val hour = binding.addScheduleHour.text.toString()
                val min = binding.addScheduleMinute.text.toString()
                val time = "${2023}-${month}-${day}T${hour}:${min}"
                val values = ContentValues().apply {
                    put("title",binding.addScheduleTitle.text.toString())
                    put("content",binding.addScheduleContent.text.toString())
                    put("time",time)
                }
                if (id!=0){
                    db.writableDatabase.update("HomeworkList", values, "id = ?", arrayOf(id.toString()))
                }else{
                    db.writableDatabase.insert("HomeworkList",null,values)
                }

                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}