package com.example.musttodolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.data.TodoData
import com.example.musttodolist.dto.TodoDTO

class TodoRVAdapter (val context: Context) : RecyclerView.Adapter<TodoRVAdapter.ViewHolder>(){
    private var list = mutableListOf<TodoDTO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.todo_rv_item,parent,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: TodoRVAdapter.ViewHolder, position: Int) {
        holder.content.text = list[position].content

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position,list[position].id)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val content = itemView.findViewById<TextView>(R.id.rv_item_content_t)
    }

    fun update(newList : MutableList<TodoDTO>){
        this.list = newList
        notifyDataSetChanged()
    }
   /* interface ItemCheckBoxClickListener{
        fun onClick(view:View,position: Int,itemId: Long)
    }
    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener){
        this.itemCheckBoxClickListener = itemCheckBoxClickListener

    }*/

    interface ItemClickListener{
        fun onClick(view:View , position: Int,itemId: Long)
    }
    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }


}