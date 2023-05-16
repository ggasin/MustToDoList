package com.example.musttodolist.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musttodolist.R
import com.example.musttodolist.adapter.TodoRVAdapter
import com.example.musttodolist.data.todoData
import com.example.musttodolist.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val items = ArrayList<todoData>()


        val rvAdapter = TodoRVAdapter(items)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return _binding!!.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //Fragment에서 View Binding을 사용할 경우 Fragment는 View보다 오래 지속되어,
    //Fragment의 Lifecycle로 인해 메모리 누수가 발생할 수 있기 때문입니다.
    //예를들어 Fragment에서 Navigation component 또는 BackStack or detach를 사용하는 경우,
    // onDestroyView() 이후에 Fragment view는 종료되지만, Fragment는 여전히 살아 있습니다.
    // 즉 메모리 누수가 발생하게 됩니다.
    //▶ 그래서 반드시 binding 변수를 onDetsroyView() 이후에 null로 만들어 줘야합니다.


}