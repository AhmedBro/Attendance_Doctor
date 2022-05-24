package com.example.attendance_doctor.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.attendance_doctor.Domain.Constants
import com.example.attendance_doctor.Domain.InitFireStore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObjects

class LecturesViewModel : ViewModel() {

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
    private val _doneDelete = MutableLiveData<Boolean>()
    val doneDelete: LiveData<Boolean>
        get() = _doneDelete


    fun doneDelete() {
        _doneDelete.value = false
    }


    private val _noLectures = MutableLiveData<Boolean>()
    val noLectures: LiveData<Boolean>
        get() = _noLectures

    fun doneRetrievingdata() {
        _doneRetrieving.value = false
    }

    fun doneAdding() {
        _error.value = null
    }

    fun showProgressBar() {
        _showProgressbar.postValue(true)
    }

    fun hideProgressBar() {
        _showProgressbar.postValue(false)
    }

    var lectures = arrayListOf<String>()
    fun getLectures(CourseID: String) {
        if (lectures.isNotEmpty()) {
            lectures.clear()
        }

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).get().addOnSuccessListener {
                Log.e("testLec", it.size().toString())
                _noLectures.value = it.isEmpty
                for (i in it) {
                    var lecture = i.id.substringBefore("*")
                    lectures.add(lecture)
                    Log.e("testLec1", i.id)
                }
                _doneRetrieving.value = true
                _showProgressbar.value = false


            }.addOnFailureListener {
                _error.value = it.message
                _showProgressbar.value = false
            }
    }

    fun addCourseToStudent(students: List<Student>, courseName: String) {
        _showProgressbar.value = true
        for (student in students) {
            Log.e("coursename", student.StudentID!!)
            InitFireStore.instance.collection(Constants.STUDENTS_TABLE)
                .document(student.StudentID!!.substring(0, 4)).collection(Constants.STUDENT_DATA)
                .document(
                    student.StudentID!!
                ).update("coursesId", FieldValue.arrayUnion(courseName)).addOnFailureListener {

                }
        }


    }

    fun addNameToStudent(students: List<Student>) {
        for (index in 0..students.size - 1) {

            InitFireStore.instance.collection(Constants.STUDENTS_TABLE)
                .document(students[index].StudentID!!.substring(0, 4))
                .collection(Constants.STUDENT_DATA).document(
                    students[index].StudentID!!
                ).update("studentName", students[index].StudentName).addOnSuccessListener {
                    if (index == students.size - 1) {
                        _error.value = "successfully add student to this course"
                        _showProgressbar.value = false

                    }
                }.addOnFailureListener {
                    if (it.message?.substring(0, 9).equals("NOT_FOUND")) {
                        _error.value = "Not Existing Student" + " ${students[index].StudentID}"
                    } else {
                        _error.value = it.message
                    }
                    _showProgressbar.value = false
                }

        }
    }

    fun deleteLecture(CourseID: String, LectureID: String) {

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
            .get().addOnSuccessListener {

                for (i in it) {
                    InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
                        .collection(Constants.LECTURES).document(LectureID)
                        .collection(Constants.LECTURES_DATA).document(i.id).delete()
                }
                InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
                    .collection(Constants.LECTURES).document(LectureID).delete()

                _showProgressbar.value = false
                _doneDelete.value = true
                _error.value = "Lecture Deleted Successfully"
            }.addOnFailureListener {
                _error.value = it.message
                _showProgressbar.value = false
            }
    }

    fun disableAttendance(CourseID: String, LectureID: String) {

        var student = Student("disabled", "disabled")
        var student2 = Student("enabled", "enabled")

        InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
            .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
            .document("Dummy").get().addOnSuccessListener {
                if ((it.toObject(Student::class.java))!!.StudentID == "enabled" || (it.toObject(Student::class.java))!!.StudentID == "Dummy ID"){
                    InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
                        .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
                        .document("Dummy").set(student).addOnSuccessListener {
                            _error.value = "Lecture attendance disabled successfully"
                            _showProgressbar.value=false
                        }

                }else{
                    InitFireStore.instance.collection(Constants.COURSES_TABLE).document(CourseID)
                        .collection(Constants.LECTURES).document(LectureID).collection(Constants.LECTURES_DATA)
                        .document("Dummy").set(student2).addOnSuccessListener {
                            _showProgressbar.value=false
                            _error.value = "Lecture attendance enabled successfully" }
                }

            }.addOnFailureListener {
                _error.value = it.message
            }
    }
}