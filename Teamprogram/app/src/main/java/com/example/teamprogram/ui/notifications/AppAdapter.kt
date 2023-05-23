package com.example.teamprogram.ui.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import com.example.teamprogram.ui.forum.ForumList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.App_web
import com.example.teamprogram.ui.notifications.NotificationsFragment
import com.example.teamprogram.ForumConent
import com.example.teamprogram.R
import java.io.BufferedReader
import java.io.InputStreamReader

class AppAdapter(val Applist: ArrayList<App>):RecyclerView.Adapter<AppAdapter.AppHolder>() {
    lateinit var context:Context
    private var onImageSelectedListener: OnImageSelectedListener? = null
    private val PICK_IMAGE_REQUEST_CODE = 100

    inner class AppHolder(view: View):RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.app_single_app_name)
        val imageView = view.findViewById<ImageView>(R.id.app_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_app,parent,false)
        val viewHolder = AppHolder(view)

        viewHolder.itemView.setOnClickListener(){
            val intent = Intent(context,App_web::class.java)
            intent.putExtra("url",Applist[viewHolder.adapterPosition].url)
            context.startActivity(intent)
        }
        viewHolder.itemView.setOnLongClickListener(){
            val position = viewHolder.adapterPosition
            val appItem = Applist[position]
            showPopMenu(viewHolder.itemView, appItem.id, position)
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        val appItem = Applist[position]
        holder.name.text = appItem.name
        if (appItem.image!=""){
            val bitmap = BitmapFactory.decodeFile(appItem.image)
            holder.imageView.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount() = Applist.size

    fun get_context(_context: Context){
        context = _context
    }

    @SuppressLint("Range")
    private fun showPopMenu(view: View, id: Int, position: Int){
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.app_pop_menu, popupMenu.menu)
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.app_float_menu_mod_info -> {
                    val name = Applist[position].name
                    val url = Applist[position].url
                    val image = Applist[position].image
                    showFloatingWindow(id,name,url,image,position)
                }
                R.id.app_float_menu_del -> {
                    delDb(id)
                    Applist.removeAt(position)
                    // 更新适配器的数据源
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount - 1)
                }
            }
            popupMenu.dismiss()
            true
        }
    }

    private fun showFloatingWindow(id: Int = 0, name:String,url:String,image_lc:String, position: Int){
        val inflater = LayoutInflater.from(context)
        val backgroundView = inflater.inflate(R.layout.background_dim, null)
        val rootView = (context as Activity).window.decorView.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(backgroundView)
        val popupView = inflater.inflate(R.layout.app_float_window, null)

        val width = context.resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = context.resources.getDimensionPixelSize(R.dimen.popup_height)

        val popupWindow = PopupWindow(
            popupView,
            width,
            height,
            true
        )

        // 设置悬浮窗口的背景
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // 在关闭悬浮窗口时移除覆盖层
        popupWindow.setOnDismissListener {
            rootView.removeView(backgroundView)
        }

        // 获取输入框和按钮的引用
        val name_view = popupView.findViewById<EditText>(R.id.app_float_name)
        val url_view = popupView.findViewById<EditText>(R.id.app_float_url)
        val submit = popupView.findViewById<Button>(R.id.app_float_submit)
        val image = popupView.findViewById<ImageView>(R.id.app_add_image)
        image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as Activity).startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        if (image_lc!=""){
            val bitmap = BitmapFactory.decodeFile(image_lc)
            image.setImageBitmap(bitmap)
        }
        name_view.setText(name)
        url_view.setText(url)

        submit.setOnClickListener(){
            updateDb(id,name_view.text.toString(),url_view.text.toString(),"")
            Applist[position].name = name_view.text.toString()
            Applist[position].url = url_view.text.toString()
            notifyItemChanged(position)
            popupWindow.dismiss()
        }
        popupWindow.showAtLocation((context as Activity).window.decorView, Gravity.CENTER, 0, 0)
    }

    private fun updateDb(id: Int,name:String,url:String,image_lc:String){
        val dbHelper = AppDbHelper(context!!,"App.db",1)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name",name)
            put("url",url)
            put("image",image_lc)
        }
        db.update("AppList", values, "id = ?", arrayOf(id.toString()))
    }

    private fun delDb(id: Int){
        val dbHelper = AppDbHelper(context!!,"App.db",1)
        val db = dbHelper.writableDatabase
        db.execSQL("delete from AppList where id = ?", arrayOf(id.toString()))
    }

    fun setOnImageSelectedListener(listener: OnImageSelectedListener) {
        this.onImageSelectedListener = listener
    }
}
interface OnImageSelectedListener {
    fun onImageSelected(image: String?)
}



