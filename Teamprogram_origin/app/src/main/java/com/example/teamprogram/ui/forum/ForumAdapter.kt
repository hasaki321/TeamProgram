package com.example.teamprogram.ui.forum

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ForumConent
import com.example.teamprogram.R

class ForumAdapter (val forumList: List<Forum_Text>):RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {

    inner class ForumViewHolder(view: View):RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.forum_title)
        val content = view.findViewById<TextView>(R.id.forum_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ForumViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forum_item,parent,false)
        val viewHolder = ForumViewHolder(view)
        viewHolder.itemView.setOnClickListener(){
            val position = viewHolder.adapterPosition
            val forumItem = forumList[position]
            ForumConent.actionStart(parent.context,forumItem.title,forumItem.content,position)
        }
        return viewHolder
    }

    override fun getItemCount() = forumList.size

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val forumItem = forumList[position]
        holder.title.text = forumItem.title
        holder.content.text  = forumItem.content
    }
}