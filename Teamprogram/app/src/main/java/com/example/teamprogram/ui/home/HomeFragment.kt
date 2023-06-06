package com.example.teamprogram.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.media.audiofx.BassBoost
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.teamprogram.MyAsyncTask
import com.example.teamprogram.R
import com.example.teamprogram.databinding.FragmentHomeBinding
import com.example.teamprogram.ui.dashboard.HomeWorkContent
import com.example.teamprogram.ui.dashboard.HomeworkDataHelper
import org.json.JSONArray
import java.io.*
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

    lateinit var floatingView:View
    lateinit var rootView:View
    lateinit var DataList:ArrayList<Calendar>
    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        floatingView = inflater.inflate(R.layout.float_window, container, false)
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

        try {
            preference(position_now)
        }catch (_:java.lang.Exception){}

        val recyclerView: RecyclerView = binding.recyclerviewCalendar
        val calendarList = DataList
        recyclerView.adapter = CalendarAdapter(calendarList)
        recyclerView.layoutManager = StaggeredGridLayoutManager(7,
            StaggeredGridLayoutManager.HORIZONTAL)

        binding.navView.setNavigationItemSelectedListener {it->
            when(it.itemId) {
                R.id.xxt_account -> {
                    showFloatingWindow("学习通")
                }
                R.id.dmkj_account -> {
                    showFloatingWindow("到梦空间")
                }
                R.id.moodle_account -> {
                    showFloatingWindow("Moodle")
                }
            }
            true
        }
        binding.calendarCallSide.setOnClickListener(){
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun showFloatingWindow(type:String) {
        val inflater = LayoutInflater.from(requireContext())
        val backgroundView = inflater.inflate(R.layout.background_dim, null)
        val rootView = requireActivity().window.decorView.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(backgroundView)
        val popupView = inflater.inflate(R.layout.float_window, null)

        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)

        val popupWindow = PopupWindow(
            popupView,
            width,
            height,
            true
        )

        // 设置悬浮窗口的背景
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // 在关闭悬浮窗口时移除覆盖层
        popupWindow.setOnDismissListener {
            rootView.removeView(backgroundView)
        }

        // 获取输入框和按钮的引用
        val etUsername = popupView.findViewById<EditText>(R.id.account_pop_username)
        val etPassword = popupView.findViewById<EditText>(R.id.account_pop_password)
        val btnSubmit = popupView.findViewById<Button>(R.id.account_pop_submit)
        val checkBox = popupView.findViewById<CheckBox>(R.id.account_pop_checkbox)

        popupView.findViewById<TextView>(R.id.account_pop_title).text = "${type} 账号"

        try {
            val checked = load("${type}check")[0]
            if (checked == "true"){
                checkBox.isChecked = true
                try {
                    val content = load(type)
                    etUsername.setText(content[0])
                    etPassword.setText(content[1])
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }catch (_: java.lang.Exception){
            val output = context!!.openFileOutput("${type}check", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.write("false")
        }

        // 设置提交按钮的点击事件
        btnSubmit.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (checkBox.isChecked){
                val output = context!!.openFileOutput("${type}check", Context.MODE_PRIVATE)
                val writer = BufferedWriter(OutputStreamWriter(output))
                writer.use {
                    it.write("true")
                save(type,username,password)
                }
            }

            MyAsyncTask().apply {
                get_context(context!!)
                execute()
            }

            popupWindow.dismiss()
        }

        // 设置悬浮窗口的位置和动画效果
        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)
    }

    private fun preference(position_now:Int){
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
    }
    private fun load(type: String): kotlin.collections.ArrayList<String> {
        val content = kotlin.collections.ArrayList<String>()
        val input = context!!.openFileInput(type)
        val reader = BufferedReader(InputStreamReader(input))
        reader.use {
            reader.forEachLine {
                content.add(it)
            }
        }
        return content
    }

    private fun save(type:String,uname:String,pwd:String){
        try {
            val output = context!!.openFileOutput(type, Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(uname)
                it.write("\n")
                it.write(pwd)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

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