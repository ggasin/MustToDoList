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
    var beforeRandomIndex = -1
    var randomList : List<String> = listOf(
            "산책 30분 이상하기","런닝 3km 이상하기",
            "영화관에서 영화 1편 보기",
            "야외 카페 방문하기",
            "카페에서 책 2시간 이상 읽기" ,
            "주변을 산책하며 풍경 사진 1장 이상 찍기" ,
            "장보기" ,
            "공부중인 혹은 공부 하고싶던 내용 2시간 이상 공부 하기" ,
            "헬스장 혹은 근처 공원에서 운동 1시간 이상 하기." ,
            "자전거 타고 10km 이내 목적지 정해서 가기." ,
            "박물관 혹은 미술관, 전시회 방문하기")
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
                if( content.isNotEmpty() && !timeStamp.equals("날짜 입력")){
                    val todoDTO = TodoDTO(0,content,timeStamp,false)
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
        binding.randomBtn.setOnClickListener {
            val randomIndex = (0 until randomList.size).filter { it != beforeRandomIndex }.random()
            beforeRandomIndex = randomIndex
            binding.contentEt.setText(randomList.get(randomIndex))
        }
    }
}