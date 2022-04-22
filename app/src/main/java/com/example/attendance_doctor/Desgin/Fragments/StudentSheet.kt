package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendance_doctor.Data.LectureAttendanceViewModel
import com.example.attendance_doctor.Data.Student
import com.example.attendance_doctor.Desgin.adapters.StudentAdapter
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_student_sheet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StudentSheet : Fragment() {
    lateinit var StudentList:ArrayList<Student>
lateinit var CourseID:String
lateinit var LectureID:String
lateinit var lectureAttendanceViewModel: LectureAttendanceViewModel
var adapter = StudentAdapter()

    override fun onStart() {
        super.onStart()
        lectureAttendanceViewModel.showProgressbar
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_sheet, container, false)
       lectureAttendanceViewModel=ViewModelProvider(this).get(LectureAttendanceViewModel::class.java)
        StudentList= arrayListOf()
        CourseID=StudentSheetArgs.fromBundle(requireArguments()).courseCode
        LectureID=StudentSheetArgs.fromBundle(requireArguments()).lectureName
        val rv = view.findViewById<RecyclerView>(R.id.mStudentsRecycler)
        val manager = LinearLayoutManager(this.context , LinearLayoutManager.VERTICAL , false)
        rv.layoutManager = manager
        lifecycleScope.launch(Dispatchers.Main) {
            lectureAttendanceViewModel.getLectures(CourseID, LectureID)
        }
        lectureAttendanceViewModel.doneRetrieving.observe(viewLifecycleOwner, Observer {
            if (it){
                StudentList=lectureAttendanceViewModel.Students
                adapter.submitList(StudentList)
                rv.adapter=adapter
                lectureAttendanceViewModel.doneRetrievingdata()
            }
        })
        lectureAttendanceViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            if (it){
                mStudentsProgressBar.visibility=View.VISIBLE
            }else{
                mStudentsProgressBar.visibility=View.GONE

            }
        })
        lectureAttendanceViewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context,it,Toast.LENGTH_LONG).show()
        })
        view.findViewById<Button>(R.id.mShowQrCodeButton).setOnClickListener {
            findNavController().navigate(StudentSheetDirections.actionStudentSheetToQrCodeGenerated(LectureID,CourseID))
        }


        return view
    }

}