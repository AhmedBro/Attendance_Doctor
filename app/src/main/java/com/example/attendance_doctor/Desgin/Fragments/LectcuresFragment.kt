package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_lectcures.*

class LectcuresFragment : Fragment() {
lateinit var mCourse: Course

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lectcures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCourse=LectcuresFragmentArgs.fromBundle(requireArguments()).course
        mCourseName.text = mCourse.courseName


    }

}