package com.example.musttodolist.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.viewModel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TodoRVAdapter (val context: Context) : RecyclerView.Adapter<TodoRVAdapter.ViewHolder>(){


    private var currentList = mutableListOf<TodoDTO>()



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

        fun onbind(data: TodoDTO){
            content.text = data.content
            if(data.complete ){
                content.paintFlags =content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemLayout.setBackgroundResource(R.drawable.todo_rv_item_complete_background)
                completeBtn.visibility = View.GONE
                completeCancelBtn.visibility = View.VISIBLE
                Log.d("adapterOnbind","complete")
            } else {
                content.paintFlags = content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                itemLayout.setBackgroundResource(R.drawable.todo_rv_item_background)
                completeBtn.visibility = View.VISIBLE
                completeCancelBtn.visibility = View.GONE
                Log.d("adapterOnbind","incomplete")
            }
            itemView.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,currentList[layoutPosition].id)
            }

            completeBtn.setOnClickListener {
                updateItemView(layoutPosition) // Update the view for the clicked item
                itemCompleteBtnClickListener.onClick(it,layoutPosition,currentList[layoutPosition].id)
            }
            completeCancelBtn.setOnClickListener {
                updateItemView(layoutPosition) // Update the view for the clicked item
                itemCompleteCancelClickListener.onClick(it,layoutPosition,currentList[layoutPosition].id)
            }
           deleteBtn.setOnClickListener {

                itemDeleteBtnClickListener.onClick(it,layoutPosition,currentList[layoutPosition].id)
            }
        }

    }

    override fun onBindViewHolder(holder: TodoRVAdapter.ViewHolder, position: Int) {
        holder.onbind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }



    fun update(newList : MutableList<TodoDTO>){
        this.currentList = newList

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
    private fun updateItemView(position: Int) {
        Log.d("adapter","updateItemView")
        notifyItemChanged(position)
    }






}