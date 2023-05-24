package com.example.musttodolist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musttodolist.R
import com.example.musttodolist.adapter.TodoRVAdapter
import com.example.musttodolist.databinding.FragmentCalendarBinding
import com.example.musttodolist.databinding.FragmentHomeBinding
import com.example.musttodolist.viewModel.TodoViewModel


class CalendarFragment : Fragment() {

    private var _binding:FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    lateinit var todoViewModel: TodoViewModel
    lateinit var todoAdapter: TodoRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoAdapter = TodoRVAdapter(requireContext())
        binding.calendarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.calendarRecyclerView.adapter = todoAdapter

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

            todoViewModel.calendarTodoList(formattedDate).observe(viewLifecycleOwner) { items ->
                todoAdapter.update(items)
            }


        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}