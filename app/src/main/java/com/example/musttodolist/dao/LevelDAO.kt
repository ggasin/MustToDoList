package com.example.musttodolist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.musttodolist.dto.LevelDTO
import com.example.musttodolist.dto.TodoDTO


@Dao
interface LevelDAO {

    @Query("select * from LevelTable")
    fun getLevelList(): LiveData<LevelDTO>

    @Update
    suspend fun levelUpdate(dto: LevelDTO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: LevelDTO)




}