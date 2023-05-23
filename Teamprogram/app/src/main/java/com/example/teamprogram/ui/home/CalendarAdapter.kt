package com.example.teamprogram.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ForumConent
import com.example.teamprogram.R

class CalendarAdapter(val calendarList:List<Calendar>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CalendarViewHolder(view:View):RecyclerView.ViewHolder(view){
        val title = view.findViewById<TextView>(R.id.calendar_title)
        val timeLeft = view.findViewById<TextView>(R.id.calendar_time)
    }

    inner class EmptyCalendarViewHolder(view:View):RecyclerView.ViewHolder(view){
        var title = ""
        val empty_layout = view.findViewById<LinearLayout>(R.id.empty_background)
        val empty_text = view.findViewById<TextView>(R.id.empty_text)
    }

    inner class CalendarDateViewHolder(view:View):RecyclerView.ViewHolder(view){
        val date = view.findViewById<TextView>(R.id.calendar_date)
    }

    override fun getItemViewType(position: Int): Int {
        val calendarItem = calendarList[position]
        return calendarItem.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Calendar.Object) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item,
            parent, false)
        CalendarViewHolder(view)
    } else if(viewType == Calendar.Empty) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_calendar_item,
            parent, false)
        EmptyCalendarViewHolder(view)
    } else if(viewType == Calendar.Today) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_calendar_item,
            parent, false)
        EmptyCalendarViewHolder(view).empty_layout.setBackgroundColor(Color.parseColor("#DE868F"))
        EmptyCalendarViewHolder(view)
    }else if(viewType == Calendar.Now) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_calendar_item,
            parent, false)
        val holder = EmptyCalendarViewHolder(view)
        holder.apply {
            empty_layout.setBackgroundColor(Color.parseColor("#F4CE98"))
            empty_text.text = "\n\n   NOW"
        }
        EmptyCalendarViewHolder(view)
    }else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.week_title,
            parent, false)
        CalendarDateViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val calendarItem = calendarList[position]
        when (holder) {
            is CalendarViewHolder -> {
                holder.title.text = calendarItem.title
                holder.timeLeft.text  = calendarItem.timeLeft + "h"
            }
            is EmptyCalendarViewHolder -> {
                holder.title = calendarItem.title
            }
            is CalendarDateViewHolder -> {
                val calendar = java.util.Calendar.getInstance()
                val _day = calendar.get(java.util.Calendar.DAY_OF_YEAR)


                when (calendarItem.date) {
                    "1" ->  holder.date.text = "\n\n昨天"
                    "2" ->  holder.date.text = "\n\n今天"
                    "3" ->  holder.date.text = "\n\n明天 "
                    "4" ->  holder.date.text = "\n\n后天"
                    "5" -> {
                        calendar.set(java.util.Calendar.DAY_OF_YEAR,_day+3)
                        val month = calendar.get(java.util.Calendar.MONTH) + 1
                        val day = calendar.get(java.util.Calendar.DATE).toString()
                        holder.date.text = "\n\n$month-$day"
                    }
                    "6" ->  {
                        calendar.set(java.util.Calendar.DAY_OF_YEAR,_day+4)
                        val month = calendar.get(java.util.Calendar.MONTH) + 1
                        val day = calendar.get(java.util.Calendar.DATE).toString()
                        holder.date.text = "\n\n$month-$day"
                    }
                    "7" ->  {
                        calendar.set(java.util.Calendar.DAY_OF_YEAR,_day+5)
                        val month = calendar.get(java.util.Calendar.MONTH) + 1
                        val day = calendar.get(java.util.Calendar.DATE).toString()
                        holder.date.text = "\n\n$month-$day"
                    }
                    else -> throw IllegalArgumentException()
                }
            }
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemCount() = calendarList.size
}