package com.example.teamprogram.ui.notifications

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teamprogram.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val dbHelper = context?.let { context -> LoginDataBase(context, "Login",1) }
        val db = dbHelper?.writableDatabase
        val cursor = db?.query("Login", null, null, null, null, null, null)
        //val cursor = db.query("Book", null, null, null, null, null, null)

        if (cursor?.moveToFirst() == true) {
                val name = cursor.getString(cursor.getColumnIndex("dmkj_uname"))
                val author = cursor.getString(cursor.getColumnIndex("dmkj_pass"))
                val pages = cursor.getInt(cursor.getColumnIndex("moodle_uname"))
                val price = cursor.getDouble(cursor.getColumnIndex("moodle_uname"))
                Log.d("MainActivity", "book name is $name")
                Log.d("MainActivity", "book author is $author")
                Log.d("MainActivity", "book pages is $pages")
                Log.d("MainActivity", "book price is $price")
        }
        cursor?.close()



        binding.submitButton.setOnClickListener(){
            try {
                db?.delete("Login","id = ?", arrayOf("1"))
            }finally {
                val values = ContentValues().apply {
                    put("dmkj_uname", binding.dmkjUname.text.toString())
                    put("dmkj_pass", binding.dmkjPass.text.toString())
                    put("moodle_uname", binding.moodleUname.text.toString())
                    put("moodle_pass", binding.moodlePass.text.toString())
                    put("xxt_uname", binding.xxtUname.text.toString())
                    put("xxt_pass", binding.xxtPass.text.toString())
                }
                db?.insert("Login",null, values)
                Toast.makeText(context,"Data added successfully",Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}