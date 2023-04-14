package com.example.teamprogram.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
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

    inner class EmptyCalendarViewHolder(view:View):RecyclerView.ViewHolder(view){var title = ""}

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
    } else {
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
                holder.timeLeft.text  = calendarItem.timeLeft
            }
            is EmptyCalendarViewHolder -> {
                holder.title = calendarItem.title
            }
            is CalendarDateViewHolder -> {
                when (calendarItem.date) {
                    "1" ->  holder.date.text = "Monday"
                    "2" ->  holder.date.text = "Tuesday"
                    "3" ->  holder.date.text = "Wednesday "
                    "4" ->  holder.date.text = "Thursday"
                    "5" ->  holder.date.text = "Friday"
                    "6" ->  holder.date.text = "Saturday"
                    "7" ->  holder.date.text = "Sunday"
                    else -> throw IllegalArgumentException()
                }
            }
            else -> throw IllegalArgumentException()
        }

    }

    override fun getItemCount() = calendarList.size
}