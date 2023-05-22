package com.example.teamprogram.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.Content
import com.example.teamprogram.NewSchedule
import com.example.teamprogram.R
import com.example.teamprogram.ui.notifications.AppDbHelper
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class HomeworkAdapter(val homeworkList: ArrayList<HomeWorkContent>) :RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>(){

    lateinit var context:Context

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
            if (homeworkItem.type == HomeWorkContent.HOMEWORK){
                Content.actionStart(parent.context, homeworkItem.title, homeworkItem.content)
            }
        }
        viewHolder.itemView.setOnLongClickListener(){
            val position = viewHolder.adapterPosition
            val homeworkItem = homeworkList[position]
            if (homeworkItem.type == HomeWorkContent.CREATE){
                showPopMenu(view,homeworkItem.id,position)
            }
            true
        }

        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        val homeworkItem = homeworkList[position]
        holder.coures.text = homeworkItem.course
        holder.title.text = homeworkItem.title
        holder.content.text = homeworkItem.content
        val hour = homeworkItem.time_left / 60
        var backgroundColor: Int
        if (hour<24){
            backgroundColor = Color.parseColor("#DE868F")
        }else if (hour<72){
            backgroundColor = Color.parseColor("#F4CE98")
        }else if (hour<168){
            backgroundColor = Color.parseColor("#93D2F3")
        }else{
            backgroundColor = Color.parseColor("#A4ADB3")
        }
        holder.itemView.setBackgroundColor(backgroundColor)
        holder.time_left.text = "Time Left: ${hour}h ${homeworkItem.time_left % 60}min"
    }

    override fun getItemCount() = homeworkList.size



    @SuppressLint("Range")
    private fun showPopMenu(view: View, id: Int, position: Int){
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.homework_pop_menu, popupMenu.menu)
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.homework_item_mod -> {
                    NewSchedule.actionStart(context,id)
                }
                R.id.homework_item_del -> {
                    val dbHelper = AppDbHelper(context!!,"homeworklist",1)
                    val db = dbHelper.writableDatabase
                    db.execSQL("delete from homeworkList where id = ?", arrayOf(id.toString()))
                    homeworkList.removeAt(position)
                    // 更新适配器的数据源
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount - 1)
                }
            }
            popupMenu.dismiss()
            true
        }
    }
}

