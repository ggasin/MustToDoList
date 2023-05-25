package com.example.musttodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musttodolist.adapter.MainViewPageAdapter
import com.example.musttodolist.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tabTitleArray = arrayOf(
        "캘린더", "홈", "메모장"
    )
    private val tabIconArray = arrayOf(
        R.drawable.calendar_icon_32,
        R.drawable.home_icon_32,
        R.drawable.memo_icon_32
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mainVp.adapter = MainViewPageAdapter(this)

        TabLayoutMediator(binding.mainBottomTl, binding.mainVp){
                tab, position -> tab.text = tabTitleArray[position]
            tab.setIcon(tabIconArray[position])
        }.attach()

        //앱 접속 시 홈페이지부터 시작하도록
        binding.mainVp.setCurrentItem(1,false)




    }

}