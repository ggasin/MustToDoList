package com.example.musttodolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musttodolist.dao.LevelDAO
import com.example.musttodolist.dao.MemoDAO
import com.example.musttodolist.dao.TodoDAO
import com.example.musttodolist.dto.LevelDTO
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.dto.TodoDTO


@Database(entities = arrayOf(TodoDTO::class,LevelDTO::class, MemoDTO::class), version = 2)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun todoDao() : TodoDAO
    abstract fun levelDao() : LevelDAO
    abstract fun memoDao() : MemoDAO
}