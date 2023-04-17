package com.example.teamprogram.ui.dashboard

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.databinding.FragmentDashboardBinding
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("Range")
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val DataList = ArrayList<HomeWorkContent>()

    lateinit var db:SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        db = HomeworkDataHelper(context!!,"homeworklist",1).readableDatabase
        val cursor = db.query("homeworkList", null,null, null, null, null, "day ASC,hour ASC,minute ASC", null)
        val calendar = java.util.Calendar.getInstance()
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"))

        //从数据库中取数据并判断剩余时间
        run{
            val _day = calendar.get(java.util.Calendar.DATE).toInt()
            val _month = calendar.get(java.util.Calendar.MONTH).toInt() + 1
            val _hour = calendar.get(java.util.Calendar.HOUR_OF_DAY).toInt() + 1
            if (cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(cursor.getColumnIndex("title"))
                    val content = cursor.getString(cursor.getColumnIndex("content"))
                    val month = cursor.getInt(cursor.getColumnIndex("month"))
                    val day = cursor.getInt(cursor.getColumnIndex("day"))
                    val hour = cursor.getInt(cursor.getColumnIndex("hour"))
                    val minute = cursor.getInt(cursor.getColumnIndex("minute"))

                    var timeleft = 0

                    if (month == _month) {
                        val day_dif = day - _day
                        val hour_dif = hour - _hour
                        timeleft = day_dif * 24 + hour_dif
                    }
                    DataList.add(HomeWorkContent(title,content, timeleft.toString(),minute))
                } while (cursor.moveToNext())
            }
        }

        val recyclerView: RecyclerView = binding.recyclerviewDashboard

        val homeworkList = DataList

        recyclerView.adapter = HomeworkAdapter(homeworkList)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}