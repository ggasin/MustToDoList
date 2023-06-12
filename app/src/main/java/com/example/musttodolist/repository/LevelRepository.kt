package com.example.musttodolist.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musttodolist.dao.LevelDAO
import com.example.musttodolist.database.TodoDatabase
import com.example.musttodolist.dto.LevelDTO
import com.example.musttodolist.dto.TodoDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


private const val DATABASE_NAME = "todo-database.db"
class LevelRepository private  constructor(context: Context){
    private val database : TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoDatabase::class.java,
        DATABASE_NAME
    ).addCallback(object : RoomDatabase.Callback(){ //처음 데이터베이스가 생설 될 떄 초기값을 넣어두는 것.
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadExecutor().execute {
                db.execSQL("INSERT INTO LevelTable VALUES(0,1,20,0,100)")
            }
        }
    }).build()

    private val levelDAO = database.levelDao()

    fun getLevelList(): LiveData<LevelDTO> = levelDAO.getLevelList()
    suspend fun levelUpdate(dto: LevelDTO) = levelDAO.levelUpdate(dto)

    companion object {
        private var INSTANCE: LevelRepository?=null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = LevelRepository(context)
            }
        }

        fun get(): LevelRepository {
            return INSTANCE ?:
            throw IllegalStateException("LevelRepository must be initialized")
        }
        private fun insertDefaultLevelData() {
            Log.d("initialize","level")

        }
    }
    /*
    - 먼저 Room.databaseBuilder().build() 를 통해 데이터베이스를 빌드하도록 합니다.
    - companion object 객체는 클래스가 생성될 때 메모리에 적재되면서 동시에 생성하는 객체로, 데이터베이스 생성 및 초기화를 담당하기 위해 작성하였습니다.
    */
}
