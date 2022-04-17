package com.example.attendance_doctor.Data.Login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.attendance_doctor.Data.Student
import com.example.attendance_doctor.Data.Teacher
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {


    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    private val _showProgressbar = MutableLiveData<Boolean>()
    val showProgressbar: LiveData<Boolean>
        get() = _showProgressbar


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun doneNavigatingToHome() {
        _navigateToHome.value = false
    }


    suspend fun teacherLogin(
        id: String,
        password: String
    ) {
        if (password.isNotEmpty() && id.isNotEmpty()) {
            InitFireStore.instance.collection(Constants.TEACHER_TABLE)
                .document(id).get().addOnSuccessListener {
                    var teacher = it.toObject(Teacher::class.java)
                    if (teacher != null) {
                        if (teacher?.teacherPassword == password) {
                            _navigateToHome.value = true
                            _showProgressbar.value = false
                        } else {
                            _errorMessage.value = "Wrong Id or Password"
                        }
                    } else {
                        _errorMessage.value = "Not Existing Id"
                    }
                    _showProgressbar.value = false
                }.addOnFailureListener {
                    _showProgressbar.value = false
                    _errorMessage.value = it.localizedMessage ?: "Wrong Id or password"
                }
        } else {
            _errorMessage.value = "Id or password can't be empty"
            _showProgressbar.value = false
        }
    }
}