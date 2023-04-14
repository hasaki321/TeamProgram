package com.example.teamprogram.ui.home

class Calendar (val title:String, val timeLeft:String, val type:Int, val date:String = "0"){
    companion object{
        const val Empty = 0
        const val Object = 1
        const val Date = 2
    }
}