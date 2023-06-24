package com.example.musttodolist.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musttodolist.MemoEditActivity
import com.example.musttodolist.adapter.MemoRVAdapter
import com.example.musttodolist.databinding.FragmentMemoBinding
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.viewModel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MemoFragment : Fragment() {
    private var _binding : FragmentMemoBinding? = null
    private val binding get() = _binding!!
    lateinit var memoRVAdapter: MemoRVAdapter
    lateinit var memoViewModel: MemoViewModel

    var checkBoxCount = 0
    var checkedItemList = mutableListOf<Long>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memoRVAdapter = MemoRVAdapter(requireContext())
        binding.memoRv.adapter = memoRVAdapter
        val layoutManager = GridLayoutManager(requireContext(), 3)
        //memoList에 값이 있나 없냐에 따라 셀의 크기를 정의
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                Log.d("getSpanSize",position.toString())
                return if (memoViewModel.memoList.value?.isEmpty() == true) {
                    layoutManager.spanCount // 빈 아이템일 경우 전체 너비로 확장

                } else {
                    1 // 일반 아이템일 경우 한 셀만 차지
                }
            }
        }
        binding.memoRv.layoutManager = layoutManager
        memoViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)

        memoViewModel.memoList.observe(viewLifecycleOwner){

            memoRVAdapter.update(it)
        }

        binding.memoAddBtn.setOnClickListener {
            val intent = Intent(requireContext(), MemoEditActivity::class.java).apply {
                putExtra("type","ADD")
            }
            requestActivity.launch(intent)
        }

        memoRVAdapter.setItemClickListener(object : MemoRVAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val selectedMemo = memoViewModel.memoSelectOne(itemId)

                    val intent = Intent(requireContext(), MemoEditActivity::class.java).apply {
                        putExtra("type","EDIT")
                        putExtra("item",selectedMemo)
                    }
                    requestActivity.launch(intent)

                }
            }

        })

        memoRVAdapter.setItemLongClickListener(object :MemoRVAdapter.ItemLongClickListener{
            override fun onLongClick(view: View, Position: Int, itemId: Long) {
                visibleCheckBox()
            }

        })

        //전체 선택 checkBox
        binding.memoAllCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                memoRVAdapter.selectAllMemo()
            } else {
                if(checkBoxCount == memoRVAdapter.itemCount){
                    memoRVAdapter.unSelectAllMemo()
                }
            }
        }

        memoRVAdapter.setItemCheckBoxCheckListener(object :MemoRVAdapter.ItemCheckBoxCheckListener{
            override fun onCheck(isCheck: Boolean, compoundButton: CompoundButton,itemId : Long) {
                Log.d("checkBoxIsCheck",isCheck.toString()+itemId.toString())
                if(isCheck){
                    checkBoxCount+=1
                    checkedItemList.add(itemId)
                    if(checkBoxCount == memoRVAdapter.itemCount && checkBoxCount>1){
                        binding.memoAllCheckBox.isChecked = true
                    }

                } else {
                    checkBoxCount-=1
                    checkedItemList.remove(itemId)
                    if(binding.memoAllCheckBox.isChecked){
                        binding.memoAllCheckBox.isChecked = false
                    }
                }
                Log.d("checkBox",isCheck.toString()+itemId.toString())
                Log.d("checkBoxList",checkedItemList.toString())
                Log.d("checkBoxCount",checkBoxCount.toString())


            }

        })
        binding.memoDeleteBtn.setOnClickListener {
            if(checkedItemList.isNotEmpty()){
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("삭제")
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("삭제",
                        DialogInterface.OnClickListener{
                                dialog, id ->
                            memoViewModel.memoList.value!!.forEach{
                                for(i in checkedItemList){
                                    if(i == it.memoId){
                                        memoViewModel.memoDelete(it)
                                    }
                                }

                            }
                            goneCheckBox()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener{
                                dialog, id ->
                        })
                builder.show()
            } else {
                Toast.makeText(requireContext(),"삭제할 메모를 선택해주세요.",Toast.LENGTH_SHORT).show()
            }
        }
        binding.memoCheckClearBtn.setOnClickListener {
            goneCheckBox()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onPause() {
        Log.d("MemoFragment","pause")
        goneCheckBox()

        super.onPause()
    }


    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val memoDTO : MemoDTO
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                memoDTO = it.data?.getSerializableExtra("memoDTO", MemoDTO::class.java)!!
            } else {
                memoDTO = (it.data?.getSerializableExtra("memoDTO") as? MemoDTO)!!
            }

            when(it.data?.getIntExtra("flag", -1)) {
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        memoViewModel.memoInsert(memoDTO)
                        //todoViewModel.insert(todo)를 통해
                        //  viewModel -> todoRepository -> todoDao 순으로 타고 들어가 데이터베이스에 저장하게 됩니다.
                    }
                    Toast.makeText(requireContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                1->{
                    CoroutineScope(Dispatchers.IO).launch {
                        memoViewModel.memoUpdate(memoDTO)
                    }
                    Toast.makeText(requireContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun visibleCheckBox(){
        if(memoRVAdapter.itemCount>1){
            binding.memoCheckBoxLy.visibility = View.VISIBLE
            binding.memoDeleteBtn.visibility = View.VISIBLE
            binding.memoCheckClearBtn.visibility = View.VISIBLE
        } else {
            binding.memoDeleteBtn.visibility = View.VISIBLE
            binding.memoCheckClearBtn.visibility = View.VISIBLE
        }

    }
    private fun goneCheckBox(){
        memoRVAdapter.ChkBoxHide()
        memoRVAdapter.unSelectAllMemo()
        memoRVAdapter.firstLongClick = true
        binding.memoCheckBoxLy.visibility = View.GONE
        binding.memoDeleteBtn.visibility = View.GONE
        binding.memoCheckClearBtn.visibility = View.GONE
        Log.d("MemoFragmentFirstLongClick",memoRVAdapter.firstLongClick.toString())

    }












}