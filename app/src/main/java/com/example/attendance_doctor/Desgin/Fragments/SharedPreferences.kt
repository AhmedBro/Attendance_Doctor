package com.example.attendance_doctor.Desgin.Fragments

import android.content.Context
import android.content.SharedPreferences
import com.example.attendance_doctor.R

class SharedPreferences(mContext: Context) {
    val STUDENT_ID: String = "mName"
    val STUDENT_PASSWORD: String = "mId"
    val IS_LOGIN: String = "IsLogin"

    val mShared =
        mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE)
    val mEditor: SharedPreferences.Editor = mShared.edit()

    fun setAdminData(mId: String, mPassword: String, mIsLogin: Boolean) {

        mEditor.putString(STUDENT_ID, mId)
        mEditor.putString(STUDENT_PASSWORD, mPassword)
        mEditor.putBoolean(IS_LOGIN.toString(), mIsLogin)
        mEditor.commit()
    }


    fun getId(): String? {
        return mShared.getString(STUDENT_ID, null)
    }

    fun getPassword(): String? {
        return mShared.getString(STUDENT_PASSWORD, null)
    }

    fun IsLogin(): Boolean {
        return mShared.getBoolean(IS_LOGIN, false)
    }

}