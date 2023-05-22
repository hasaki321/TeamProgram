package com.example.teamprogram.ui.dashboard

class HomeWorkContent (val course:String, val title:String, val content:String, val time_left :Long, val type:Int, val id:Int=0){
    companion object{
        val HOMEWORK = 0
        val CREATE = 1
    }
}