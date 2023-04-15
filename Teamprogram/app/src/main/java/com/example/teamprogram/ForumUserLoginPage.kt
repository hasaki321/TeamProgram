package com.example.teamprogram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamprogram.databinding.ForumUserLoginPageBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class ForumUserLoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ForumUserLoginPageBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        try {
            val strList = ArrayList<String>()
            val input = this?.openFileInput("userdata")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    strList.add(it)
                }
            }
            binding.forumInputUsername.setText(strList[0])
            binding.forumInputEmail.setText(strList[1])
        } catch (e: java.lang.Exception) {}

        binding.forumInputSubmit.setOnClickListener(){
            val username = binding.forumInputUsername.text.toString()
            val email = binding.forumInputEmail.text.toString()

            val intent = Intent()
            intent.putExtra("username",username)
            intent.putExtra("email",email)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}