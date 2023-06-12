package com.example.musttodolist.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musttodolist.ItemDetailActivity
import com.example.musttodolist.TodoAddActivity
import com.example.musttodolist.adapter.TodoRVAdapter
import com.example.musttodolist.databinding.FragmentHomeBinding
import com.example.musttodolist.dto.LevelDTO
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.repository.TodoRepository
import com.example.musttodolist.viewModel.LevelViewModel
import com.example.musttodolist.viewModel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var todoViewModel: TodoViewModel
    lateinit var levelViewModel: LevelViewModel
    lateinit var todoAdapter: TodoRVAdapter
    lateinit var currentTime:String

    var isTodayUpdate:Boolean = true






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoAdapter = TodoRVAdapter(requireContext())
        binding.todoRv.layoutManager = LinearLayoutManager(requireContext())
        binding.todoRv.adapter = todoAdapter


        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        levelViewModel = ViewModelProvider(this).get(LevelViewModel::class.java)
        val calendar = Calendar.getInstance()
        currentTime = SimpleDateFormat("yyyy-MM-dd").format(calendar.timeInMillis)

        levelViewModel.levelList.observe(viewLifecycleOwner){
            if(it == null){
                Log.d("HomeFragmentLevelList","null")

            } else {
                updateLevelInfo(it)
            }


        }



        binding.dayToggleBtnGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            Log.d("zz",isChecked.toString())
            if(isChecked){
                when(checkedId){
                    binding.todayBtn.id ->{
                        isTodayUpdate = true
                        Log.d("Homefragment","todoBtn")
                        todoViewModel.todoList.observe(viewLifecycleOwner){
                            if(isTodayUpdate){
                                todoAdapter.update(it)
                                Log.d("Homefragment","todoList")
                            }
                        }

                    }
                    binding.tomorrowBtn.id ->{
                        isTodayUpdate = false
                        Log.d("Homefragment","todoTomorrowBtn")
                        todoViewModel.todoTomorrowList.observe(viewLifecycleOwner){
                            if(!isTodayUpdate){
                                todoAdapter.update(it)
                                Log.d("Homefragment","todoTomorrowList")
                            }


                        }

                    }
                }
            }
        }
        //'오늘'이라는 버튼이 클릭되어 있도록
        binding.dayToggleBtnGroup.check(binding.dayToggleBtnGroup.getChildAt(0).id)



        binding.todoAddBtn.setOnClickListener{
            val intent = Intent(requireContext(),TodoAddActivity::class.java).apply {
                putExtra("type","ADD")
            }
            requestActivity.launch(intent)

        }

        todoAdapter.setItemClickListener(object :TodoRVAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("itemClick","true")
                    val todo = todoViewModel.getOneTodo(itemId)

                    val intent = Intent(requireContext(),ItemDetailActivity::class.java).apply {
                        putExtra("type","EDIT")
                        putExtra("item",todo)
                    }
                    requestActivity.launch(intent)

                }
            }
        })
        todoAdapter.setItemCompleteBtnClickListener(object : TodoRVAdapter.ItemCompleteBtnClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                //itemid -> room database 에 작성된 id
                todoViewModel.updateCompleteStatus(itemId, true)
                //레벨 업데이트
                updateDBlevelUp(binding.level.text.toString().toInt(),20,binding.progressBar.progress,binding.progressBar.max)

                Toast.makeText(requireContext(),"완료",Toast.LENGTH_SHORT).show()
            }

        })
        todoAdapter.setItemCompleteCancelBtnClickListener(object :TodoRVAdapter.ItemCompleteCancelBtnClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {


                //itemid -> room database 에 작성된 id
                todoViewModel.updateCompleteStatus(itemId,false)
                updateDBlevelDown(binding.level.text.toString().toInt(),20,binding.progressBar.progress,binding.progressBar.max)
                //todoViewModel.insert(todo)를 통해
                //  viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장하게 됩니다.


            }

        })
        todoAdapter.setItemDeleteBtnClickListener(object :TodoRVAdapter.ItemDeleteBtnClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                val builder = AlertDialog.Builder(requireContext())
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //Fragment에서 View Binding을 사용할 경우 Fragment는 View보다 오래 지속되어,
    //Fragment의 Lifecycle로 인해 메모리 누수가 발생할 수 있기 때문입니다.
    //예를들어 Fragment에서 Navigation component 또는 BackStack or detach를 사용하는 경우,
    // onDestroyView() 이후에 Fragment view는 종료되지만, Fragment는 여전히 살아 있습니다.
    // 즉 메모리 누수가 발생하게 됩니다.
    //▶ 그래서 반드시 binding 변수를 onDetsroyView() 이후에 null로 만들어 줘야합니다.
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
                        //todoViewModel.insert(todo)를 통해
                        //  viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장하게 됩니다.
                    }
                    Toast.makeText(requireContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                1->{
                    CoroutineScope(Dispatchers.IO).launch {
                        todoViewModel.todoUpdate(todoDTO)
                    }
                    Toast.makeText(requireContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun updateDBlevelUp(currentLevel : Int, upGaugeSize : Int, currentGauge : Int, max : Int) {
        if (currentGauge+upGaugeSize >= max) {
            val levelDTO = LevelDTO(0,currentLevel+1,upGaugeSize,0,max+20)
            levelViewModel.levelUpdate(levelDTO)
        } else {
            val levelDTO = LevelDTO(0,currentLevel,upGaugeSize,currentGauge+upGaugeSize,max)
            levelViewModel.levelUpdate(levelDTO)
        }
    }
    private fun updateDBlevelDown(currentLevel : Int, downGaugeSize : Int, currentGauge : Int, max : Int) {
        if (currentGauge-downGaugeSize < 0) {
            val levelDTO = LevelDTO(0,currentLevel-1,downGaugeSize,max-20+currentGauge-downGaugeSize,max-20)
            levelViewModel.levelUpdate(levelDTO)
        } else {
            val levelDTO = LevelDTO(0,currentLevel,downGaugeSize,currentGauge-downGaugeSize,max)
            levelViewModel.levelUpdate(levelDTO)
        }
    }

    private fun updateLevelInfo(dto : LevelDTO){

            binding.level.text = dto.level.toString()
            binding.progressBar.progress = dto.currentGauge
            binding.progressBar.max = dto.max



    }

}