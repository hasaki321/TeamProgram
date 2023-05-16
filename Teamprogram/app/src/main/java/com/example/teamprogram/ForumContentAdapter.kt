package com.example.teamprogram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ui.forum.ForumComment

class ForumContentAdapter (val DataList: List<ForumComment>): RecyclerView.Adapter<ForumContentAdapter.ForumContentViewHolder>() {

    inner class ForumContentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val user = view.findViewById<TextView>(R.id.forum_content_item_username)
        val content = view.findViewById<TextView>(R.id.forum_content_item_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ForumContentViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forum_content_item,parent,false)
        val viewHolder = ForumContentViewHolder(view)
        return viewHolder
    }

    override fun getItemCount() = DataList.size

    override fun onBindViewHolder(holder: ForumContentViewHolder, position: Int) {
        holder.user.text = DataList[position].username
        holder.content.text  = DataList[position].content
    }
}