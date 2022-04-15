package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_doctor.Data.Student
import com.example.attendance_doctor.Desgin.adapters.StudentAdapter
import com.example.attendance_doctor.R


class StudentSheet : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_sheet, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.mStudentsRecycler)

        val manager = GridLayoutManager(activity, 3)

        rv.layoutManager = manager


        var students = mutableListOf(
            Student("20180512", "20180512", "محمد عادل عبدالله", null),
            Student("20180512", "20180512", "محمد عادل عبدالله", null),
            Student("20180512", "20180512", "محمد عادل عبدالله", null),
            Student("20180512", "20180512", "محمد عادل عبدالله", null),
            Student("20180512", "20180512", "محمد عادل عبدالله", null),
            Student("20180512", "20180512", "محمد عادل عبدالله", null),
            Student("20180512", "20180512", "محمد عادل عبدالله", null)
        )

        var adapter = StudentAdapter()

        adapter.submitList(students)

        return view
    }

}