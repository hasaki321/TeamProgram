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
            Toast.makeText(this, "fetch data successfully!", Toast.LENGTH_SHORT).show()
            binding.forumInputUsername.setText(strList[0])
            Log.d("user name",strList[0])
            Log.d("email",strList[1])
            binding.forumInputEmail.setText(strList[1])
        } catch (e: java.lang.Exception) {}

        binding.forumInputSubmit.setOnClickListener(){
            val username = binding.forumInputUsername.text.toString()
            val email = binding.forumInputEmail.text.toString()

            Log.d("user name fetch",username)

            val intent = Intent()
            intent.putExtra("username",username)
            intent.putExtra("email",email)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val username = findViewById<EditText>(R.id.forum_input_username).text.toString()
        val email = findViewById<EditText>(R.id.forum_input_email).text.toString()

        val intent = Intent()
        intent.putExtra("username",username)
        intent.putExtra("email",email)
        setResult(RESULT_OK, intent)
        finish()
    }
}