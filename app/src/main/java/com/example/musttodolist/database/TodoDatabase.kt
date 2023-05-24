package com.example.musttodolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musttodolist.dao.TodoDAO
import com.example.musttodolist.dto.TodoDTO


@Database(entities = arrayOf(TodoDTO::class), version = 2)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun todoDao() : TodoDAO
}