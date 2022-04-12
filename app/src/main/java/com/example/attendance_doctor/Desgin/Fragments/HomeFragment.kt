package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.Data.DoctorDataViewModel
import com.example.attendance_doctor.Data.Teacher
import com.example.attendance_doctor.Desgin.adapters.CourseListAdapter
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {
    lateinit var doctorDataViewModel: DoctorDataViewModel
    var teacherID : String="2233"
     var mTeacher =Teacher()
   // Teacher("33","Ashraf","234", arrayListOf("CS-300Group 1","IS-300Group 1"))
    lateinit var mTeacherCourses : ArrayList<Course>
    lateinit var adapter: CourseListAdapter

    override fun onStart() {
        super.onStart()
        getTeacherData()
        getTeacherCourses()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorDataViewModel= ViewModelProvider(this).get(DoctorDataViewModel::class.java)
        mTeacherCourses= arrayListOf()

        doctorDataViewModel.showProgressbar

        doctorDataViewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })
        doctorDataViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            if (it) {
                mTeacherTableProgressBar.visibility = View.VISIBLE
            } else {
                mTeacherTableProgressBar.visibility = View.GONE
            }
        })
        doctorDataViewModel.doneRetrieving.observe(viewLifecycleOwner, Observer {
            if (it){
                mTeacher_Name.text=" Hello Dr/ ${mTeacher.teacherName}"
                adapter= CourseListAdapter(mTeacherCourses)
                Log.e("testtttt",mTeacherCourses.size.toString())
                mTableRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                mTableRecyclerView.adapter=adapter
                doctorDataViewModel.doneRetrievingdata()
            }else{}
        })

    }



    private fun getTeacherData() {
        lifecycleScope.launch(Dispatchers.IO) {
            mTeacher = async { doctorDataViewModel.getDoctorData(teacherID) }.await()
        }
    }
    private fun getTeacherCourses() {
        lifecycleScope.launch(Dispatchers.IO) {
            mTeacherCourses = async { doctorDataViewModel.getDoctorCourses(mTeacher.CoursesId) }.await()
           // mTeacherCourses = async { doctorDataViewModel.getDoctorCourses(arrayListOf("CS-300Group 1","IS-300Group 1")) }.await()
        }

    }

}