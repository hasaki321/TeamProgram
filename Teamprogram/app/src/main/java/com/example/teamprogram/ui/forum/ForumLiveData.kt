package com.example.teamprogram.ui.forum

import androidx.lifecycle.LiveData

class ForumLiveData(forumData: ArrayList<Forum_Text>):LiveData<ArrayList<Forum_Text>>(){
    private var data = forumData

    fun getData() = this.data
    
}