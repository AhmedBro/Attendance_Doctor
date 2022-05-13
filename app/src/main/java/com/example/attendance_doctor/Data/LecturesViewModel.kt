package com.example.attendance_doctor.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.google.firebase.firestore.ktx.toObjects

class LecturesViewModel :ViewModel(){

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
    var lectures = arrayListOf<String>()
    fun getLectures(CourseID:String){
        if(lectures.isNotEmpty()){
            lectures.clear()
        }

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).get().addOnSuccessListener {
                Log.e("testLec", it.size().toString())
                for (i in it){
                    var lecture = i.id.substringBefore("*")
                    lectures.add(lecture)
                    Log.e("testLec1",i.id )
                }
                _doneRetrieving.value=true
                _showProgressbar.value=false


            }.addOnFailureListener {
                _error.value = it.message
                _showProgressbar.value = false
            }
    }
}