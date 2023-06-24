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
import com.example.musttodolist.dto.MemoDTO

class MemoRVAdapter (val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    private val VIEW_TYPE_ITEM = 1
    private val VIEW_TYPE_EMPTY = 2
    private var memoList = mutableListOf<MemoDTO>()
    var isHiddenChkBoxVisible = false
    var allChkBoxChecked = false
    var longClickedItemId:Long? = null
    var firstLongClick = true





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
        return when(viewType){
            VIEW_TYPE_ITEM -> {
                val itemView = v.inflate(R.layout.memo_rv_item,parent,false)
                ViewHolder(itemView)
            }
            VIEW_TYPE_EMPTY ->{
                val itemView = v.inflate(R.layout.memo_empty,parent,false)
                EmptyHolder(itemView)

            } else -> throw ClassCastException("Unknown viewType in MemoAd $viewType")
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(memoList.size!=0){
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_EMPTY
        }
    }
    inner class EmptyHolder(itemView : View) : RecyclerView.ViewHolder(itemView){}
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.memo_title_tv)
        val content = itemView.findViewById<TextView>(R.id.memo_content_tv)
        val time = itemView.findViewById<TextView>(R.id.memo_time_tv)
        val chkBox = itemView.findViewById<CheckBox>(R.id.memo_check_box)


        fun onBind(data: MemoDTO){
            title.text = data.memoTitle
            content.text = data.memoContent
            time.text = data.memoTime

            //isHiddenChkBoxVisible은  longClickChkBoxUpdate()에서 값이 바뀌고 true가 되면 숨겨져 있던 checkbox layout이 보여진다.
            //아이템을 long click할 시 아이템의 체크박스가 보이는 걸 구현 한 것.
            chkBox.visibility = if(isHiddenChkBoxVisible) View.VISIBLE else View.GONE

            //전체 선택이 체크되어 allChkBoxChecked가 true 면 바인딩 되는 chkBox들을 하나씩 true로, 아니라면 false로.
            //allChkBoxChecked의 값을 바꾸는 selectAllMemo()와 unSelectAllMemo() 메소드는 MemoFragment에서
            //전체 선택 checkBox의 check 이벤트가 발생할 때 호출.
            chkBox.isChecked = if(allChkBoxChecked) true else false


            //long click시 long click을 한 아이템의 checkBox는 check가 되어있는 상태로 만들고
            //해당하는 아이템의 checkBox가 체크되고, 첫 long click 시에만 호출되도록 if조건문 구성.
            if (longClickedItemId == memoList[layoutPosition].memoId && firstLongClick){
                chkBox.isChecked = true
                firstLongClick = false
                longClickedItemId = null

            }

            //아이템 클릭 이벤트
            itemView.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,memoList[layoutPosition].memoId)
            }

            //아이템 long click 이벤트
            itemView.setOnLongClickListener {
                //첫 long click일 때에만 long click의 작업을 수행하도록.
                //첫 long click 이후엔 long click을 사용할 일도 없을 뿐더러, 풀어두면 에러 발생.
                if(firstLongClick){
                    itemLongClickListener.onLongClick(it,layoutPosition,memoList[layoutPosition].memoId)
                    longClickedItemId = memoList[layoutPosition].memoId //long click을 한 item의 id를 저장
                    longClickChkBoxUpdate()
                }
                true
            }

            chkBox.setOnCheckedChangeListener { compoundButton, b ->
                itemCheckBoxCheckListener.onCheck(b,compoundButton,memoList[layoutPosition].memoId)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder ->{
                holder.onBind(memoList[position])
            }
            is EmptyHolder -> {}
        }


    }

    override fun getItemCount(): Int {
        return if(memoList.size==0) 1 else memoList.size
    }

    fun update(newList : MutableList<MemoDTO>){
        this.memoList = newList
        notifyDataSetChanged()
    }

    //long click시 isHiddenChkBoxVisible의 값을 true로 바꾸고 adapter 업데이트
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