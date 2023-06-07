package com.example.musttodolist.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.dto.TodoDTO

class CalendarRVAdapter (val context: Context) : RecyclerView.Adapter<CalendarRVAdapter.ViewHolder>(){
    private var list = mutableListOf<TodoDTO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.calendar_todo_rv_item,parent,false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: CalendarRVAdapter.ViewHolder, position: Int) {
        holder.onbind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val content = itemView.findViewById<TextView>(R.id.rv_item_content_t)
        val completeIv = itemView.findViewById<ImageView>(R.id.complete_iv) //완료 했다는 표시
        val incompleteIv= itemView.findViewById<ImageView>(R.id.incomplete_iv) //완료를 아직 하지 않았다는 표시
        val deleteBtn = itemView.findViewById<ImageButton>(R.id.delete_btn)
        val itemLayout = itemView.findViewById<LinearLayout>(R.id.itemLayout)

        fun onbind(data: TodoDTO){
            content.text = data.content
            if(data.complete ){
                content.paintFlags =content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemLayout.setBackgroundResource(R.drawable.todo_rv_item_complete_background)
                completeIv.visibility = View.VISIBLE
                incompleteIv.visibility = View.GONE
            } else {
                content.paintFlags = content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                itemLayout.setBackgroundResource(R.drawable.todo_rv_item_background)
                completeIv.visibility = View.GONE
                incompleteIv.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,list[layoutPosition].id)
            }
            deleteBtn.setOnClickListener {
                itemDeleteBtnClickListener.onClick(it,layoutPosition,list[layoutPosition].id)
            }
        }
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

    private lateinit var itemClickListener: TodoRVAdapter.ItemClickListener
    private lateinit var itemCompleteBtnClickListener: TodoRVAdapter.ItemCompleteBtnClickListener
    private lateinit var itemDeleteBtnClickListener: TodoRVAdapter.ItemDeleteBtnClickListener
    private lateinit var itemCompleteCancelClickListener: TodoRVAdapter.ItemCompleteCancelBtnClickListener


    fun setItemClickListener(itemClickListener: TodoRVAdapter.ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    fun setItemCompleteBtnClickListener(itemCompleteBtnClickListener: TodoRVAdapter.ItemCompleteBtnClickListener){
        this.itemCompleteBtnClickListener = itemCompleteBtnClickListener
    }
    fun setItemDeleteBtnClickListener(itemDeleteBtnClickListener: TodoRVAdapter.ItemDeleteBtnClickListener){
        this.itemDeleteBtnClickListener = itemDeleteBtnClickListener
    }
    fun setItemCompleteCancelBtnClickListener(itemCompleteCancelBtnClickListener: TodoRVAdapter.ItemCompleteCancelBtnClickListener){
        this.itemCompleteCancelClickListener = itemCompleteCancelBtnClickListener
    }

    private fun updateItemView(position: Int) {
        Log.d("calendarAdapter","updateItemView")
        notifyItemChanged(position)
    }

}