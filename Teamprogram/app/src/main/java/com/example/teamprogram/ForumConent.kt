package com.example.teamprogram

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamprogram.databinding.ActivityForumConentBinding
import com.example.teamprogram.ui.forum.*
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.security.AccessController.getContext


class ForumConent : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityForumConentBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter:ForumContentAdapter
    lateinit var DataList:List<ForumComment>
    lateinit var view:View

    companion object{
        fun actionStart(
            context: Context,
            position: Int,
            PostID:Int,
            user: String,
            publisher: String,
            email: String,
            title: String,
            content: String,
        ){
            val intent = Intent(context, ForumConent::class.java)
            intent.putExtra("position",position)
            intent.putExtra("user",user)
            intent.putExtra("publisher", publisher)
            intent.putExtra("title", title)
            intent.putExtra("content", content)
            intent.putExtra("email", email)
            intent.putExtra("PostID", PostID)
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

        val async = Async()
        async.get_type("get")
        async.execute()
        binding.forumContentPublishButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        setContentView(view)
        when(p0) {
            binding.forumContentPublishButton -> {
                val async = Async()
                async.get_type("post")
                async.get_data(
                    intent.getStringExtra("user")!!,
                    intent.getStringExtra("email")!!,
                    binding.forumContentEditText.text.toString()
                )
                async.execute()
            }
        }
    }
    inner class Async: AsyncTask<Void, Void, Void>(){
        private lateinit var data:List<ForumComment>
        private lateinit var _type:String
        private lateinit var _name:String
        private lateinit var _email:String
        private lateinit var _content:String

        fun get_type(type:String){
            _type = type
        }

        fun get_data(name:String,email:String,content:String){
            _name = name
            _email = email
            _content = content
        }

        fun get(client:OkHttpClient){
            val urlBuilder: HttpUrl.Builder = "http://10.0.2.2:3000/forum/comment".toHttpUrlOrNull()!!.newBuilder()
            urlBuilder.addQueryParameter("PostID", intent.getIntExtra("PostID",0).toString())
            val url: String = urlBuilder.build().toString()

            val request: Request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            data = parseJson(responseData!!)
        }

        fun post(client:OkHttpClient){
            val requestBody = FormBody.Builder()
                .add("PostID",intent.getIntExtra("PostID",0).toString())
                .add("uname",_name)
                .add("email",_email)
                .add("content", _content)
                .build()

            val request: Request = Request.Builder()
                .url("http://10.0.2.2:3000/forum/comment")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
        }
        override fun doInBackground(vararg p0: Void?): Void? {
            val client = OkHttpClient()
            if (_type=="post"){
                post(client)
            }else{
                get(client)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            if (_type=="get") {
                DataList = data
                adapt()
            }else{
                val async = Async()
                async.get_type("get")
                async.execute()
            }
        }
    }
    private fun adapt(){
        adapter = ForumContentAdapter(DataList)
        binding.recyclerviewForumContent.adapter = adapter
        binding.recyclerviewForumContent.layoutManager = LinearLayoutManager(this)
    }
    private fun parseJson(data:String):List<ForumComment>{
        val list = ArrayList<ForumComment>()
        val jsonArray = JSONArray(data)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = JSONObject(jsonArray.getString(i))
            val uname = jsonObject.getString("uname")
            val email = jsonObject.getString("email")
            val content = jsonObject.getString("content")
            list.add(ForumComment(uname,email,content))
        }
        return list
    }
}