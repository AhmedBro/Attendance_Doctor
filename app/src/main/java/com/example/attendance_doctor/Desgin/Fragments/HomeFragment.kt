package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.Data.DoctorDataViewModel
import com.example.attendance_doctor.Data.Teacher
import com.example.attendance_doctor.Desgin.Activities.MainActivity
import com.example.attendance_doctor.Desgin.adapters.CourseListAdapter
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    lateinit var mSharedPreferences: SharedPreferences
    lateinit var teacherID :String
   // Amr Ghomiem1155
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mSharedPreferences = context?.let { SharedPreferences(it) }!!
        teacherID= mSharedPreferences.getId().toString()
       view.findViewById<TextView>(R.id.mChangePassword).setOnClickListener {
           findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToChangePasswordFragment())
       }

        return view
    }
    lateinit var doctorDataViewModel: DoctorDataViewModel

    var mTeacher =Teacher()
   // Teacher("33","Ashraf","234", arrayListOf("CS-300Group 1","IS-300Group 1"))
    lateinit var mTeacherCourses : ArrayList<Course>
    lateinit var adapter: CourseListAdapter

    override fun onStart() {
        super.onStart()
        getTeacherData()
        doctorDataViewModel.showProgressBar()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorDataViewModel= ViewModelProvider(this).get(DoctorDataViewModel::class.java)
        mTeacherCourses= arrayListOf()


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
                mTeacher=doctorDataViewModel.teacherdata
                mGuide.text=" Hello Dr/ ${mTeacher.teacherName + getString(R.string.doctor_guide)}"
                getTeacherCourses()

            }else{}
        })
        doctorDataViewModel.doneRetrievingcourses.observe(viewLifecycleOwner, Observer {
            if (it){
                adapter= CourseListAdapter(mTeacherCourses)
                Log.e("testtttt",mTeacherCourses.size.toString())
                mTableRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                mTableRecyclerView.adapter=adapter

            }
            adapter.setOnItemClickListener {course->
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLectcuresFragment(course))
            }

        })



    }



    private fun getTeacherData() {
        lifecycleScope.launch(Dispatchers.IO) {
             async { teacherID?.let { doctorDataViewModel.getDoctorData(it) } }.await()
        }
    }
    private fun getTeacherCourses() {
        lifecycleScope.launch(Dispatchers.IO) {
            mTeacherCourses = async { doctorDataViewModel.getDoctorCourses(mTeacher.CoursesId) }.await()
           // mTeacherCourses = async { doctorDataViewModel.getDoctorCourses(arrayListOf("CS-300Group 1","IS-300Group 1")) }.await()
        }

    }

}