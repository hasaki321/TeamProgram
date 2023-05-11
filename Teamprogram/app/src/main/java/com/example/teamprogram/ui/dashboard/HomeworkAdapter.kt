package com.example.teamprogram.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.Content
import com.example.teamprogram.R

class HomeworkAdapter(val homeworkList: ArrayList<HomeWorkContent>) :RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(){

    inner class HomeworkViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val coures = view.findViewById<TextView>(R.id.Homework_course)
        val title = view.findViewById<TextView>(R.id.Homework_title)
        val content = view.findViewById<TextView>(R.id.Homework_content)
        val time_left = view.findViewById<TextView>(R.id.time_left)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):HomeworkViewHolder{
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.dash_board_item, parent, false)
        val viewHolder = HomeworkViewHolder(view)
        viewHolder.itemView.setOnClickListener(){
            val position = viewHolder.adapterPosition
            val homeworkItem = homeworkList[position]
            Content.actionStart(parent.context, homeworkItem.title, homeworkItem.content)
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val homeworkItem = homeworkList[position]
        holder.coures.text = homeworkItem.course
        holder.title.text = homeworkItem.title
        holder.content.text = homeworkItem.content
        holder.time_left.text = "剩余时间：\n ${homeworkItem.time_left / 60}h ${homeworkItem.time_left % 60}min"
    }

    override fun getItemCount() = homeworkList.size
}