package com.example.attendance_doctor.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.google.firebase.firestore.ktx.toObjects

class LectureAttendanceViewModel : ViewModel() {

    private val _showProgressbar = MutableLiveData<Boolean>()
    val showProgressbar: LiveData<Boolean>
        get() = _showProgressbar

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _doneRetrieving = MutableLiveData<Boolean>()
    val doneRetrieving: LiveData<Boolean>
        get() = _doneRetrieving

    private val _doneAdding = MutableLiveData<Boolean>()
    val doneAdding: LiveData<Boolean>
        get() = _doneAdding

    private val _disabledFlag = MutableLiveData<Boolean>()
    val disabledFlag: LiveData<Boolean>
        get() = _disabledFlag

    fun doneRetrievingdata() {
        _doneRetrieving.value = false
    }

    fun showProgressBar() {
        _showProgressbar.postValue(true)
    }

    fun hideProgressBar() {
        _showProgressbar.postValue(false)
    }

    var Students = arrayListOf<Student>()

    fun getLectures(CourseID: String, LectureID: String) {
        if (Students.isNotEmpty()) {
            Students.clear()
        }

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
            .get().addOnSuccessListener {

                for (i in it) {
                    if (i.id != "Dummy") {
                        var student = i.toObject(Student::class.java)
                        Students.add(student)
                    }

                }
                _doneRetrieving.value = true
                _showProgressbar.value = false


            }.addOnFailureListener {
                _error.value = it.message
                _showProgressbar.value = false
            }
    }


    suspend fun addManually(student: Student, LectureID: String, courseCode: String) {
        InitFireStore.instance.collection(Constants.COURSES_TABLE)
            .document(courseCode)
            .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
            .document(student.StudentID!!).set(student)
            .addOnSuccessListener {
                _error.value = "Student Added successfully"
            }
            .addOnFailureListener {
                _error.value = it.message
            }

    }

    fun disableAttendance(CourseID: String, LectureID: String) {

        var student = Student("disabled", "disabled")
        var student2 = Student("enabled", "enabled")

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
            .document("Dummy").get().addOnSuccessListener {
                _disabledFlag.value = it.toObject(Student::class.java)!!.StudentID == "disabled"
            }.addOnFailureListener {
                _error.value = it.message
            }
    }


}