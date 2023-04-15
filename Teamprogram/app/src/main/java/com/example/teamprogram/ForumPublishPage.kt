package com.example.teamprogram

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.teamprogram.databinding.ForumPublishPageBinding
import com.example.teamprogram.ui.forum.ForumListDataHelper
import java.io.BufferedReader
import java.io.InputStreamReader


class ForumPublishPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forum_publish_page)

        val binding = ForumPublishPageBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        binding.forumPublishSubmit.setOnClickListener(){
            val username = intent.getStringExtra("username")
            val name = "forumList"
            val dbHelper = this.let { ForumListDataHelper(it, name, 1) }
            val db = dbHelper?.writableDatabase

            val values = ContentValues().apply {
                put("username", username)
                put("title", binding.forumPublishTitle.text.toString())
                put("content", binding.forumPublishContent.text.toString())
            }

            db?.insert(name,null, values)

            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}