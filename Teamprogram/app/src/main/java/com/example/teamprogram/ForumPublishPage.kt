package com.example.teamprogram

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.teamprogram.databinding.ForumPublishPageBinding
import com.example.teamprogram.ui.forum.ForumListDataHelper
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
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
            val email = intent.getStringExtra("email")

            val async = Async()
            async.apply {
                get_context(this@ForumPublishPage)
                get_user_info(username!!,email!!)
                get_binding(binding)
            }.execute()
        }
    }

    inner class Async: AsyncTask<Void, Void, Boolean>(){
        lateinit var _context:Context
        lateinit var _name:String
        lateinit var _email:String
        lateinit var _binding:ForumPublishPageBinding
        lateinit var _responseData:String
        fun get_context(context:Context){
            _context = context
        }

        fun get_user_info(username:String,email:String) {
            _name = username
            _email = email
        }

        fun get_binding(binding: ForumPublishPageBinding){
            _binding = binding
        }
        override fun doInBackground(vararg p0: Void?): Boolean? {
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("uname",_name)
                .add("email",_email)
                .add("title", _binding.forumPublishTitle.text.toString())
                .add("content", _binding.forumPublishContent.text.toString())
                .build()

            val request = Request.Builder()
                .url("http://101.43.184.204:3002/forum/post")
                .post(requestBody)
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            if (responseData != null) {
                _responseData = responseData
                return true
            }else{
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            val intent = Intent()
            if (result!!){
                intent.putExtra("responseData",_responseData)
                setResult(RESULT_OK, intent)
            }else{
                intent.putExtra("responseData","")
                setResult(RESULT_CANCELED, intent)
            }
            finish()
        }
    }
}