package com.example.musttodolist

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.musttodolist.databinding.ActivityTodoAddBinding
import com.example.musttodolist.dto.TodoDTO
import java.text.SimpleDateFormat
import java.util.Calendar

class TodoAddActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTodoAddBinding
    private var todoDTO:TodoDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setDateBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                val dateString = "%d-%02d-%02d".format(year,month+1,dayOfMonth)
                binding.setDateBtn.setText(dateString)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DATE)).show()
        }

        var type = intent.getStringExtra("type")

        binding.addBtn.setOnClickListener {
            val content = binding.contentEt.text.toString()
            val timeStamp =binding.setDateBtn.text.toString()
            if(type.equals("ADD")){
                if(title.isNotEmpty() && content.isNotEmpty() && !timeStamp.equals("날짜 입력")){
                    val todoDTO = TodoDTO(0,content,timeStamp)
                    val intent = Intent().apply {
                        putExtra("todoDTO",todoDTO)
                        putExtra("flag",0) //flag는 단순 구분을 위함으로 0일 경우 "추가"처리를, 1일 경우 "수정"처리를 하도록 분기를 나눌 것입니다.
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this,"입력하지 않은 란이 있는지 확인해 주세요",Toast.LENGTH_SHORT).show()
                }

            } else {

            }


        }
        binding.addBackBtn.setOnClickListener {
            finish()
        }
    }
}