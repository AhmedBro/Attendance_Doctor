package com.example.attendance_doctor.Data

import java.io.Serializable

class Student : Serializable {
    var StudentID: String? = null
    var StudentName: String? = null

    constructor()

    constructor(
        StudentID: String?,
        StudentName: String?,
    ) {
        this.StudentID = StudentID
        this.StudentName = StudentName
    }
}