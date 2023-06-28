package com.example.musttodolist

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.musttodolist.api.SetAlarmAPI
import com.example.musttodolist.databinding.ActivityTodoAddEditBinding
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.singleton.randomListSingleton
import com.example.musttodolist.viewModel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class TodoAddEditActivity : AppCompatActivity() {
    private lateinit var binding:ActivityTodoAddEditBinding
    private var todo:TodoDTO? = null
    lateinit var todoViewModel: TodoViewModel
    private lateinit var alarmManager : AlarmManager
    private lateinit var  setAlarmAPI: SetAlarmAPI
    var beforeRandomIndex = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //푸시 알림
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        setAlarmAPI = SetAlarmAPI()

        //viewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        var type = intent.getStringExtra("type")

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
            binding.pushAlarmChkBox.isChecked = todo!!.isAlarmOn
            binding.pushAlarmTimeTv.text = todo!!.alarmTime
            binding.todoAddEditTopTitleTv.text = "할 일 수정"

        } else {
            // ADD
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

        binding.saveBtn.setOnClickListener {
            val content = binding.contentEt.text.toString()
            val timeStamp =binding.setDateBtn.text.toString() //2023-05-05
            val alarmtime = binding.pushAlarmTimeTv.text.toString()
            val isAlarmOn = binding.pushAlarmChkBox.isChecked


            if(type.equals("ADD")){
                if( content.isNotEmpty() && !timeStamp.equals("날짜 입력")){
                    val year = timeStamp.substring(0,4).toInt()
                    val month = timeStamp.substring(5,7).toInt()
                    val day = timeStamp.substring(8,10).toInt()

                    //알림 체크가 되어 있다면
                    if(isAlarmOn){
                        CoroutineScope(Dispatchers.IO).launch {
                            val hour = alarmtime.substring(0,2).toInt()
                            val minute = alarmtime.substring(3,5).toInt()
                            val latestTodo : TodoDTO? = todoViewModel.getLatestTodo()
                            val id = if(latestTodo == null) 1 else latestTodo.id.toInt()+1
                            Log.d("시간add",hour.toString()+","+minute.toString()+"id : "+id)
                            setAlarmAPI.setNotice(
                                this@TodoAddEditActivity,
                                alarmManager,
                                year,
                                month,
                                day,
                                hour ,
                                minute,
                                id,
                                content,
                                id
                            )
                        }

                    }
                    val todoDTO = TodoDTO(0,content,timeStamp,false,isAlarmOn,alarmtime)
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
                // EDIT
                val content = binding.contentEt.text.toString()
                val timeStamp = binding.setDateBtn.text.toString()
                if (content.isNotEmpty() && !timeStamp.equals("날짜 입력")) {
                    val year = timeStamp.substring(0,4).toInt()
                    val month = timeStamp.substring(5,7).toInt()
                    val day = timeStamp.substring(8,10).toInt()

                    //알림 체크가 되어 있다면
                    if(isAlarmOn){
                        val hour = alarmtime.substring(0,2).toInt()
                        val minute = alarmtime.substring(3,5).toInt()
                        Log.d("시간edit",hour.toString()+","+minute.toString()+"id : "+todo!!.id)
                        setAlarmAPI.setNotice(
                            this,
                            alarmManager,
                            year,
                            month,
                            day,
                            hour ,
                            minute,
                            todo!!.id.toInt(),
                            content,
                            todo!!.id.toInt()
                        )
                    } else {
                        //알림이 체크 되어 있었다가 풀린거라면 알림 해제
                        CoroutineScope(Dispatchers.IO).launch {
                            if(todoViewModel.getOneTodo(todo!!.id).isAlarmOn){
                                setAlarmAPI.cancelAlarm(this@TodoAddEditActivity,alarmManager,todo!!.id.toInt())
                            }
                        }
                        Log.d("시간edit","id : "+todo!!.id)
                    }
                    val todoDTO = TodoDTO(todo!!.id,content, timeStamp,false,isAlarmOn,alarmtime)
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


        }
        binding.addBackBtn.setOnClickListener {
            finish()
        }
        binding.randomBtn.setOnClickListener {
            val index = randomListSingleton.getRandomListIndex(beforeRandomIndex)
            beforeRandomIndex = index //중복 방지를 위해
            binding.contentEt.setText(randomListSingleton.randomList.get(index))
        }
        binding.pushAlarmChkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                showTimePickerDialog()
            } else {
                binding.pushAlarmTimeTv.text = "알림 시간"
            }
        }
    }
    private fun showTimePickerDialog() {
        val currentTime = java.util.Calendar.getInstance()
        val hour = currentTime.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(java.util.Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.pushAlarmTimeTv.text = selectedTime
            }, hour, minute, true)
        timePickerDialog.setTitle("푸시 알림 시간")
        timePickerDialog.setOnCancelListener{
            binding.pushAlarmChkBox.isChecked = false
        }
        timePickerDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }
}