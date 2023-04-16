package com.example.teamprogram

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.teamprogram.databinding.ActivityForumConentBinding
import com.example.teamprogram.databinding.ForumPublishPageBinding
import com.example.teamprogram.databinding.FragmentForumBinding
import com.example.teamprogram.ui.forum.*
import java.text.FieldPosition

class ForumConent : AppCompatActivity(), View.OnClickListener {

    private val DataList = ArrayList<ForumComment>()
    private var adapter:ForumContentAdapter ?= null

    private var _binding: ActivityForumConentBinding? = null
    private val binding get() = _binding!!

    lateinit var view:View
    lateinit var db:ForumContentDataHelper
    lateinit var cursor:Cursor
    lateinit var builder:StringBuilder

    companion object{
        fun actionStart(context: Context, position: Int, user: String, publisher:String,title: String, content:String){
            val intent = Intent(context, ForumConent::class.java)
            intent.putExtra("position",position)
            intent.putExtra("user",user)
            intent.putExtra("publisher", publisher)
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            context.startActivity(intent)
        }
    }

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_conent)
        supportActionBar?.hide()

        _binding = ActivityForumConentBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        binding.forumContentTitle.text = intent.getStringExtra("title")
        binding.forumContentUsername.text = intent.getStringExtra("publisher")
        binding.forumContentMain.text = intent.getStringExtra("content")

        builder = StringBuilder()
        builder.append(intent.getStringExtra("user"))
        builder.append(intent.getStringExtra("title"))
        builder.append("${intent.getIntExtra("position",0)}")


        db = ForumContentDataHelper(this, builder.toString(), 1)
        cursor = db.writableDatabase.query(builder.toString(), null,null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据
                val name = cursor.getString(cursor.getColumnIndex("username"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                DataList.add(ForumComment(name, content))
            } while (cursor.moveToNext())
        }

        adapter = ForumContentAdapter(DataList)
        binding.recyclerviewForumContent.adapter = adapter
        binding.recyclerviewForumContent.layoutManager = LinearLayoutManager(this)


        binding.forumContentPublishButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        setContentView(view)
        when(p0) {
            binding.forumContentPublishButton -> {
                val values = ContentValues().apply {
                    put("username", intent.getStringExtra("user"))
                    put("content",binding.forumContentEditText.text.toString())
                }
                db.writableDatabase.insert(builder.toString(),null,values)
                cursor = db.readableDatabase.query(builder.toString(), null,null, null, null, null, null, null)
                DataList.add(ForumComment(intent.getStringExtra("user"),binding.forumContentEditText.text.toString()))
                adapter?.notifyItemChanged(cursor.count - 1)
                binding.recyclerviewForumContent.scrollToPosition(cursor.count - 1)
            }
        }
    }
}