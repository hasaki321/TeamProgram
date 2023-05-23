package com.example.teamprogram.ui.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.teamprogram.MainActivity
import com.example.teamprogram.R
import com.example.teamprogram.databinding.FragmentNotificationsBinding
import com.example.teamprogram.ui.forum.ForumAdapter
import java.io.*
import kotlin.random.Random


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private var imagePath:String = ""
    private val PERMISSION_REQUEST_CODE = 200
    private val PICK_IMAGE_REQUEST_CODE = 100
    private val binding get() = _binding!!
    private val AppList = ArrayList<App>()
    lateinit var adapter:AppAdapter

    companion object {
        var counter = 0
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appAdd.setOnClickListener(){
            showFloatingWindow()
        }
        initView()

        return root
    }

    fun showFloatingWindow(){
        val inflater = LayoutInflater.from(requireContext())
        val backgroundView = inflater.inflate(R.layout.background_dim, null)
        val rootView = requireActivity().window.decorView.findViewById<ViewGroup>(android.R.id.content)
        rootView.addView(backgroundView)
        val popupView = inflater.inflate(R.layout.app_float_window, null)

        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)

        val popupWindow = PopupWindow(
            popupView,
            width,
            height,
            true
        )
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        popupWindow.setOnDismissListener {
            rootView.removeView(backgroundView)
        }

        val image = popupView.findViewById<ImageView>(R.id.app_add_image)
        image.setOnClickListener(){
            openImagePicker()
            val bitmap = BitmapFactory.decodeFile(imagePath)
            image.setImageBitmap(bitmap)
        }

        val submit = popupView.findViewById<Button>(R.id.app_float_submit)
        submit.setOnClickListener(){
            val name = popupView.findViewById<EditText>(R.id.app_float_name).text.toString()
            val url = popupView.findViewById<EditText>(R.id.app_float_url).text.toString()

            insertDb(name,url,imagePath)
            AppList.add(App(counter+1,name,url,imagePath))
            imagePath = ""

            adapter.notifyItemChanged(counter+1)
            popupWindow.dismiss()
        }
        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)

    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)

            // 将图片保存到内部存储
            imagePath = saveImageToInternalStorage(bitmap)!!
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): String? {
        val contextWrapper = ContextWrapper(requireContext().applicationContext)
        // 创建一个存储图片的目录
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        // 创建一个唯一的文件名
        val fileName = Base64.encodeToString(bitmap.toString().toByteArray(), Base64.DEFAULT)

        val file = File(directory, fileName)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return file.absolutePath
    }

    // 处理权限请求结果
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(context, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun insertDb(name:String,url:String,image_lc:String){
        val dbHelper = AppDbHelper(context!!,"App.db",1)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name",name)
            put("url",url)
            put("image",image_lc)
        }
        db.insert("AppList",null,values)
    }

    @SuppressLint("Range")
    private fun initView(){
        val dbHelper = AppDbHelper(context!!, "App.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("AppList", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val url = cursor.getString(cursor.getColumnIndex("url"))
                val image = cursor.getString(cursor.getColumnIndex("image"))
                counter += 1
                AppList.add(App(id,name,url,image))
            } while (cursor.moveToNext())
        }
        cursor.close()
        Log.d("list",AppList.toString())
        adapt()
    }
    private fun adapt(){
        adapter = AppAdapter(AppList)
        adapter.get_context(context!!)
        binding.appRecycleView.adapter = adapter
        binding.appRecycleView.layoutManager = StaggeredGridLayoutManager(4,
            StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

