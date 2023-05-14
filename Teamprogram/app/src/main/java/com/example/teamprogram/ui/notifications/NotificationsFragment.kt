package com.example.teamprogram.ui.notifications

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teamprogram.databinding.FragmentNotificationsBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

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

        binding.webview.settings.javaScriptEnabled=true
        binding.webview.webViewClient = WebViewClient()
        binding.webview.loadUrl("http://10.0.2.2:3000/forum")

        val dbHelper = context?.let { LoginDataBaseHelper(it,name, 1) }
        val db = dbHelper?.writableDatabase

        val cursor = db?.query(name, null,null, null, null, null, null, null)

        if (cursor?.moveToFirst() == true){ //如果表里面有数据
            if (cursor.getInt(cursor.getColumnIndex("remember_check")) == 1) { //如果已选择记住
                binding.loginCheckbox.isChecked = true //将选择框勾选
                binding.dmkjUname.setText(cursor.getString(cursor.getColumnIndex("dmkj_uname")))
                binding.dmkjPass.setText(cursor.getString(cursor.getColumnIndex("dmkj_pass")))
                binding.moodleUname.setText(cursor.getString(cursor.getColumnIndex("moodle_uname")))
                binding.moodlePass.setText(cursor.getString(cursor.getColumnIndex("moodle_pass")))
                binding.xxtUname.setText(cursor.getString(cursor.getColumnIndex("xxt_uname")))
                binding.xxtPass.setText(cursor.getString(cursor.getColumnIndex("xxt_pass")))
            }
        }else{ //表中没有数据就创建一个不记忆的空内容
            val novalues = ContentValues().apply {
                put("id","1")
                put("remember_check", 0)
            }
            db?.insert(name, null, novalues)
        }

        binding.submitButton.setOnClickListener() {

            if (binding.loginCheckbox.isChecked) { //如果勾选记住

                val values = ContentValues().apply {
                    put("id","1")
                    put("dmkj_uname", binding.dmkjUname.text.toString())
                    put("dmkj_pass", binding.dmkjPass.text.toString())
                    put("moodle_uname", binding.moodleUname.text.toString())
                    put("moodle_pass", binding.moodlePass.text.toString())
                    put("xxt_uname", binding.xxtUname.text.toString())
                    put("xxt_pass", binding.xxtPass.text.toString())
                    put("remember_check", 1)
                }

                db?.delete(name,"id = ?", arrayOf("1"))
                Log.d("delete:","successed")
                db?.insert(name, null, values)
                Log.d("insert:","successed")
            }else{
                db?.delete(name,"id = ?", arrayOf("1"))
                Log.d("no checked delete:","successed")
                val novalues = ContentValues().apply {
                    put("id","1")
                    put("remember_check", 0)
                }
                db?.insert(name, null, novalues)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

