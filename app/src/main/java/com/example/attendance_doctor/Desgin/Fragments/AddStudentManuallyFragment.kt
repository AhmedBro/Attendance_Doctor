package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.attendance_doctor.Data.LectureAttendanceViewModel
import com.example.attendance_doctor.Data.Student
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_add_student_manually.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class AddStudentManuallyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_student_manually, container, false)
        var addButton = view.findViewById<Button>(R.id.mAddBtn)
//        var addButton = view.findViewById<>(R.id.mAddBtn)
//        var addButton = view.findViewById<>(R.id.mAddBtn)
        var lectureID = AddStudentManuallyFragmentArgs.fromBundle(requireArguments()).lectureID
        var courseCode = AddStudentManuallyFragmentArgs.fromBundle(requireArguments()).courseCode
        var addStudentManuallyViewModel =
            ViewModelProvider(this).get(LectureAttendanceViewModel::class.java)

        addButton.setOnClickListener {
            var studentID = mStudentIdEt.text
            var studentName = mStudentNameEt.text
            lifecycleScope.launch(Dispatchers.IO) {
                if (studentID.trim().isNotEmpty() && studentName.trim().isNotEmpty()) {
                    addStudentManuallyViewModel.addManually(
                        Student(
                            studentID.toString(),
                            studentName.toString()
                        ), lectureID, courseCode
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Enter both, student ID and name",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        addStudentManuallyViewModel.error.observe(viewLifecycleOwner , Observer {
            Toast.makeText(
                requireContext(),
                "${it.toString()}",
                Toast.LENGTH_LONG
            ).show()
        })





        return view
    }

}