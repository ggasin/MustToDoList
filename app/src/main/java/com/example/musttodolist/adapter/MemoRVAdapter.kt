package com.example.musttodolist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musttodolist.R
import com.example.musttodolist.databinding.FragmentMemoBinding
import com.example.musttodolist.dto.MemoDTO

class MemoRVAdapter (val context: Context) : RecyclerView.Adapter<MemoRVAdapter.ViewHolder>(){


    private var memoList = mutableListOf<MemoDTO>()
    var isHiddenChkBoxVisible = false
    var allChkBoxChecked = false
    var isLongClickedItem = false





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
            chkBox.visibility = if(isHiddenChkBoxVisible) View.VISIBLE else View.GONE
            chkBox.isChecked = if(allChkBoxChecked) true else false
            Log.d("checkBoxOnBind",chkBox.isChecked.toString()+"+"+data.memoTitle)
            itemView.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,memoList[layoutPosition].memoId)
            }
            itemView.setOnLongClickListener {
                itemLongClickListener.onLongClick(it,layoutPosition,memoList[layoutPosition].memoId)
                chkBox.isChecked = true
                longClickChkBoxUpdate()

                true
            }
            chkBox.setOnCheckedChangeListener { compoundButton, b ->
                Log.d("checkBoxOnAdapter",chkBox.isChecked.toString()+"+"+data.memoTitle)
                itemCheckBoxCheckListener.onCheck(b,compoundButton,memoList[layoutPosition].memoId)
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
        isHiddenChkBoxVisible = true
        notifyDataSetChanged()
    }
    fun ChkBoxHide(){
        isHiddenChkBoxVisible = false
        notifyDataSetChanged()
    }
    fun selectAllMemo(){
        allChkBoxChecked = true
        notifyDataSetChanged()
    }
    fun unSelectAllMemo(){
        allChkBoxChecked = false
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int, itemId: Long)
    }
    interface ItemLongClickListener{
        fun onLongClick(view:View,Position:Int,itemId: Long)
    }
    interface ItemCheckBoxCheckListener{
        fun onCheck(isCheck:Boolean, compoundButton: CompoundButton , itemId: Long)
    }
    private lateinit var itemClickListener: ItemClickListener
    private lateinit var itemLongClickListener: ItemLongClickListener
    private lateinit var itemCheckBoxCheckListener: ItemCheckBoxCheckListener


    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    fun setItemLongClickListener(itemLongClickListener: ItemLongClickListener){
        this.itemLongClickListener = itemLongClickListener
    }
    fun setItemCheckBoxCheckListener(itemCheckBoxCheckListener: ItemCheckBoxCheckListener){
        this.itemCheckBoxCheckListener = itemCheckBoxCheckListener
    }







}