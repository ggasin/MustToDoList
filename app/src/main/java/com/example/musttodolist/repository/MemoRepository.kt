package com.example.musttodolist.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.musttodolist.database.TodoDatabase
import com.example.musttodolist.dto.MemoDTO


private const val DATABASE_NAME = "todo-database.db"
class MemoRepository private constructor(context : Context) {
    private val database : TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private var memoDAO = database.memoDao()

    fun getAllMemo() : LiveData<MutableList<MemoDTO>> = memoDAO.getAllMemo()
    fun memoInsert(dto: MemoDTO) = memoDAO.memoInsert(dto)
    fun memoSelectOne(id: Long) = memoDAO.memoSelectOne(id)
    suspend fun memoUpdate(dto : MemoDTO)=  memoDAO.memoUpdate(dto)
    fun memoDelete(dto:MemoDTO) = memoDAO.memoDelete(dto)




    companion object {
        private var INSTANCE: MemoRepository?=null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MemoRepository(context)
                Log.d("initialize","memo")
            }
        }

        fun get(): MemoRepository {
            return INSTANCE ?:
            throw IllegalStateException("MemoRepository must be initialized")
        }
    }
    /*
    - 먼저 Room.databaseBuilder().build() 를 통해 데이터베이스를 빌드하도록 합니다.
    - companion object 객체는 클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체로, 데이터베이스 생성 및 초기화를 담당하기 위해 작성하였습니다.
    */

}
