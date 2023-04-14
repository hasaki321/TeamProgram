package com.example.teamprogram.ui.notifications

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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


        val name:String = "login"
        val dbHelper = context?.let { LoginDataBaseHelper(it,name, 1) }
        val db = dbHelper?.writableDatabase

        val cursor = db?.query(name, null,null, null, null, null, null, null)
        Log.d("curser:",cursor.toString())
        if (cursor?.moveToFirst() == true){
            binding.dmkjUname.setText(cursor.getString(cursor.getColumnIndex("dmkj_uname")))
            binding.dmkjPass.setText(cursor.getString(cursor.getColumnIndex("dmkj_pass")))
            binding.moodleUname.setText(cursor.getString(cursor.getColumnIndex("moodle_uname")))
            binding.moodlePass.setText(cursor.getString(cursor.getColumnIndex("moodle_pass")))
            binding.xxtUname.setText(cursor.getString(cursor.getColumnIndex("xxt_uname")))
            binding.xxtPass.setText(cursor.getString(cursor.getColumnIndex("xxt_pass")))}

        binding.submitButton.setOnClickListener() {
            val values = ContentValues().apply {
                put("id","1")
                put("dmkj_uname", binding.dmkjUname.text.toString())
                put("dmkj_pass", binding.dmkjPass.text.toString())
                put("moodle_uname", binding.moodleUname.text.toString())
                put("moodle_pass", binding.moodlePass.text.toString())
                put("xxt_uname", binding.xxtUname.text.toString())
                put("xxt_pass", binding.xxtPass.text.toString())
            }
            if (cursor?.moveToFirst() == true) {
                db?.delete(name,"id = ?", arrayOf("1"))
                Log.d("delete:","successed")
                db?.insert(name, null, values)
                Log.d("insert:","successed")
            } else {
                db?.insert(name, null, values)
                Log.d("creat:","successed")
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}