package com.example.teamprogram.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.Content
import com.example.teamprogram.R

class HomeworkAdapter(val homeworkList: List<HomeWorkContent>) :RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(){

    inner class HomeworkViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.textView)
        val content = view.findViewById<TextView>(R.id.textView2)
    }

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):HomeworkViewHolder{
        val view:View
        if (viewType == 0) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.dash_board_item, parent, false)
        }else{
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.dash_board_item_1, parent, false)
        }
        val viewHolder = HomeworkViewHolder(view)
        viewHolder.itemView.setOnClickListener(){
            val position = viewHolder.adapterPosition
            val homeworkItem = homeworkList[position]
            Content.actionStart(parent.context, homeworkItem.title, homeworkItem.content)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val homeworkItem = homeworkList[position]
        holder.title.text = homeworkItem.title
        holder.content.text = homeworkItem.content
    }

    override fun getItemCount() = homeworkList.size
}