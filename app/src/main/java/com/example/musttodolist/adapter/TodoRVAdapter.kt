package com.example.musttodolist.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.dto.TodoDTO

class TodoRVAdapter (val context: Context) : RecyclerView.Adapter<TodoRVAdapter.ViewHolder>(){
    enum class ListType {
        TODAY,
        TOMORROW
    }

    private var todayList = mutableListOf<TodoDTO>()
    private var tomorrowList = mutableListOf<TodoDTO>()
    private var currentList: MutableList<TodoDTO> = todayList
    private var currentListType: ListType = ListType.TODAY
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.todo_rv_item,parent,false)
        return ViewHolder(v)

    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val content = itemView.findViewById<TextView>(R.id.rv_item_content_t)
        val completeBtn = itemView.findViewById<ImageButton>(R.id.complete_btn)
        val completeCancelBtn = itemView.findViewById<ImageButton>(R.id.complete_cancel_btn)
        val deleteBtn = itemView.findViewById<ImageButton>(R.id.delete_btn)
        val itemLayout = itemView.findViewById<LinearLayout>(R.id.itemLayout)

    }

    override fun onBindViewHolder(holder: TodoRVAdapter.ViewHolder, position: Int) {
        val item = todayList[position]
        holder.content.text = item.content


        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position,item.id)
        }
        holder.completeBtn.setOnClickListener {
            holder.content.paintFlags =holder.content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemLayout.setBackgroundResource(R.drawable.todo_rv_item_complete_background)
            holder.completeBtn.visibility = View.GONE
            holder.completeCancelBtn.visibility = View.VISIBLE

            itemCompleteBtnClickListener.onClick(it,position,item.id)
        }
        holder.completeCancelBtn.setOnClickListener {
            holder.content.paintFlags = holder.content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.itemLayout.setBackgroundResource(R.drawable.todo_rv_item_background)
            holder.completeBtn.visibility = View.VISIBLE
            holder.completeCancelBtn.visibility = View.GONE
            itemCompleteCancelClickListener.onClick(it,position,item.id)
        }
        holder.deleteBtn.setOnClickListener {

            itemDeleteBtnClickListener.onClick(it,position,item.id)
        }
    }

    override fun getItemCount(): Int {
        return todayList.size
    }



    fun update(newList : MutableList<TodoDTO>){
        this.todayList = newList
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
    interface ItemCompleteBtnClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }
    interface ItemDeleteBtnClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }
    interface ItemCompleteCancelBtnClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var itemCompleteBtnClickListener: ItemCompleteBtnClickListener
    private lateinit var itemDeleteBtnClickListener: ItemDeleteBtnClickListener
    private lateinit var itemCompleteCancelClickListener: ItemCompleteCancelBtnClickListener


    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    fun setItemCompleteBtnClickListener(itemCompleteBtnClickListener: ItemCompleteBtnClickListener){
        this.itemCompleteBtnClickListener = itemCompleteBtnClickListener
    }
    fun setItemDeleteBtnClickListener(itemDeleteBtnClickListener: ItemDeleteBtnClickListener){
        this.itemDeleteBtnClickListener = itemDeleteBtnClickListener
    }
    fun setItemCompleteCancelBtnClickListener(itemCompleteCancelBtnClickListener: ItemCompleteCancelBtnClickListener){
        this.itemCompleteCancelClickListener = itemCompleteCancelBtnClickListener
    }

    fun updateTodayList(newList: MutableList<TodoDTO>) {
        todayList = newList
        if (currentListType == ListType.TODAY) {
            currentList = todayList
            notifyDataSetChanged()
        }
    }

    fun updateTomorrowList(newList: MutableList<TodoDTO>) {
        tomorrowList = newList
        if (currentListType == ListType.TOMORROW) {
            currentList = tomorrowList
            notifyDataSetChanged()
        }
    }

    fun switchToList(listType: ListType) {
        currentListType = listType
        currentList = if (listType == ListType.TODAY) {
            todayList
        } else {
            tomorrowList
        }
        notifyDataSetChanged()
    }




}