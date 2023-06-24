package com.example.musttodolist

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.example.musttodolist.adapter.MainViewPageAdapter
import com.example.musttodolist.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tabTitleArray = arrayOf(
        "캘린더", "홈", "메모장"
    )
    private val tabIconArray = arrayOf(
        R.drawable.calendar_icon_black,
        R.drawable.home_icon_black,
        R.drawable.memo_icon_black
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mainVp.adapter = MainViewPageAdapter(this)

        TabLayoutMediator(binding.mainBottomTl, binding.mainVp){
                tab, position ->
            tab.text = tabTitleArray[position]
            tab.setIcon(tabIconArray[position])
        }.attach()







        //앱 접속 시 홈페이지부터 시작하도록
        binding.mainVp.setCurrentItem(1,false)
        //drawerLayout 내의 아이템 클릭 이벤트
        binding.navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.past_todo_list ->{

                    val intent = Intent(this,DoneTodoListActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }





    }

}