package com.example.teamprogram.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprogram.databinding.FragmentForumBinding

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
            val forumList = forumViewModel.data
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
}