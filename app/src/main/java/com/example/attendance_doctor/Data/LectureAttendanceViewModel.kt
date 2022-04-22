package com.example.attendance_doctor.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.google.firebase.firestore.ktx.toObjects

class LectureAttendanceViewModel :ViewModel(){

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

    fun showProgressBar() {
        _showProgressbar.postValue(true)
    }
    var Students = arrayListOf<Student>()

    fun getLectures(CourseID:String,LectureID:String){
        if(Students.isNotEmpty()){
            Students.clear()
        }

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA).get().addOnSuccessListener {

                for (i in it){
                    if (i.id!="Dummy"){
                        var student=i.toObject(Student::class.java)
                        Students.add(student)
                    }

                }
                _doneRetrieving.value=true
                _showProgressbar.value=false


            }.addOnFailureListener {
                _error.value = it.message
                _showProgressbar.value = false
            }
    }
}