package com.example.attendance_doctor.Domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object InitFireStore {
    var db:FirebaseFirestore? = null

    @JvmStatic
    val instance:FirebaseFirestore
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            if (db == null) {

                db = Firebase.firestore

            }
            return db!!
        }

}