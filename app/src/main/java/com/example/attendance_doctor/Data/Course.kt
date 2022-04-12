package com.example.attendance_doctor.Data

import java.io.Serializable


class Course :Serializable  {
     var courseCode : String? = null
    var courseName: String?= null
    var courseDate: String?= null
    var courseTimeFrom: String?= null
    var courseTimeTo: String?= null
    var coursePlace: String?= null
    var courseGroup: String?= null
    constructor()

   //for fill course table
    constructor(
        courseCode: String?,
        courseName: String?,
        courseDate: String?,
        courseTimeFrom: String?,
        courseTimeTo: String?,
        coursePlace: String?,
        courseGroup: String?,
    ) {
        this.courseCode = courseCode
        this.courseName = courseName
        this.courseDate = courseDate
        this.courseTimeFrom = courseTimeFrom
        this.courseTimeTo = courseTimeTo
        this.coursePlace = coursePlace
        this.courseGroup = courseGroup
    }
}
