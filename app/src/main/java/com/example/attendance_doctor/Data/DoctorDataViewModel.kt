package com.example.attendance_doctor.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore

class DoctorDataViewModel : ViewModel() {
    private val _showProgressbar = MutableLiveData<Boolean>()
    val showProgressbar: LiveData<Boolean>
        get() = _showProgressbar

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _doneRetrieving = MutableLiveData<Boolean>()
    val doneRetrieving: LiveData<Boolean>
        get() = _doneRetrieving

    fun doneRetrievingdata() {
        _doneRetrieving.value = false
    }

    private val _doneRetrievingcourses = MutableLiveData<Boolean>()
    val doneRetrievingcourses: LiveData<Boolean>
        get() = _doneRetrievingcourses

    fun doneRetrievingcourses() {
        _doneRetrievingcourses.value = false
    }

    fun showProgressBar() {
        _showProgressbar.value = true
    }

    var teacherdata = Teacher()
    suspend fun getDoctorData(id: String) {
        Log.e("id test", id)
        InitFireStore.instance.collection(Constants.TEACHER_TABLE).document(id).get()
            .addOnSuccessListener {

                val teacher = it.toObject(Teacher::class.java)
                if (teacher != null) {
                    teacherdata = teacher
                }


                Log.e("get3", teacherdata.teacherName.toString())
                _doneRetrieving.value = true
            }.addOnFailureListener {
            _error.value = it.message
            _showProgressbar.value = false

        }
        Log.e("get2", teacherdata.teacherName.toString())
    }

    suspend fun getDoctorCourses(CoursesCode: ArrayList<String>?): ArrayList<Course> {
        var courses = ArrayList<Course>()
        Log.e("te5555", CoursesCode?.size.toString())
        InitFireStore.instance.collection(Constants.COURSES_TABLE)
            .get().addOnSuccessListener {
                for (course in it) {
                    Log.e("test111", course.id)

                    val newCourse = course.toObject(Course::class.java)
                    if (CoursesCode != null) {
                        var i = newCourse.courseCode.toString() + newCourse.courseGroup.toString()
                        if (i in CoursesCode) {
                            courses.add(newCourse)
                        }
                    }
                }
                _doneRetrievingcourses.value = true
                _showProgressbar.value = false

            }.addOnFailureListener {
                _error.value = it.message
                _showProgressbar.value = false
            }
        return courses
    }
}