package com.example.musttodolist.fragment

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.musttodolist.ItemDetailActivity
import com.example.musttodolist.MemoEditActivity
import com.example.musttodolist.R
import com.example.musttodolist.TodoAddActivity
import com.example.musttodolist.adapter.MemoRVAdapter
import com.example.musttodolist.databinding.FragmentMemoBinding
import com.example.musttodolist.dto.MemoDTO
import com.example.musttodolist.dto.TodoDTO
import com.example.musttodolist.viewModel.MemoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MemoFragment : Fragment() {
    private var _binding : FragmentMemoBinding? = null
    private val binding get() = _binding!!
    lateinit var memoRVAdapter: MemoRVAdapter
    lateinit var memoViewModel: MemoViewModel



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

            }

        })



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











}