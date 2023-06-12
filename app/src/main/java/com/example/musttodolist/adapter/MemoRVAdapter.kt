package com.example.musttodolist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.dto.TodoDTO

class MemoRVAdapter (val context: Context) : RecyclerView.Adapter<MemoRVAdapter.ViewHolder>(){


    private var memoList = mutableListOf<MemoDTO>()
    var isHiddenChkBox = false



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.memo_rv_item,parent,false)
        return ViewHolder(v)

    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.memo_title_tv)
        val content = itemView.findViewById<TextView>(R.id.memo_content_tv)
        val time = itemView.findViewById<TextView>(R.id.memo_time_tv)
        val chkBox = itemView.findViewById<CheckBox>(R.id.memo_check_box)


        fun onbind(data: MemoDTO){
            title.text = data.memoTitle
            content.text = data.memoContent
            time.text = data.memoTime
            chkBox.visibility = if(isHiddenChkBox) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,memoList[layoutPosition].memoId)
            }
            itemView.setOnLongClickListener {
                itemLongClickListener.onLongClick(it,layoutPosition,memoList[layoutPosition].memoId)
                longClickChkBoxUpdate()
                true

            }


        }

    }

    override fun onBindViewHolder(holder: MemoRVAdapter.ViewHolder, position: Int) {
        holder.onbind(memoList[position])

    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    fun update(newList : MutableList<MemoDTO>){
        this.memoList = newList
        notifyDataSetChanged()
    }
    fun longClickChkBoxUpdate(){
        isHiddenChkBox = true
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }
    interface ItemLongClickListener{
        fun onLongClick(view:View,Position:Int,itemId: Long)
    }
    private lateinit var itemClickListener: ItemClickListener
    private lateinit var itemLongClickListener: ItemLongClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    fun setItemLongClickListener(itemLongClickListener: ItemLongClickListener){
        this.itemLongClickListener = itemLongClickListener
    }







}