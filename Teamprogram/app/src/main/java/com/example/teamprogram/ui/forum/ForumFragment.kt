package com.example.teamprogram.ui.forum

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.customview.widget.ViewDragHelper
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ForumPublishPage
import com.example.teamprogram.ForumUserLoginPage
import com.example.teamprogram.R
import com.example.teamprogram.databinding.FragmentForumBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.reflect.InvocationTargetException

@SuppressLint("Range")
class ForumFragment :Fragment(), View.OnClickListener {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    lateinit var DataList:List<ForumList>
    lateinit var adapter:ForumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumBinding.inflate(inflater,container,false)
        val  root: View = binding.root


        //登录检测
        run{
            try {
                val strList = ArrayList<String>()
                val input = context?.openFileInput("userdata")

                val reader = BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        strList.add(it)
                    }
                }
                Toast.makeText(context, "Automatic login successfully!", Toast.LENGTH_SHORT).show()

            } catch (e: java.lang.Exception) {
                val intent = Intent(context, ForumUserLoginPage::class.java)
                startActivityForResult(intent, 1)
            }
        }

        Async().execute()

        //点击用户详情界面
        binding.forumUserButton.setOnClickListener(this)
        //发布新贴
        binding.forumAddButton.setOnClickListener(this)
        return root
    }

    inner class Async: AsyncTask<Void, Void, Void>(){
        lateinit var data:List<ForumList>
        override fun doInBackground(vararg p0: Void?): Void? {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("http://101.43.184.204:3002/forum/post")
                .build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            data = parseJson(responseData!!)
            return null
        }

        override fun onPostExecute(result: Void?) {
            DataList = data
            adapt()
        }
    }

    override fun onClick(p0: View?) {
        when(p0) {
            binding.forumAddButton -> {

                try {
                    //进入发布内容界面
                    val input = context?.openFileInput("userdata")

                    val strList = ArrayList<String>()
                    val reader = BufferedReader(InputStreamReader(input))
                    reader.use {
                        reader.forEachLine {
                            strList.add(it)
                        }
                    }
                    val intent = Intent(context, ForumPublishPage::class.java)
                    intent.putExtra("username",strList[0])
                    intent.putExtra("email",strList[1])
                    startActivityForResult(intent, 2)

                } catch (e: java.lang.Exception) {
                    //检测有无用户数据，没有就要输入用户数据
                    val intent = Intent(context, ForumUserLoginPage::class.java)
                    startActivityForResult(intent, 1)
                }
            }
            binding.forumUserButton -> {
                val intent = Intent(context, ForumUserLoginPage::class.java)
                startActivityForResult(intent, 1)
            }
        }
        binding.forumDrawer.closeDrawers()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if(resultCode == RESULT_OK) {
                val returnusername = data?.getStringExtra("username")
                if (returnusername != null) {
                    Log.d("return name",returnusername)
                }
                val returnemail = data?.getStringExtra("email")
                val output = context?.openFileOutput("userdata", Context.MODE_PRIVATE)
                val writer = BufferedWriter(OutputStreamWriter(output))
                writer.use {
                    it.write(returnusername)
                    it.write("\n")
                    it.write(returnemail)
                }
            }
            2 -> if(resultCode == RESULT_OK) {
                val returndata = data?.getStringExtra("responseData")
                Async().execute()
                Toast.makeText(context, returndata, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parseJson(data:String):List<ForumList>{
        val jsonArray = JSONArray(data)
        val list = ArrayList<ForumList>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = JSONObject(jsonArray.getString(i))
            val PostID = jsonObject.getInt("PostID")
            val uname = jsonObject.getString("uname")
            val email = jsonObject.getString("email")
            val title = jsonObject.getString("title")
            val content = jsonObject.getString("content")
            list.add(ForumList(PostID,uname,email,title,content))
        }
        return list
    }

    private fun adapt(){
        adapter = ForumAdapter(DataList)
        binding.recyclerviewForum.adapter = adapter
        binding.recyclerviewForum.layoutManager = LinearLayoutManager(context)
    }
}