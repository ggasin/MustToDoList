package com.example.musttodolist.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musttodolist.dto.LevelDTO
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.repository.LevelRepository
import com.example.musttodolist.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LevelViewModel:ViewModel() {
    val levelList : LiveData<LevelDTO>
    private val levelRepository: LevelRepository = LevelRepository.get()

    init {
        levelList = levelRepository.getLevelList()
    }
    fun levelUpdate(dto: LevelDTO) = viewModelScope.launch(Dispatchers.IO){
        levelRepository.levelUpdate(dto)
    }
}