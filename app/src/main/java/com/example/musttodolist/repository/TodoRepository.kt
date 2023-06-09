package com.example.musttodolist.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.musttodolist.dao.TodoDAO
import com.example.musttodolist.database.TodoDatabase
import com.example.musttodolist.dto.TodoDTO


private const val DATABASE_NAME = "todo-database.db"
class TodoRepository private  constructor(context: Context){

    private val database : TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val todoDAO = database.todoDao()

    fun allOfTodoList(): LiveData<MutableList<TodoDTO>> = todoDAO.allOfTodoList()

    fun todoList(time: String): LiveData<MutableList<TodoDTO>> = todoDAO.todoList(time)


    fun getTomorrowList(time: String) : LiveData<MutableList<TodoDTO>> = todoDAO.getTomorrowList(time)
    fun calendarTodoList(time: String): LiveData<MutableList<TodoDTO>> = todoDAO.calendarTodoList(time)


    fun getOneTodo(id:Long):TodoDTO = todoDAO.todoSelectOne(id)
    fun getLatestTodo():TodoDTO? = todoDAO.getLatestTodo()
    fun todoInsert(dto:TodoDTO) = todoDAO.todoInsert(dto)
    suspend fun todoUpdate(dto: TodoDTO) = todoDAO.todoUpdate(dto)
    fun todoDelete(dto: TodoDTO) = todoDAO.todoDelete(dto)

    fun getTime() = todoDAO.getTimeList()
    fun doneTodoList() = todoDAO.doneTodoList()
    suspend fun updateCompleteStatus(id: Long, isComplete: Boolean) {
        todoDAO.updateCompleteStatus(id, isComplete)
    }
    companion object {
        private var INSTANCE: TodoRepository?=null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TodoRepository(context)
                Log.d("initialize","todo")
            }
        }

        fun get(): TodoRepository {
            return INSTANCE ?:
            throw IllegalStateException("TodoRepository must be initialized")
        }
    }
    /*
    - 먼저 Room.databaseBuilder().build() 를 통해 데이터베이스를 빌드하도록 합니다.
    - companion object 객체는 클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체로, 데이터베이스 생성 및 초기화를 담당하기 위해 작성하였습니다.
    */
}