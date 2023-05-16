package com.example.musttodolist.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musttodolist.fragment.CalendarFragment
import com.example.musttodolist.fragment.HomeFragment
import com.example.musttodolist.fragment.MemoFragment

class MainViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){


    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0 -> return CalendarFragment()
            1 -> return HomeFragment()
            2 -> return MemoFragment()
            else -> throw IllegalArgumentException("Invalid tab position: $position")
        }
    }


}