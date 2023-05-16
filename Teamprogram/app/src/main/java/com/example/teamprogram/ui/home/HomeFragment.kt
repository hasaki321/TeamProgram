package com.example.teamprogram.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.teamprogram.databinding.FragmentHomeBinding
import com.example.teamprogram.ui.dashboard.HomeWorkContent
import com.example.teamprogram.ui.dashboard.HomeworkDataHelper
import org.json.JSONArray
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
@RequiresApi(Build.VERSION_CODES.O)
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
        return root
    }
    override fun onResume() {
        super.onResume()
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
        val zoneId = ZoneId.of("Asia/Shanghai")
        val now = LocalDateTime.now(zoneId).hour
        Log.d("hour","${now}")
        val position_now = (now - 6) / 3 + 7 + 1
        if (now in 6..24){
            DataList[position_now] = Calendar("", "", Calendar.Now)
        }


        val sharedPreference = activity!!.getSharedPreferences("HW_data", Context.MODE_PRIVATE)
        val jsonString = sharedPreference.getString("jsData", "")
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val hw_name = jsonObject.getString("hw_name")
            val end_time = jsonObject.getString("end_time").replace(" ","T")
            val time_left = get_remain_time(end_time)
            Log.d("time","${time_left}")
            Log.d("position_now","${position_now}")
            val position = position_now + (time_left / 24) * 7 + (time_left % 24) / 3
            val position_ = position.toInt()
            if (position_ in 0..48){
                DataList[position_] = Calendar(hw_name,"${time_left}",Calendar.Object)
            }
        }

        val recyclerView: RecyclerView = binding.recyclerviewCalendar
        val calendarList = DataList
        recyclerView.adapter = CalendarAdapter(calendarList)
        recyclerView.layoutManager = StaggeredGridLayoutManager(7,
            StaggeredGridLayoutManager.HORIZONTAL)
    }


    private fun get_remain_time(time:String): Long {
        val zoneId = ZoneId.of("Asia/Shanghai")
        val date1 = LocalDateTime.parse(time)
        val date2 = LocalDateTime.now(zoneId)
        val duration = Duration.between(date2, date1)
        val hours = duration.toHours()
        return hours
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}