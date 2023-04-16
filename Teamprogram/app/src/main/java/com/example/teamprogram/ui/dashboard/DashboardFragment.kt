package com.example.teamprogram.ui.dashboard

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.teamprogram.databinding.FragmentDashboardBinding
import com.example.teamprogram.ui.forum.ForumAdapter
import com.example.teamprogram.ui.forum.ForumComment
import com.example.teamprogram.ui.forum.ForumList
import com.example.teamprogram.ui.forum.ForumListDataHelper

@SuppressLint("Range")
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val DataList = ArrayList<HomeWorkContent>()
    private var adapter: ForumAdapter?= null

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


        db = ForumListDataHelper(context!!, "homeworkList", 1).readableDatabase!!
        val cursor = db.query("homeworkList", null,null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                val name = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val time_left = cursor.getString(cursor.getColumnIndex("time_left"))
                DataList.add(HomeWorkContent(name, content, time_left))
            } while (cursor.moveToNext())
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