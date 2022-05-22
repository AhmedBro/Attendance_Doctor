package com.example.attendance_doctor.Desgin.Fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import java.io.File
import java.io.FileOutputStream
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.red
import android.net.Uri
import android.provider.Settings
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.attendance_doctor.Desgin.Activities.MainActivity
import org.apache.poi.ss.usermodel.HorizontalAlignment


class StudentSheet : Fragment() {
    lateinit var StudentList: ArrayList<Student>
    lateinit var CourseID: String
    lateinit var LectureID: String
    lateinit var lectureAttendanceViewModel: LectureAttendanceViewModel
    var adapter = StudentAdapter()

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_sheet, container, false)
        lectureAttendanceViewModel =
            ViewModelProvider(this).get(LectureAttendanceViewModel::class.java)
        StudentList = arrayListOf()
        CourseID = StudentSheetArgs.fromBundle(requireArguments()).courseCode
        LectureID = StudentSheetArgs.fromBundle(requireArguments()).lectureName
        val rv = view.findViewById<RecyclerView>(R.id.mStudentsRecycler)
        val tv = view.findViewById<TextView>(R.id.mAddStudentManually)
        tv.setOnClickListener {
            findNavController().navigate(StudentSheetDirections.actionStudentSheetToAddStudentManuallyFragment(CourseID , LectureID+"*${StudentSheetArgs.fromBundle(requireArguments()).courseCodeOnly}"))
        }
        val manager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = manager
        lifecycleScope.launch(Dispatchers.Main) {
            lectureAttendanceViewModel.getLectures(
                CourseID,
                LectureID + "*${StudentSheetArgs.fromBundle(requireArguments()).courseCodeOnly}"
            )
        }
        lectureAttendanceViewModel.doneRetrieving.observe(viewLifecycleOwner, Observer {
            if (it) {
                StudentList = lectureAttendanceViewModel.Students
                if (StudentList.isEmpty()) {
                    mAlterStudentsTv.visibility = View.VISIBLE
                } else {
                    mAlterStudentsTv.visibility = View.GONE
                }
                adapter.submitList(StudentList)
                rv.adapter = adapter
                lectureAttendanceViewModel.doneRetrievingdata()
            }
        })
        lectureAttendanceViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            if (it) {
                mStudentsProgressBar.visibility = View.VISIBLE
            } else {
                mStudentsProgressBar.visibility = View.GONE

            }
        })
        lectureAttendanceViewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context, it, Toast.LENGTH_LONG).show()
        })
        view.findViewById<Button>(R.id.mShowQrCodeButton).setOnClickListener {
            findNavController().navigate(
                StudentSheetDirections.actionStudentSheetToQrCodeGenerated(
                    LectureID + "*${StudentSheetArgs.fromBundle(requireArguments()).courseCodeOnly}",
                    CourseID
                )
            )
        }
        view.findViewById<Button>(R.id.mDownloadSheet).setOnClickListener {
            lectureAttendanceViewModel.showProgressBar()
            if (StudentList.isEmpty()) {
                Toast.makeText(this.context, "There is no Students", Toast.LENGTH_SHORT).show()
                lectureAttendanceViewModel.hideProgressBar()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        try {
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            intent.addCategory("android.intent.category.DEFAULT")
                            intent.data =
                                Uri.parse(
                                    String.format(
                                        "package:%s",
                                        MainActivity.context!!.packageName
                                    )
                                )
                            startActivityForResult(intent, 2296)
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()

                            val intent = Intent()
                            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                            startActivityForResult(intent, 2296)
                        }

                    } else {
                        Download()
                    }
                } else {
                    Download()
                }

            }

        }



        return view
    }

    fun Download() {

        var path = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Attendance/${CourseID}", "/${LectureID}.xls"
        )
        var hSSFWorkbook = HSSFWorkbook()
        var style = hSSFWorkbook.createCellStyle()
        style.setFillForegroundColor(IndexedColors.RED.getIndex())
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        style.setAlignment(HorizontalAlignment.CENTER)
        var style2 = hSSFWorkbook.createCellStyle()
        style2.setAlignment(HorizontalAlignment.CENTER)
        var hssSheet = hSSFWorkbook.createSheet("Attendance")
        hssSheet.setColumnWidth(0, 24 * 256)
        hssSheet.setColumnWidth(1, 20 * 256)
        var nameRow = hssSheet.createRow(0)
        var IDRow = hssSheet.createRow(0)
        nameRow.createCell(0).apply {
            setCellValue("Student Name")
            setCellStyle(style)

        }
        IDRow.createCell(1).apply {
            setCellValue("ID")
            setCellStyle(style)
        }
        for (i in 0 until StudentList.size) {
            Log.e("check", StudentList[i].StudentName.toString())
            hssSheet.createRow(i + 1).apply {

                createCell(1).apply {
                    setCellValue(StudentList[i].StudentID)
                    setCellStyle(style2)

                }
                createCell(0).apply {
                    setCellValue(StudentList[i].StudentName.toString())
                    setCellStyle(style2)
                }
            }
        }

        Log.e("row", "rowCreated")
        try {
            if (!path.parentFile.exists()) {
                path.parentFile.mkdirs()
            }
            if (!path.exists()) {
                path.createNewFile()
            }
            var FileOutputStream = FileOutputStream(path)
            Log.e("done2", "done")
            hSSFWorkbook.write(FileOutputStream)
            Log.e("done1", "done")
            FileOutputStream.flush()
            Log.e("done", "done")
            FileOutputStream.close()
            Toast.makeText(this.context, "Downloaded", Toast.LENGTH_SHORT).show()
            lectureAttendanceViewModel.hideProgressBar()

        } catch (e: Exception) {
            Log.e("ndone", e.toString())
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            lectureAttendanceViewModel.hideProgressBar()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2296) {
            lectureAttendanceViewModel.hideProgressBar()
            Download()
        }
    }
}
