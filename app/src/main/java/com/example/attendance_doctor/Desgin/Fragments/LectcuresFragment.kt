package com.example.attendance_doctor.Desgin.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.Data.LecturesViewModel
import com.example.attendance_doctor.Desgin.adapters.LecturesAdapter
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_lectcures.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class LectcuresFragment : Fragment() {
    lateinit var mCourse: Course
    lateinit var lecturesViewModel: LecturesViewModel
    lateinit var lectures: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lectcures, container, false)
    }

    override fun onStart() {
        super.onStart()
        lecturesViewModel.showProgressBar()


    }

    lateinit var adapter: LecturesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lectures = arrayListOf()
        lecturesViewModel = ViewModelProvider(this).get(LecturesViewModel::class.java)
        mCourse = LectcuresFragmentArgs.fromBundle(requireArguments()).course
        lifecycleScope.launch(Dispatchers.Main) {
            lecturesViewModel.getLectures(mCourse.courseCode + mCourse.courseGroup)
        }
        mCourseName.text = mCourse.courseName
        lecturesViewModel.doneRetrieving.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                lectures = lecturesViewModel.lectures
                Log.e("tttt", lectures.size.toString())
                adapter = LecturesAdapter(lectures)
                mStudentsRecycler.layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                if (lectures.isEmpty()) {
                    Toast.makeText(this.context, "There is no lectures yet", Toast.LENGTH_LONG)
                        .show()
                } else {
                    mStudentsRecycler.adapter = adapter
                }
                adapter.setOnItemClickListener { Lecture ->
                    findNavController().navigate(
                        LectcuresFragmentDirections.actionLectcuresFragmentToStudentSheet(
                            Lecture,
                            mCourse.courseCode + mCourse.courseGroup
                        )
                    )

                }

                lecturesViewModel.doneRetrievingdata()
            }

        })
        lecturesViewModel.showProgressbar.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                progressBar4.visibility = View.VISIBLE
            } else {
                progressBar4.visibility = View.GONE
            }
        })

        mGenerateQr.setOnClickListener {
            val calendar = Calendar.getInstance()
            val Format = SimpleDateFormat("yyyy-MM-dd")
            val Date = Format.format(calendar.time).toString()
            val date : StringBuilder = StringBuilder()
            for (i in Date){
                when(i){
                    '٠' -> date.append('0')
                    '١' -> date.append('1')
                    '٢' -> date.append('2')
                    '٣' -> date.append('3')
                    '٤' -> date.append('4')
                    '٥' -> date.append('5')
                    '٦' -> date.append('6')
                    '٧' -> date.append('7')
                    '٨' -> date.append('8')
                    '٩' -> date.append('9')
                    '0' -> date.append('0')
                    '1' -> date.append('1')
                    '2' -> date.append('2')
                    '3' -> date.append('3')
                    '4' -> date.append('4')
                    '5' -> date.append('5')
                    '6' -> date.append('6')
                    '7' -> date.append('7')
                    '8' -> date.append('8')
                    '9' -> date.append('9')
                    else -> date.append('-')
                }
            }
            findNavController().navigate(
                LectcuresFragmentDirections.actionLectcuresFragmentToQrCodeGenerated(
                    date.toString(),
                    mCourse.courseCode + mCourse.courseGroup
                )
            )
        }


    }

}