package com.example.attendance_doctor.Data

import java.io.Serializable

class Student : Serializable {
    var StudentID: String? = null
    var StudentPassword: String? = null
    var StudentName: String? = null
    var CoursesId: ArrayList<String>? = null

    constructor()

    constructor(
        StudentID: String?,
        StudentPassword: String?,
        StudentName: String?,
        CoursesId: ArrayList<String>?
    ) {
        this.StudentID = StudentID
        this.StudentPassword = StudentPassword
        this.StudentName = StudentName
        this.CoursesId = CoursesId
    }
}