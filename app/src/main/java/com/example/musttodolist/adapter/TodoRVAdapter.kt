package com.example.musttodolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musttodolist.R
import com.example.musttodolist.data.todoData

class TodoRVAdapter (val items : ArrayList<todoData>) : RecyclerView.Adapter<TodoRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.todo_rv_item,parent,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: TodoRVAdapter.ViewHolder, position: Int) {
        holder.content.text = items[position].content
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val content = itemView.findViewById<TextView>(R.id.rv_item_content_t)
    }


}