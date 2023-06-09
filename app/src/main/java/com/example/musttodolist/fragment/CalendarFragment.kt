package com.example.musttodolist.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musttodolist.TodoAddEditActivity
import com.example.musttodolist.adapter.CalendarRVAdapter
import com.example.musttodolist.adapter.TodoRVAdapter
import com.example.musttodolist.databinding.FragmentCalendarBinding
import com.example.musttodolist.decorator.DotCalendar
import com.example.musttodolist.decorator.SaturdayCalendar
import com.example.musttodolist.decorator.SundayCalendar
import com.example.musttodolist.decorator.TodayCalendar
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.viewModel.TodoViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Collections


class CalendarFragment : Fragment() {

    private var _binding:FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: CalendarRVAdapter
    lateinit var formattedDate : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoAdapter = CalendarRVAdapter(requireContext())
        binding.calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.calendarRecyclerView.adapter = todoAdapter



        val currentCalendar:Calendar = Calendar.getInstance()

        dotOnCalendar()
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->

            formattedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)

            todoViewModel.calendarTodoList(formattedDate).observe(viewLifecycleOwner) { items ->
                todoAdapter.update(items)
                if(items.isEmpty()){
                    Log.d("todoList1",date.month.toString()+"월"+date.day.toString())
                    binding.calendarView.removeDecorator(DotCalendar(Color.RED, Collections.singleton(CalendarDay.from(date.year,date.month,date.day))))

                }
            }
        }
        binding.calendarView.setDateSelected(currentCalendar, true)

        todoAdapter.setItemClickListener(object :TodoRVAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("itemClick","true")
                    val todo = todoViewModel.getOneTodo(itemId)

                    val intent = Intent(requireContext(), TodoAddEditActivity::class.java).apply {
                        putExtra("type","EDIT")
                        putExtra("item",todo)
                    }
                    requestActivity.launch(intent)

                }
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
                                Toast.makeText(requireContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()


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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dotOnCalendar(){
        binding.calendarView.addDecorator(TodayCalendar())
        binding.calendarView.addDecorator(SundayCalendar())
        binding.calendarView.addDecorator(SaturdayCalendar())
        todoViewModel.allOfTodoList().observe(viewLifecycleOwner){
            binding.calendarView.removeDecorators() //값이 삭제되면 빨간점이 없어져야할 상황에도 남아있으므로 값이 변할때마다 점을 지우고 다시 찍음.
            Log.d("InDotOnCalendarObserve",".")
            for(i in it){
                Log.d("todoList",i.content)
                //2023-05-23
                val time = i.time.split("-")
                val year : Int = time[0].toInt()
                val month : Int = time[1].toInt()-1
                val day : Int = time[2].toInt()
                Log.d("todoList",year.toString()+"z"+month.toString()+"z"+day.toString())
                binding.calendarView.addDecorator(DotCalendar(Color.RED, Collections.singleton(CalendarDay.from(year,month,day))))
            }
        }
    }
    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
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



}