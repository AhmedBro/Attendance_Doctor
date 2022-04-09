package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.attendance_doctor.R


class Splash : Fragment() {
    lateinit var mSharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPreferences = context?.let { SharedPreferences(it) }!!
        gotoNextPage()
    }

    private fun gotoNextPage() {
        Handler().postDelayed(Runnable {
            if (mSharedPreferences.IsLogin()) {
                val navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.main, true)
                    .build()
                val action = SplashDirections.actionSplashToHomeFragment()
                findNavController().navigate(action , navOptions)


            } else {
                val navOptions: NavOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.main, true)
                    .build()
                val action =SplashDirections.actionSplashToLoginFragment()
                findNavController().navigate(action , navOptions)

            }
        }, 2000L)

    }


    companion object {

    }
}