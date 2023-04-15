package com.example.teamprogram.ui.forum

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.system.ErrnoException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.ForumUserLoginPage
import com.example.teamprogram.databinding.FragmentForumBinding
import java.io.*
import java.lang.reflect.InvocationTargetException

class ForumFragment :Fragment(){
    private var _binding: FragmentForumBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val forumViewModel = ViewModelProvider(this).get(ForumViewModel::class.java)

        _binding = FragmentForumBinding.inflate(inflater,container,false)
        val  root: View = binding.root

        var  db: SQLiteDatabase? = null;



        try {
            val strList = ArrayList<String>()
            val input = context?.openFileInput("userdata")
            Log.d("fetch user data","tried")

            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    strList.add(it)
                }
            }
            Toast.makeText(context,"Automatic login successfully!",Toast.LENGTH_SHORT).show()

        } catch (e: java.lang.Exception) {
            val intent = Intent(context,ForumUserLoginPage::class.java)
            startActivityForResult(intent, 1)
        }



        val recyclerView: RecyclerView = binding.recyclerviewForum
        val forumList = forumViewModel.data
        val adapter = ForumAdapter(forumList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val button = binding.forumAddButton
        button.setOnClickListener(){
            forumViewModel.addData(Forum_Text("title","content content content",
                ArrayList<String>(), ArrayList<String>()
            ))
            var forumList = ArrayList<Forum_Text>()
            forumViewModel.livedata.observe(this, Observer {
                    data -> forumList = data
            } )

            Log.d("data size: ", "${forumList.size}")
            val adapter = ForumAdapter(forumList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)


            recyclerView.scrollToPosition(forumViewModel.data.size - 1)
        }

        return root
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
                    it.write(returnemail)
                }
            }
        }
    }
}