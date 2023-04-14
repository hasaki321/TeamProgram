package com.example.teamprogram.ui.forum

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teamprogram.ui.home.Calendar
import kotlin.random.Random

class ForumViewModel :ViewModel(){
    val data = ArrayList<Forum_Text>()
    var livedata = ForumLiveData(data)
    init {
        for(i in 1..20) {
            val random = (1..50).random()
            var content = "内容内容内容"
            val builder = StringBuilder()
            repeat(random) {
                builder.append(content)
            }
            val userList = ArrayList<String>()
            val contentList = ArrayList<String>()

            for (i in 1..random) {
                userList.add("user: ${i}")
                val builder = StringBuilder()
                repeat(random/3) {
                    builder.append(content)
                }
                contentList.add(builder.toString())
            }
            data.add(Forum_Text("This is Title ${i}", builder.toString(), userList, contentList))
        }
        livedata = ForumLiveData(data)
        Log.d("live data", "${livedata.getData().size}")
    }

    fun addData(forumText: Forum_Text){
        livedata.value?.add(forumText)
        Log.d("live data size", "${livedata.getData().size}")
    }
}

