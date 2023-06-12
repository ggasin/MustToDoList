package com.example.musttodolist.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musttodolist.MemoEditActivity
import com.example.musttodolist.adapter.MemoRVAdapter
import com.example.musttodolist.databinding.FragmentMemoBinding
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.viewModel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        binding.memoRv.layoutManager = GridLayoutManager(requireContext(),3)
        binding.memoRv.adapter = memoRVAdapter

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
                Log.d("MemoFragment",itemId.toString())
            }

        })
        binding.memoAllCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                memoRVAdapter.selectAllMemo()
            } else {
                Log.d("allCheckNotBox","call")
                if(checkBoxCount == memoRVAdapter.itemCount){
                    memoRVAdapter.unSelectAllMemo()
                }
            }
        }

        memoRVAdapter.setItemCheckBoxCheckListener(object :MemoRVAdapter.ItemCheckBoxCheckListener{
            override fun onCheck(isCheck: Boolean, compoundButton: CompoundButton,itemId : Long) {
                if(isCheck){
                    checkBoxCount+=1
                    checkedItemList.add(itemId)
                    if(checkBoxCount == memoRVAdapter.itemCount){
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
        memoRVAdapter.ChkBoxHide()
        memoRVAdapter.unSelectAllMemo()
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
        binding.memoCheckBoxLy.visibility = View.VISIBLE
        binding.memoDeleteBtn.visibility = View.VISIBLE
    }
    private fun goneCheckBox(){
        binding.memoCheckBoxLy.visibility = View.GONE
        binding.memoDeleteBtn.visibility = View.GONE
    }











}