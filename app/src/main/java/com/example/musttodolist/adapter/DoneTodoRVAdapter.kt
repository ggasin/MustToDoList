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




class DoneTodoRVAdapter(val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1
    private val VIEW_TYPE_EMPTY = 2




    private var dateList = mutableListOf<String>()
    private var doneTodoList = mutableListOf<TodoDTO>()
    lateinit var inDoneTodoRVAdapter: InDoneTodoRVAdapter


    //recyclerview 호출 순서
    //getItemCount() 아이템 몇개인지 판단
    //-> getItemViewType() 현재 아이템뷰의 포지션에 해당하는 뷰타입 판단
    //-> onCreateViewHolder() 뷰타입에 해당하는 뷰홀더를 생성하여 리턴
    //-> onBindViewHolder() 생성된 뷰홀더와 포지션 전달받아 현재 포지션에 맞는 데이터를 뷰홀더가 관리하는 뷰들에 바인딩
    //즉, 화면에 4개의 리스트가 보인다면 맨 처음 getItemCount() 불리고 그 후 4번 etItemViewType, onCreateViewHolder, onBindViewHolder 가 연속적으로 호출됨




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val headerView = inflater.inflate(R.layout.done_todo_rv_header, parent, false)
                HeaderViewHolder(headerView)
            }
            VIEW_TYPE_ITEM -> {
                val itemView = inflater.inflate(R.layout.done_todo_rv_item, parent, false)
                ViewHolder(itemView)
            }
            VIEW_TYPE_EMPTY ->{
                val emptyView = inflater.inflate(R.layout.todo_empty, parent, false)
                EmptyViewHolder(emptyView)
            } else ->{
                throw ClassCastException("Unknown viewType $viewType")
            }
        }


    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Reference UI elements in the header view
        // Example:
        // val headerTitle = itemView.findViewById<TextView>(R.id.header_title)

        // Bind data to the header view
        // Example:
        // fun bind(headerData: String) {
        //     headerTitle.text = headerData
        // }
    }
    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.done_todo_item_date_tv)
        val itemRecyclerView = itemView.findViewById<RecyclerView>(R.id.done_todo_in_rv)

        fun onBind(data: String){
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
            inDoneTodoRVAdapter.setItemDeleteBtnClickListener(object : InDoneTodoRVAdapter.ItemDeleteBtnClickListener{
                override fun onDeleteClick(view: View, position: Int, itemId: Long) {
                    itemDeleteBtnClickListener.onDeleteClick(view,position,itemId)
                }
            })

        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            // HEADER
            is HeaderViewHolder -> {}
            // ITEM
            is ViewHolder -> {
                holder.onBind(dateList[position-1])
            }

            is EmptyViewHolder -> {}
        }

    }

    override fun getItemCount(): Int {
        return if (dateList.size == 0) 1 else dateList.size + 1
    }
    override fun getItemViewType(position: Int): Int {
        return if (dateList.size != 0) {
            if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_EMPTY
        }
    }
    fun update(newList : MutableList<String>){
        this.dateList = newList
        dateList.sortDescending()
        Log.d("DoneTodoAdapterTimeList",newList.toString())

        notifyDataSetChanged()
    }
    fun updateDoneTodoList(newList : MutableList<TodoDTO>){
        this.doneTodoList = newList
        for(i in doneTodoList){
            Log.d("InDoneBefore",i.content + i.time)
        }

    }
    interface ItemDeleteBtnClickListener{
        fun onDeleteClick(view: View, position: Int, itemId: Long)
    }

    private lateinit var itemDeleteBtnClickListener: ItemDeleteBtnClickListener


    fun setItemDeleteBtnClickListener(itemDeleteBtnClickListener: ItemDeleteBtnClickListener){
        this.itemDeleteBtnClickListener = itemDeleteBtnClickListener
    }



}