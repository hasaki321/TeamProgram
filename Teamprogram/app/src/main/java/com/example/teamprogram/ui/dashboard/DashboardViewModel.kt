package com.example.teamprogram.ui.dashboard

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView

class DashboardViewModel : ViewModel() {
    val data = ArrayList<HomeWorkContent>()
    init{
        for (i in 1..20) {
            val random = (1..15).random()
            var content = "内容内容内容"
            val builder = StringBuilder()
            repeat(random) {
                builder.append(content)
            }
            data.add(HomeWorkContent("Homework ${i}", builder.toString()))
        }
    }
}