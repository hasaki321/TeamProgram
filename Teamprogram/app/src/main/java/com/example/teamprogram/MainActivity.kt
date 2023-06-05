package com.example.teamprogram

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.teamprogram.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_forum, R.id.navigation_notifications
            )
        )

        //异步进行网络请求
        val task:MyAsyncTask = MyAsyncTask()
        task.get_context(this)
        task.execute()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setSelectedItemId(R.id.navigation_dashboard)
    }
}

class MyAsyncTask: AsyncTask<Void, Void, Void>(){
    lateinit var _context:Context
    fun get_context(context:Context){
        _context = context
    }
    override fun doInBackground(vararg p0: Void?): Void? {
        val client = OkHttpClient()

        val content = load("学习通")
        if (content.isNullOrEmpty()||content.size==1){
            val sharedPreferences: SharedPreferences = _context.getSharedPreferences("HW_data", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply{
                putString("jsData","还没有输入学习通账户！")
                apply()
            }
        }else{
            val requestBody = FormBody.Builder()
                .add("uname", String(Base64.encode(content[0].toByteArray(),Base64.DEFAULT)))
                .add("pwd",String(Base64.encode(content[1].toByteArray(),Base64.DEFAULT)))
                .build()

            val request = Request.Builder()
                .url("http://www.lzuhrs.cn:3001/chaoxing")
                .post(requestBody)
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            val jsArray: JSONArray = JSONArray(responseData)
            Log.d("jsondata:",jsArray.toString())
            val sharedPreferences: SharedPreferences = _context.getSharedPreferences("HW_data", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply{
                putString("jsData",jsArray.toString())
                apply()
            }
        }
        return null
    }

    override fun onCancelled() {
        val sharedPreferences: SharedPreferences = _context.getSharedPreferences("HW_data", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("jsData", "网络请求失败，请检查网络连接或者学习通账号密码是否正确")
            apply()
        }
    }
    private fun load(type: String): kotlin.collections.ArrayList<String> {
        val content = kotlin.collections.ArrayList<String>()
        try {
            val input = _context.openFileInput(type)
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.add(it)
                }
            }
        }catch (_:java.lang.Exception){ }
        return content
    }
}