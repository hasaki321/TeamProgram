package com.example.teamprogram.ui.home

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.teamprogram.databinding.FragmentHomeBinding
import com.example.teamprogram.ui.dashboard.HomeWorkContent
import com.example.teamprogram.ui.dashboard.HomeworkDataHelper
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var db:SQLiteDatabase
    lateinit var DataList:ArrayList<Calendar>
    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //创建空数据列表
        DataList = ArrayList<Calendar>()
        for(i in 0..48) {

            if (i%7 == 0){
                DataList.add(Calendar("Date","0", Calendar.Date, "${i/7 + 1}"))
            }else if (i in 8..13){
                DataList.add(Calendar("", "", Calendar.Today))
            }else{
                DataList.add(Calendar("", "", Calendar.Empty))
            }
        }
        //链接数据库
        db = HomeworkDataHelper(context!!,"homeworklist",1).readableDatabase
        val cursor = db.query("homeworkList", null,null, null, null, null, null, null)
        val calendar = java.util.Calendar.getInstance()
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"))



        //从数据库中取数据并判断剩余时间
        run{
            val _day = calendar.get(java.util.Calendar.DATE).toInt()
            val _month = calendar.get(java.util.Calendar.MONTH).toInt() + 1
            val _hour = calendar.get(java.util.Calendar.HOUR_OF_DAY).toInt() + 1

            val today_row = (_hour - 6) / 3 + 1
            val today_position = 7 + today_row
            DataList[today_position] = Calendar("", "", Calendar.Now)
            if (cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(cursor.getColumnIndex("title"))
                    val month = cursor.getInt(cursor.getColumnIndex("month"))
                    val day = cursor.getInt(cursor.getColumnIndex("day"))
                    val hour = cursor.getInt(cursor.getColumnIndex("hour"))

                    if (month == _month && day - _day <=5) {
                        var position = 0
                        var timeleft = 0
                        val col = (day - _day + 1) * 7
                        val row = (hour - 6) / 3 + 1
                        position = col + row

                        val day_dif = day - _day
                        val hour_dif = hour - _hour
                        timeleft = day_dif * 24 + hour_dif
                        DataList[position] = Calendar(title, "$timeleft h", Calendar.Object)
                    }
                } while (cursor.moveToNext())
            }
        }


        val recyclerView: RecyclerView = binding.recyclerviewCalendar
        val calendarList = DataList
        recyclerView.adapter = CalendarAdapter(calendarList)
        recyclerView.layoutManager = StaggeredGridLayoutManager(7,
            StaggeredGridLayoutManager.HORIZONTAL)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}