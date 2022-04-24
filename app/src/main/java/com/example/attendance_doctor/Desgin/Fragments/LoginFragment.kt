package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.attendance_doctor.Data.Login.LoginViewModel
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    lateinit var mSharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onStart() {
        super.onStart()
    }


    lateinit var loginViewModel: LoginViewModel

    lateinit var id: String
    lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        mSharedPreferences = context?.let { SharedPreferences(it) }!!
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val logBtn = view.findViewById<Button>(R.id.mLoginBtn)
        logBtn.setOnClickListener {
            mLoginProgressBar.visibility = View.VISIBLE
            if (Validate()) {
                id = mIdEt.text.toString()
                password = mPasswordEt.text.toString()
                lifecycleScope.launch(Dispatchers.IO) { loginViewModel.teacherLogin(id, password) }

            } else {
                mLoginProgressBar.visibility = View.INVISIBLE
            }
        }


        loginViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it) {
                mSharedPreferences.setAdminData("$id", "$password", true)
                Navigation.findNavController(this.requireView())
                    .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                loginViewModel.doneNavigatingToHome()
            }
        })


        loginViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            if (it) {
                mLoginProgressBar.visibility = View.VISIBLE
            } else {
                mLoginProgressBar.visibility = View.INVISIBLE
            }
        })

        loginViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message.isNotEmpty()) {
                mErrorMessage.visibility = View.VISIBLE
                mErrorMessage.text = message
            } else {
                mErrorMessage.visibility = View.INVISIBLE
            }
        })

        return view
    }

    fun Validate(): Boolean {

        if (mIdEt.text.toString().isEmpty()) {
            mErrorMessage.text = "Please enter an Id"
            mErrorMessage.visibility = View.VISIBLE
            return false
        } else if (mPasswordEt.text.toString().isEmpty()) {
            mErrorMessage.text = "Please enter your Password"
            mErrorMessage.visibility = View.VISIBLE
            return false
        } else {
            mErrorMessage.visibility = View.INVISIBLE
            return true
        }

    }

}