package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendance_doctor.Data.Course
import com.example.attendance_doctor.Data.DoctorDataViewModel
import com.example.attendance_doctor.Data.ForgetPassword.ForgetPasswordViewModel
import com.example.attendance_doctor.Data.Login.LoginViewModel
import com.example.attendance_doctor.Data.Teacher
import com.example.attendance_doctor.Desgin.adapters.CourseListAdapter
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    lateinit var forgetPasswordViewModel: ForgetPasswordViewModel
    lateinit var mSharedPreferences: SharedPreferences
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPreferences = context?.let { SharedPreferences(it) }!!

        forgetPasswordViewModel=ViewModelProvider(this).get(ForgetPasswordViewModel::class.java)
        mSubmitForgetPasswordBtn.setOnClickListener {
            if (Validate()) {

                lifecycleScope.launch(Dispatchers.IO) { forgetPasswordViewModel.ChangePassword(mSharedPreferences.getId().toString(), mOldPassEt.text.toString() , mNewPassEt.text.toString()) }

            } else {
                mForgetPasswordProgressBar.visibility = View.INVISIBLE
            }
        }


        forgetPasswordViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                mSharedPreferences.setAdminData("", "", false)
                Navigation.findNavController(this.requireView())
                    .navigate(ChangePasswordFragmentDirections.actionChangePasswordFragmentToLoginFragment())
                forgetPasswordViewModel.doneNavigatingToHome()
            }
        })


        forgetPasswordViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            if (it) {
                mForgetPasswordProgressBar.visibility = View.VISIBLE
            } else {
                mForgetPasswordProgressBar.visibility = View.INVISIBLE
            }
        })

        forgetPasswordViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message.isNotEmpty()) {

                Toast.makeText(context , message , Toast.LENGTH_LONG).show()
            }
        })

    }

    fun Validate(): Boolean {

        if (mOldPassEt.text.toString().isEmpty()) {

            Toast.makeText(context , "Please enter your Old Password" , Toast.LENGTH_LONG).show()
            return false
        } else if (mNewPassEt.text.toString().isEmpty()) {
            Toast.makeText(context , "Please enter your new Password" , Toast.LENGTH_LONG).show()
            return false
        } else if (mConfirNewPasstv.text.toString().isEmpty()) {
            Toast.makeText(context , "Please enter your new Password" , Toast.LENGTH_LONG).show()
            return false
        } else if (mConfirNewPasstv.text.toString().equals(mNewPassEt.text.toString())) {
            Toast.makeText(context , "please enter your new pass in confirm pass right", Toast.LENGTH_LONG).show()
            return false
        } else {
            return true
        }

    }

}