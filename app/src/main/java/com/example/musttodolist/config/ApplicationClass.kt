package com.example.musttodolist.config

import android.app.Application
import com.example.musttodolist.repository.LevelRepository
import com.example.musttodolist.repository.TodoRepository

class ApplicationClass : Application(){
    override fun onCreate() {
        super.onCreate()
        TodoRepository.initialize(this)
        LevelRepository.initialize(this)
    }
}