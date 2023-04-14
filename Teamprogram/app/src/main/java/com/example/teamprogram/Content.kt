package com.example.teamprogram

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.teamprogram.R

class Content : AppCompatActivity() {

    companion object{
        fun actionStart(context: Context,title:String,content:String){
            val intent = Intent(context, Content::class.java)
            intent.putExtra("title",title)
            intent.putExtra("content",content)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        supportActionBar?.hide()

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        val titleText = findViewById<TextView>(R.id.content_title)
        val contentText = findViewById<TextView>(R.id.content_content)

        titleText.text = title
        contentText.text = content

        val turn = findViewById<Button>(R.id.turn_to_web)
        turn.setOnClickListener(){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://erya.mooc.chaoxing.com/")
            startActivity(intent)
        }
    }
}