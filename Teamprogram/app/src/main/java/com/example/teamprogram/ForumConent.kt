package com.example.teamprogram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ui.forum.ForumAdapter
import com.example.teamprogram.ui.forum.ForumViewModel

class ForumConent : AppCompatActivity() {

    companion object{
        fun actionStart(context: Context, title:String, content: String, number: Int){
            val intent = Intent(context, ForumConent::class.java)
            intent.putExtra("title",title)
            intent.putExtra("content",content)
            intent.putExtra("number", number)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_conent)
        supportActionBar?.hide()

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val number = intent.getIntExtra("number",0)

        val inputText = findViewById<EditText>(R.id.forum_content_edittext)
        val button = findViewById<Button>(R.id.forum_content_publish_button)
        val titleText = findViewById<TextView>(R.id.forum_content_title)
        val contentText = findViewById<TextView>(R.id.forum_content_content)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_forumContent)

        val forumViewModel = ViewModelProvider(this).get(ForumViewModel::class.java)
        Log.d("list size","${forumViewModel.data.size}")
        val adapter = ForumContentAdapter(forumViewModel.data[number].userList,forumViewModel.data[number].contentList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        titleText.text = title
        contentText.text = content

        button.setOnClickListener(){
            val username = "new Input user"
            val content = inputText.text.toString()
            if (content.isNotEmpty()) {
                forumViewModel.data[number].userList.add(username)
                forumViewModel.data[number].contentList.add(content)
                adapter?.notifyItemInserted(forumViewModel.data[number].userList.size - 1)
                recyclerView.scrollToPosition(forumViewModel.data[number].userList.size - 1)
                inputText.setText("")
            }
        }
    }
}