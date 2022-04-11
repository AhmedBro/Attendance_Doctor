package com.example.attendance_doctor.Desgin.Fragments

import android.content.Context
import android.content.SharedPreferences
import com.example.attendance_doctor.R

class SharedPreferences(mContext: Context) {
    val ADMIN_NAME: String = "mName"
    val ADMIN_ID: String = "mId"
    val IS_LOGIN: String = "IsLogin"

    val mShared =
        mContext.getSharedPreferences(mContext.getString(R.string.app_name), Context.MODE_PRIVATE)
    val mEditor: SharedPreferences.Editor = mShared.edit()

    fun setAdminData(mName: String, mId: String, mIsLogin: Boolean) {

        mEditor.putString(ADMIN_NAME, mName)
        mEditor.putString(ADMIN_ID, mId)
        mEditor.putBoolean(IS_LOGIN.toString(), mIsLogin)
        mEditor.commit()
    }


    fun getName(): String? {
        return mShared.getString(ADMIN_NAME, null)
    }

    fun getId(): String? {
        return mShared.getString(ADMIN_ID, null)
    }

    fun IsLogin(): Boolean {
        return mShared.getBoolean(IS_LOGIN, false)
    }

}