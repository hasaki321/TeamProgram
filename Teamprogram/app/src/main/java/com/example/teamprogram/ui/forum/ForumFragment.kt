package com.example.teamprogram.ui.forum

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.system.ErrnoException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ForumPublishPage
import com.example.teamprogram.ForumUserLoginPage
import com.example.teamprogram.databinding.FragmentForumBinding
import com.example.teamprogram.ui.notifications.LoginDataBaseHelper
import java.io.*
import java.lang.reflect.InvocationTargetException

@SuppressLint("Range")
class ForumFragment :Fragment(), View.OnClickListener {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!



    private val DataList = ArrayList<ForumList>()
    private var adapter:ForumAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumBinding.inflate(inflater,container,false)
        val  root: View = binding.root

        val db = context?.let { ForumListDataHelper(it, "forumList", 1) }?.readableDatabase
        val cursor = db?.query("forumList", null,null, null, null, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // 遍历Cursor对象，取出数据
                    val name = cursor.getString(cursor.getColumnIndex("username"))
                    val title = cursor.getString(cursor.getColumnIndex("title"))
                    val content = cursor.getString(cursor.getColumnIndex("content"))
                    DataList.add(ForumList(name, title, content))
                } while (cursor.moveToNext())
            }
            Log.d("datalist size",DataList.size.toString())
            adapter = ForumAdapter(DataList)
        }

        val adapter = ForumAdapter(DataList)
        binding.recyclerviewForum.adapter = adapter
        binding.recyclerviewForum.layoutManager = LinearLayoutManager(context)

        //登录检测
        run{
            try {
                val strList = ArrayList<String>()
                val input = context?.openFileInput("userdata")
                Log.d("fetch user data", "tried")

                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        strList.add(it)
                    }
                }
                Toast.makeText(context, "Automatic login successfully!", Toast.LENGTH_SHORT).show()

            } catch (e: java.lang.Exception) {
                val intent = Intent(context, ForumUserLoginPage::class.java)
                startActivityForResult(intent, 1)
            }
        }

        //点击用户详情界面
        binding.forumUserButton.setOnClickListener(this)
        //发布新贴
        binding.forumAddButton.setOnClickListener(this)
        return root
    }
    override fun onClick(p0: View?) {
        when(p0) {
            binding.forumAddButton -> {

                try {
                    //进入发布内容界面
                    val input = context?.openFileInput("userdata")

                    val username = BufferedReader(InputStreamReader(input)).readLine()
                    val intent = Intent(context, ForumPublishPage::class.java)
                    intent.putExtra("username",username)
                    startActivityForResult(intent, 2)

                } catch (e: java.lang.Exception) {
                    //检测有无用户数据，没有就要输入用户数据
                    val intent = Intent(context, ForumUserLoginPage::class.java)
                    startActivityForResult(intent, 1)
                }
            }
            binding.forumUserButton -> {
                val intent = Intent(context, ForumUserLoginPage::class.java)
                startActivityForResult(intent, 1)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if(resultCode == RESULT_OK) {
                val returnusername = data?.getStringExtra("username")
                if (returnusername != null) {
                    Log.d("return name",returnusername)
                }
                val returnemail = data?.getStringExtra("email")
                val output = context?.openFileOutput("userdata", Context.MODE_PRIVATE)
                val writer = BufferedWriter(OutputStreamWriter(output))
                writer.use {
                    it.write(returnusername)
                    it.write("\n")
                    it.write(returnemail)
                }
            }
            2 -> if(resultCode == RESULT_OK) {
                val dbHelper = context?.let { ForumListDataHelper(it, "forumList", 1) }
                val db = dbHelper?.readableDatabase
                val cursor = db?.query("forumList", null,null, null, null, null, null, null)
                if (cursor != null) {
                    cursor.moveToLast()
                    val name = cursor.getString(cursor.getColumnIndex("username"))
                    val title = cursor.getString(cursor.getColumnIndex("title"))
                    val content = cursor.getString(cursor.getColumnIndex("content"))
                    DataList.add(ForumList(name, title, content))

                    adapter?.notifyItemInserted(cursor.count - 1)
                    binding.recyclerviewForum.scrollToPosition(cursor.count - 1)
                }
            }
        }
    }


}