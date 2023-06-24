package com.example.musttodolist

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musttodolist.adapter.DoneTodoRVAdapter
import com.example.musttodolist.adapter.InDoneTodoRVAdapter
import com.example.musttodolist.adapter.TodoRVAdapter
import com.example.musttodolist.databinding.ActivityDoneTodoListBinding
import com.example.musttodolist.viewModel.LevelViewModel
import com.example.musttodolist.viewModel.TodoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DoneTodoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoneTodoListBinding
    lateinit var todoViewModel: TodoViewModel
    lateinit var doneTodoRVAdapter: DoneTodoRVAdapter
    lateinit var inDoneTodoRVAdapter: InDoneTodoRVAdapter

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

        doneTodoRVAdapter.setItemDeleteBtnClickListener(object : DoneTodoRVAdapter.ItemDeleteBtnClickListener{
            override fun onDeleteClick(view: View, position: Int, itemId: Long) {
                val builder = AlertDialog.Builder(this@DoneTodoListActivity)
                builder.setTitle("삭제")
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("삭제",
                        DialogInterface.OnClickListener{
                                dialog, id ->
                            lifecycleScope.launch {
                                val deleteItem = withContext(Dispatchers.IO) {
                                    todoViewModel.getOneTodo(itemId)
                                }

                                Log.d("HodeFragmentDeleteItem", deleteItem.content + "+" + deleteItem.id)

                                withContext(Dispatchers.IO) {
                                    todoViewModel.todoDelete(deleteItem)
                                }


                            }
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener{
                                dialog, id ->
                        })
                builder.show()
            }

        })


    }
}