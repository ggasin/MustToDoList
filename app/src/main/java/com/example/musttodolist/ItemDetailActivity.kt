package com.example.musttodolist

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.musttodolist.databinding.ActivityItemDetailBinding
import com.example.musttodolist.dto.TodoDTO
import java.util.Calendar

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemDetailBinding
    private var todo: TodoDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var type = intent.getStringExtra("type")
        Log.d("ItemDetailActivity",type.toString())
        if (type.equals("EDIT")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                todo = intent.getSerializableExtra("item",TodoDTO::class.java)!!
            } else {
                todo = intent.getSerializableExtra("item") as? TodoDTO
            }
            Log.d("contentEt",todo!!.content)
            Log.d("setDate",todo!!.time)
            binding.contentEt.setText(todo!!.content)
            binding.setDateBtn.text = todo!!.time

        }
        binding.setDateBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                val dateString = "%d-%02d-%02d".format(year,month+1,dayOfMonth)
                binding.setDateBtn.setText(dateString)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DATE)).show()
        }
        binding.editBtn.setOnClickListener {
            val content = binding.contentEt.text.toString()
            val timeStamp = binding.setDateBtn.text.toString()
            if (content.isNotEmpty()) {
                val todoDTO = TodoDTO(todo!!.id,content, timeStamp,false)
                val intent = Intent().apply {
                    putExtra("todoDTO", todoDTO)
                    putExtra("flag", 1)
                }
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this,"입력하지 않은 란이 있는지 확인해 주세요", Toast.LENGTH_SHORT).show()
            }

        }
        binding.detailBackBtn.setOnClickListener {
            finish()
        }
    }
}