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

class InDoneTodoRVAdapter(val context : Context, val date : String) : RecyclerView.Adapter<InDoneTodoRVAdapter.ViewHolder>() {


    private var doneTodoList = mutableListOf<TodoDTO>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InDoneTodoRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.in_done_todo_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: InDoneTodoRVAdapter.ViewHolder, position: Int) {
        holder.onbind(doneTodoList[position])


    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val content = itemView.findViewById<TextView>(R.id.in_done_todo_item_content_tv)
        val completeIv = itemView.findViewById<ImageView>(R.id.in_done_todo_item_complete_iv) //완료 했다는 표시
        val incompleteIv= itemView.findViewById<ImageView>(R.id.in_done_todo_item_incomplete_iv) //완료를 아직 하지 않았다는 표시
        val deleteBtn = itemView.findViewById<ImageButton>(R.id.in_done_todo_item_delete_btn)
        val itemLayout = itemView.findViewById<LinearLayout>(R.id.in_done_todo_item_ly)

        fun onbind(data : TodoDTO){
            if(date == data.time){
                Log.d("InDoneDate",date)
                Log.d("InDoneDataTime",data.time)
                content.text = data.content
                if(data.complete ){
                    content.paintFlags =content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    completeIv.visibility = View.VISIBLE
                    incompleteIv.visibility = View.GONE
                } else {
                    content.paintFlags = content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

                    completeIv.visibility = View.GONE
                    incompleteIv.visibility = View.VISIBLE
                }

                deleteBtn.setOnClickListener {
                    itemDeleteBtnClickListener.onDeleteClick(it,layoutPosition,doneTodoList[layoutPosition].id)
                }
                itemView.setOnClickListener {
                    itemClickListener.onClick(it,layoutPosition,doneTodoList[layoutPosition].id)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return doneTodoList.size

    }

    interface ItemDeleteBtnClickListener{
        fun onDeleteClick(view: View, position: Int, itemId: Long)
    }
    interface ItemClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }


    private lateinit var itemDeleteBtnClickListener: ItemDeleteBtnClickListener
    private lateinit var itemClickListener: ItemClickListener

    fun setItemDeleteBtnClickListener(itemDeleteBtnClickListener: ItemDeleteBtnClickListener){
        this.itemDeleteBtnClickListener = itemDeleteBtnClickListener
    }
    fun setItemClickListener(itemClickListener : ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    fun update(newList : MutableList<TodoDTO>){
        this.doneTodoList = newList
        for(i in doneTodoList){
            Log.d("InDoneTodo",i.content)
        }
        Log.d("InDoneTodo","---------------------------")


        notifyDataSetChanged()
    }



}