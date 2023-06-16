package com.example.musttodolist.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musttodolist.dao.TodoDAO
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodoViewModel:ViewModel() {
    val todoList : LiveData<MutableList<TodoDTO>>
    val allOfTodoList : LiveData<MutableList<TodoDTO>>
    private val todoRepository:TodoRepository = TodoRepository.get()
    val todoTomorrowList : LiveData<MutableList<TodoDTO>>
    val timeList : LiveData<MutableList<String>>
    val doneTodoList : LiveData<MutableList<TodoDTO>>

    init {
        allOfTodoList = todoRepository.allOfTodoList()
        todoList = todoRepository.todoList(getTodayTimestamp())
        todoTomorrowList = todoRepository.getTomorrowList(getTomorrowTimestamp())
        timeList = todoRepository.getTime()
        doneTodoList = todoRepository.doneTodoList()
        Log.d("viewModel","init")

    }

    fun allOfTodoList() = todoRepository.allOfTodoList()
    fun getOneTodo(id:Long) = todoRepository.getOneTodo(id)

    fun todoInsert(dto: TodoDTO) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.todoInsert(dto)
    }

    fun calendarTodoList(time : String):LiveData<MutableList<TodoDTO>>{
        return todoRepository.calendarTodoList(time)
    }

    fun todoUpdate(dto: TodoDTO) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.todoUpdate(dto)
    }

    fun todoDelete(dto: TodoDTO) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.todoDelete(dto)
    }
    fun updateCompleteStatus(id: Long, isComplete: Boolean) {
        viewModelScope.launch {
            todoRepository.updateCompleteStatus(id, isComplete)
        }
    }



    private fun getTodayTimestamp(): String {
        val calendar = Calendar.getInstance()
        val timestamp = SimpleDateFormat("yyyy-MM-dd").format(calendar.timeInMillis)
        return timestamp // 현재 시간의 타임스탬프
    }
    private fun getTomorrowTimestamp(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1) // 일자 값에 +1을 해줍니다.

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timestamp = dateFormat.format(calendar.time)
        return timestamp // 내일의 타임스탬프
    }





}