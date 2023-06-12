package com.example.musttodolist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.dto.TodoDTO


@Dao
interface MemoDAO {
    @Query("select * from memoTable")
    fun getAllMemo():LiveData<MutableList<MemoDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun memoInsert(dto: MemoDTO)

    @Query( "select * from memoTable where id = (:id)")
    fun memoSelectOne(id:Long):MemoDTO

    @Update
    suspend fun memoUpdate(dto: MemoDTO)

    @Delete
    fun memoDelete(dto:MemoDTO)
}