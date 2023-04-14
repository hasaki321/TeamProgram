package com.example.teamprogram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.teamprogram.databinding.ForumUserLoginPageBinding

class ForumUserLoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ForumUserLoginPageBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        binding.forumInputSubmit.setOnClickListener(){
            val username = binding.forumInputUsername.text
            val email = binding.forumInputEamil.text

            val intent = Intent()
            intent.putExtra("username",username)
            intent.putExtra("email",email)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val username = findViewById<EditText>(R.id.forum_input_username).text
        val email = findViewById<EditText>(R.id.forum_input_eamil).text

        val intent = Intent()
        intent.putExtra("username",username)
        intent.putExtra("email",email)
        setResult(RESULT_OK, intent)
        finish()
    }
}