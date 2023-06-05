package com.example.teamprogram.ui.dashboard

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.NewSchedule
import com.example.teamprogram.databinding.FragmentDashboardBinding
import com.example.teamprogram.ui.notifications.App
import com.example.teamprogram.ui.notifications.NotificationsFragment
import org.json.JSONArray
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("Range")
@RequiresApi(Build.VERSION_CODES.O)
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastManager = LocalBroadcastManager.getInstance(requireActivity())
        val intentFilter = IntentFilter().apply { addAction(Intent.ACTION_TIME_TICK) }
        timeChangeReceiver = TimeChangeReceiver()
        context?.registerReceiver(timeChangeReceiver, intentFilter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getList()
        adapter = HomeworkAdapter(DataList)
        adapter.context = context!!
        recyclerView = binding.recyclerviewDashboard
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        binding.addNewSchedule.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context?.unregisterReceiver(timeChangeReceiver)
        _binding = null
    }


    private fun getList(){
        DataList = ArrayList<HomeWorkContent>()
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("HW_data", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("jsData", "")
        Log.d("hw:",jsonString.toString())
        try {
            // 获取SharedPreferences实例
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val course_name = jsonObject.getString("course_name")
                val hw_name = jsonObject.getString("hw_name")
                val end_time = jsonObject.getString("end_time").replace(" ","T")
                val content = jsonObject.getString("content")
                val time_left = get_remain_time(end_time)
                if (time_left>0){
                    DataList.add(HomeWorkContent(course_name,hw_name,content,time_left,HomeWorkContent.HOMEWORK))
                    Log.d("add","true")
                }else{
                    Log.d("add","false")
                }
            }
        }catch (e:java.lang.Exception){
            Toast.makeText(context,jsonString,Toast.LENGTH_SHORT).show()
        }

        db = HomeworkDataHelper(context!!,"homeworklist",1).readableDatabase
        cursor = db.query("HomeworkList", null,null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val _time_left = cursor.getString(cursor.getColumnIndex("time"))
                val time_left = get_remain_time(_time_left)
                DataList.add(HomeWorkContent("",title,content,time_left,HomeWorkContent.CREATE,id))
            } while (cursor.moveToNext())
        }
        DataList.sortBy  { it.time_left }
        try{
            val cloest_time = DataList[0].time_left
            binding.closestDeadline.setText("${cloest_time/60/24}D ${cloest_time/60%24}H ${cloest_time%60}M")
        }catch (_:Exception){ }
    }

    private fun get_remain_time(time:String): Long {
        val zoneId = ZoneId.of("Asia/Shanghai")
        val date1 = LocalDateTime.parse(time)
        val date2 = LocalDateTime.now(zoneId)
        val duration = Duration.between(date2, date1)
        val minutes = duration.toMinutes()
        Log.d("now",date2.toString())
        Log.d("end",date1.toString())
        return minutes
    }

    inner class TimeChangeReceiver:BroadcastReceiver(){
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(p0: Context?, p1: Intent?) {
            getList()
            adapter = HomeworkAdapter(DataList)
            adapter.context = context!!
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
                getList()
                adapter = HomeworkAdapter(DataList)
                adapter.context = context!!
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
        }
    }
}