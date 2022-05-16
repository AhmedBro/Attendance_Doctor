package com.example.attendance_doctor.Desgin.Fragments

//import com.monitorjbl.xlsx.StreamingReader
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.Data.LecturesViewModel
import com.example.attendance_doctor.Desgin.adapters.LecturesAdapter
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_lectcures.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

import android.os.Build
import android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
import android.provider.MediaStore
import android.provider.DocumentsContract

import android.content.ContentUris
import com.example.attendance_doctor.Desgin.Activities.MainActivity
import com.example.attendance_doctor.Desgin.util.RealPathUtil
import com.monitorjbl.xlsx.StreamingReader
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.InputStream


class LectcuresFragment : Fragment() {
    lateinit var mCourse: Course
    lateinit var lecturesViewModel: LecturesViewModel
    lateinit var lectures: ArrayList<String>
    val GET_FIle_CODE = 1000

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
                            mCourse.courseCode + mCourse.courseGroup,mCourse.courseCode!!
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
            val date: StringBuilder = StringBuilder()
            for (i in Date) {
                when (i) {
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
                    date.toString()+"*${mCourse.courseCode}",
                    mCourse.courseCode + mCourse.courseGroup
                )
            )
        }

        askForPermissions()
        mUploadStudentSheet.setOnClickListener {

            getFile()
        }

    }

    private fun getFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/vnd.ms-excel"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, GET_FIle_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("EEEEEEEEEEEEEEEEE" , data!!.getData().toString())

        if (requestCode==GET_FIle_CODE && resultCode== Activity.RESULT_OK){

//            val path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS
//            )
//            val file: File? = File(path, data!!.data!!.path)
//
//            if (!file!!.parentFile.exists()) {
//                file!!.parentFile.mkdirs()
//            }
//            if (!file!!.exists()) {
//                file.createNewFile()
//            }



            Log.e("EEEEEEEEEEEEEEEEE" , RealPathUtil.getRealPath(MainActivity.context!!,data.getData()!!).toString())
            Log.e("EEEEEEEEEEEEEEEEE" , data.data.toString())

            val t: InputStream = FileInputStream(RealPathUtil.getRealPath(MainActivity.context!!,data.getData()!!))
            try {
                val wb = WorkbookFactory.create(t)
                for (sheet in wb) {
                    System.out.println(sheet.sheetName)
                    for (r in sheet) {
                        for (c in r) {
                            System.out.println(c.stringCellValue)
                        }
                    }
                }
            }
            catch (E:Exception){
                Log.e("EEEEEEEEEEEEEEEEE" , E.message.toString())

            }



        }
    }
    fun askForPermissions() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getpermission = Intent()
                getpermission.action = ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }
    }





}