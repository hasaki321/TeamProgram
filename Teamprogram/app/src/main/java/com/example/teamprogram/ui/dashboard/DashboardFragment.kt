package com.example.teamprogram.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.NewSchedule
import com.example.teamprogram.databinding.FragmentDashboardBinding
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("Range")
class DashboardFragment : Fragment(),View.OnClickListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var DataList = ArrayList<HomeWorkContent>()

    lateinit var db:SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var timeChangeReceiver: TimeChangeReceiver
    lateinit var broadcastManager:LocalBroadcastManager
    lateinit var adapter: HomeworkAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = HomeworkDataHelper(context!!,"homeworklist",1).readableDatabase
        cursor = db.query("homeworkList", null,null, null, null, null, "day ASC,hour ASC,minute ASC", null)

        //注册广播监听时间
        broadcastManager = getActivity()?.let { LocalBroadcastManager.getInstance(it) }!!
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        context!!.registerReceiver(timeChangeReceiver, intentFilter)

        //创建作业列表
        getList()
        adapter = HomeworkAdapter(DataList)
        recyclerView = binding.recyclerviewDashboard
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        //增加新日程
        binding.addNewSchedule.setOnClickListener(this)
        return root
    }

    private fun getList(){
        DataList = ArrayList<HomeWorkContent>()
        val calendar = java.util.Calendar.getInstance()
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
        val _day = calendar.get(java.util.Calendar.DATE).toInt()
        val _month = calendar.get(java.util.Calendar.MONTH).toInt() + 1
        val _hour = calendar.get(java.util.Calendar.HOUR_OF_DAY).toInt() + 1
        val _minute = calendar.get(java.util.Calendar.MINUTE).toInt()
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
                    val min_dif = minute - _minute
                    timeleft = day_dif * 24 * 60 + hour_dif * 60 + min_dif
                }
                DataList.add(HomeWorkContent(title,content, (timeleft/60).toString(),timeleft%60))
            } while (cursor.moveToNext())
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        broadcastManager.unregisterReceiver(timeChangeReceiver)
    }

    inner class TimeChangeReceiver:BroadcastReceiver(){
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(p0: Context?, p1: Intent?) {
            getList()
            adapter = HomeworkAdapter(DataList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onClick(p0: View?) {
        when(p0){
            binding.addNewSchedule -> {
                val intent = Intent(context,NewSchedule::class.java)
                startActivityForResult(intent,1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 -> {
                db = HomeworkDataHelper(context!!,"homeworklist",1).readableDatabase
                cursor = db.query("homeworkList", null,null, null, null, null, "day ASC,hour ASC,minute ASC", null)
                getList()
            }
        }
    }
}