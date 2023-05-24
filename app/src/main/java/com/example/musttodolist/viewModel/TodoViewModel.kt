package com.example.musttodolist.viewModel

import androidx.lifecycle.LiveData
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
    private val todoRepository:TodoRepository = TodoRepository.get()
    val todoTomorrowList : LiveData<MutableList<TodoDTO>> = todoRepository.getTomorrowList(getTomorrowTimestamp())

    init {
        todoList = todoRepository.todoList(getTodayTimestamp())
    }

    fun getOneTodo(id:Long) = todoRepository.getOneTodo(id)

    fun todoInsert(dto: TodoDTO) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.todoInsert(dto)
    }

    fun todoUpdate(dto: TodoDTO) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.todoUpdate(dto)
    }

    fun todoDelete(dto: TodoDTO) = viewModelScope.launch(Dispatchers.IO){
        todoRepository.todoDelete(dto)
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