package com.example.musttodolist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musttodolist.dto.TodoDTO
import kotlinx.coroutines.selects.select
import java.sql.Timestamp


@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun todoInsert(dto: TodoDTO)


    @Query("select * from todoTable")
    fun allOfTodoList(): LiveData<MutableList<TodoDTO>>
    @Query("select * from todoTable where time = :time")
    fun todoList(time: String): LiveData<MutableList<TodoDTO>>
    @Query("select * from todoTable where time < DATE('now')")
    fun doneTodoList(): LiveData<MutableList<TodoDTO>>

    @Query("select DISTINCT time from todoTable where time < DATE('now')")
    fun getTimeList(): LiveData<MutableList<String>>
    @Query("SELECT * FROM todoTable WHERE time = :time")
    fun getTomorrowList(time: String): LiveData<MutableList<TodoDTO>>


    @Query("select * from todoTable where time = :time")
    fun calendarTodoList(time: String): LiveData<MutableList<TodoDTO>>


    @Query( "select * from todoTable where id = (:id)")
    fun todoSelectOne(id:Long):TodoDTO

    @Update
    suspend fun todoUpdate(dto: TodoDTO)

    @Delete
    fun todoDelete(dto: TodoDTO)



    @Query("UPDATE todoTable SET complete = :isComplete WHERE id = :id")
    suspend fun updateCompleteStatus(id: Long, isComplete: Boolean)


}