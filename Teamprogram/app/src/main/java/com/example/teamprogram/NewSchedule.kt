package com.example.teamprogram

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.teamprogram.databinding.ActivityForumConentBinding
import com.example.teamprogram.databinding.NewScheduleBinding
import com.example.teamprogram.ui.dashboard.HomeworkDataHelper
import com.example.teamprogram.ui.forum.ForumComment

class NewSchedule : AppCompatActivity(),View.OnClickListener {

    private var _binding: NewScheduleBinding? = null
    private val binding get() = _binding!!
    lateinit var view: View
    lateinit var db: HomeworkDataHelper
    lateinit var cursor: Cursor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_schedule)
        _binding = NewScheduleBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        db = HomeworkDataHelper(this,"homeworklist",1)
        cursor = db.readableDatabase.query("homeworkList", null,null, null, null, null, "day ASC,hour ASC,minute ASC", null)
        binding.addScheduleSubmit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        setContentView(view)
        when(p0) {
            binding.addScheduleSubmit -> {
                val values = ContentValues().apply {
                    put("title",binding.addScheduleTitle.text.toString())
                    put("content",binding.addScheduleContent.text.toString())
                    put("month",binding.addScheduleMonth.text.toString().toInt())
                    put("day",binding.addScheduleDay.text.toString().toInt())
                    put("hour",binding.addScheduleHour.text.toString().toInt())
                    put("minute",binding.addScheduleMinute.text.toString().toInt())
                }
                db.writableDatabase.insert("homeworklist",null,values)

                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}