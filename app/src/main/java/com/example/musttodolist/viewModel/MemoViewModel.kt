package com.example.musttodolist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.repository.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel:ViewModel() {
    val memoList:LiveData<MutableList<MemoDTO>>
    private val memoRepository:MemoRepository = MemoRepository.get()

    init {
        memoList = memoRepository.getAllMemo()
    }


    fun memoInsert(dto: MemoDTO) = viewModelScope.launch(Dispatchers.IO){
        memoRepository.memoInsert(dto)
    }
    fun memoSelectOne(id: Long) = memoRepository.memoSelectOne(id)

    fun memoUpdate(dto:MemoDTO) = viewModelScope.launch(Dispatchers.IO){
        memoRepository.memoUpdate(dto)
    }

    fun memoDelete(dto:MemoDTO) = viewModelScope.launch(Dispatchers.IO){
        memoRepository.memoDelete(dto)
    }



}