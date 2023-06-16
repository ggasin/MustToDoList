package com.example.musttodolist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.dto.TodoDTO

class DoneTodoRVAdapter(val context : Context) : RecyclerView.Adapter<DoneTodoRVAdapter.ViewHolder>() {

    private var dateList = mutableListOf<String>()
    private var doneTodoList = mutableListOf<TodoDTO>()
    lateinit var inDoneTodoRVAdapter: InDoneTodoRVAdapter

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoneTodoRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.done_todo_rv_item,parent,false)
        return ViewHolder(v)


    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.done_todo_item_date_tv)
        val itemRecyclerView = itemView.findViewById<RecyclerView>(R.id.done_todo_in_rv)

        fun onbind(data: String){
            var sameDateTodoList = mutableListOf<TodoDTO>()
            date.text = data
            Log.d("DoneTodoAdapterData",data)

            inDoneTodoRVAdapter = InDoneTodoRVAdapter(context,data)
            itemRecyclerView.layoutManager = LinearLayoutManager(context)
            itemRecyclerView.adapter = inDoneTodoRVAdapter
            for(i in doneTodoList){
                if(data == i.time){
                    sameDateTodoList.add(i)
                }
                Log.d("InDoneBeforeOnBind",i.content + i.time)
            }
            inDoneTodoRVAdapter.update(sameDateTodoList)
        }


    }

    override fun onBindViewHolder(holder: DoneTodoRVAdapter.ViewHolder, position: Int) {
        holder.onbind(dateList[position])

    }

    override fun getItemCount(): Int {
        return dateList.size
    }
    fun update(newList : MutableList<String>){
        this.dateList = newList
        Log.d("DoneTodoAdapterTimeList",newList.toString())

        notifyDataSetChanged()
    }
    fun updateDoneTodoList(newList : MutableList<TodoDTO>){
        this.doneTodoList = newList
        for(i in doneTodoList){
            Log.d("InDoneBefore",i.content + i.time)
        }

    }

}