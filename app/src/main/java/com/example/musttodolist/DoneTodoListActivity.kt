package com.example.musttodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musttodolist.adapter.DoneTodoRVAdapter
import com.example.musttodolist.adapter.TodoRVAdapter
import com.example.musttodolist.databinding.ActivityDoneTodoListBinding
import com.example.musttodolist.viewModel.LevelViewModel
import com.example.musttodolist.viewModel.TodoViewModel

class DoneTodoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoneTodoListBinding
    lateinit var todoViewModel: TodoViewModel
    lateinit var doneTodoRVAdapter: DoneTodoRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoneTodoListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //todoList 어뎁터 설정
        doneTodoRVAdapter = DoneTodoRVAdapter(this)
        binding.doneTodoRv.layoutManager = LinearLayoutManager(this)
        binding.doneTodoRv.adapter = doneTodoRVAdapter

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)


        todoViewModel.doneTodoList.observe(this){
            doneTodoRVAdapter.updateDoneTodoList(it)
        }
        todoViewModel.timeList.observe(this){
            Log.d("DoneTodoTimeList",it.toString())
            doneTodoRVAdapter.update(it)

        }

        binding.doneTodoListBackBtn.setOnClickListener {
            finish()
        }


    }
}