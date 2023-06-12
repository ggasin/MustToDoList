package com.example.musttodolist

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.musttodolist.databinding.ActivityMemoEditBinding
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.dto.TodoDTO
import java.text.SimpleDateFormat
import java.util.Calendar

class MemoEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMemoEditBinding
    private var memo:MemoDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        var type = intent.getStringExtra("type")
        if (type.equals("ADD")) {
            binding.memoEditTopTitleTv.setText("메모 추가")

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                memo = intent.getSerializableExtra("item",MemoDTO::class.java)!!
            } else {
                memo = intent.getSerializableExtra("item") as? MemoDTO
            }
            binding.memoEditTopTitleTv.setText("메모 수정")
            binding.memoTitleEt.setText(memo!!.memoTitle)
            binding.memoContentEt.setText(memo!!.memoContent)

        }

        binding.memoAddBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val time = SimpleDateFormat("yyyy-MM-dd").format(calendar.timeInMillis)
            val title = binding.memoTitleEt.text.toString()
            val content = binding.memoContentEt.text.toString()
            if(type.equals("ADD")){
                if(title.isNotEmpty() && content.isNotEmpty() ){
                    val memoDTO = MemoDTO(0,title,content,time)
                    val intent = Intent().apply {
                        putExtra("memoDTO",memoDTO)
                        putExtra("flag",0) //flag는 단순 구분을 위함으로 0일 경우 "추가"처리를, 1일 경우 "수정"처리를 하도록 분기를 나눌 것입니다.
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this,"입력하지 않은 란이 있는지 확인해 주세요", Toast.LENGTH_SHORT).show()
                }

            } else {
                if(title.isNotEmpty() && content.isNotEmpty() ){
                    val memoDTO = MemoDTO(memo!!.memoId,title,content,time)
                    val intent = Intent().apply {
                        putExtra("memoDTO",memoDTO)
                        putExtra("flag",1) //flag는 단순 구분을 위함으로 0일 경우 "추가"처리를, 1일 경우 "수정"처리를 하도록 분기를 나눌 것입니다.
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this,"입력하지 않은 란이 있는지 확인해 주세요", Toast.LENGTH_SHORT).show()
                }

            }
        }
        binding.memoEditBackBtn.setOnClickListener {
            finish()
        }
    }
}