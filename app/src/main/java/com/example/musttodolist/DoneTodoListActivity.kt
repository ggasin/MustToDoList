package com.example.musttodolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.viewModel.LevelViewModel
import com.example.musttodolist.viewModel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
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
                                Toast.makeText(this@DoneTodoListActivity, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener{
                                dialog, id ->
                        })
                builder.show()
            }
        })
        doneTodoRVAdapter.setItemClickListener(object : DoneTodoRVAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("itemClick","true")
                    val todo = todoViewModel.getOneTodo(itemId)

                    val intent = Intent(this@DoneTodoListActivity,TodoAddEditActivity::class.java).apply {
                        putExtra("type","EDIT")
                        putExtra("item",todo)
                    }
                    requestActivity.launch(intent)
                }
            }

        })


    }
    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val todoDTO : TodoDTO
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                todoDTO = it.data?.getSerializableExtra("todoDTO", TodoDTO::class.java)!!
            } else {
                todoDTO = (it.data?.getSerializableExtra("todoDTO") as? TodoDTO)!!
            }

            when(it.data?.getIntExtra("flag", -1)) {
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        todoViewModel.todoInsert(todoDTO)
                        Log.d("id : " , todoDTO.id.toString())

                        //todoViewModel.insert(todo)를 통해
                        //  viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장하게 됩니다.
                    }

                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                1->{
                    CoroutineScope(Dispatchers.IO).launch {
                        todoViewModel.todoUpdate(todoDTO)
                    }
                    Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}