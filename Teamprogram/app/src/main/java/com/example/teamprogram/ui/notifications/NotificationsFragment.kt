package com.example.teamprogram.ui.notifications

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.teamprogram.MainActivity
import com.example.teamprogram.R
import com.example.teamprogram.databinding.FragmentNotificationsBinding
import com.example.teamprogram.ui.forum.ForumAdapter
import java.io.BufferedWriter
import java.io.OutputStreamWriter


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val AppList = ArrayList<App>()
    lateinit var adapter:AppAdapter
    companion object {
        var counter = 0
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appAdd.setOnClickListener(){
            showFloatingWindow()
        }
        initView()

        return root
    }

    fun showFloatingWindow(){
        val inflater = LayoutInflater.from(requireContext())
        val backgroundView = inflater.inflate(R.layout.background_dim, null)
        val rootView = requireActivity().window.decorView.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(backgroundView)
        val popupView = inflater.inflate(R.layout.app_float_window, null)

        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)

        val popupWindow = PopupWindow(
            popupView,
            width,
            height,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        popupWindow.setOnDismissListener {
            rootView.removeView(backgroundView)
        }
        // 获取输入框和按钮的引用
        val submit = popupView.findViewById<Button>(R.id.app_float_submit)
        submit.setOnClickListener(){
            val name = popupView.findViewById<EditText>(R.id.app_float_name).text.toString()
            val url = popupView.findViewById<EditText>(R.id.app_float_url).text.toString()

            insertDb(name,url,"")
            AppList.add(App(counter+1,name,url,""))
            adapter.notifyItemChanged(counter+1)
            popupWindow.dismiss()
        }
        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)

    }
    private fun insertDb(name:String,url:String,image_lc:String){
        val dbHelper = AppDbHelper(context!!,"App.db",1)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name",name)
            put("url",url)
            put("image",image_lc)
        }
        db.insert("AppList",null,values)
    }

    @SuppressLint("Range")
    private fun initView(){
        val dbHelper = AppDbHelper(context!!, "App.db", 1)
        val db = dbHelper.writableDatabase
        // 查询Book表中所有的数据
        val cursor = db.query("AppList", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val url = cursor.getString(cursor.getColumnIndex("url"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                counter += 1
                AppList.add(App(id,name,url,image))
            } while (cursor.moveToNext())
        }
        cursor.close()
        Log.d("list",AppList.toString())
        adapt()
    }
    private fun adapt(){
        adapter = AppAdapter(AppList)
        adapter.get_context(context!!)
        binding.appRecycleView.adapter = adapter
        binding.appRecycleView.layoutManager = StaggeredGridLayoutManager(4,
            StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

