package com.example.attendance_doctor.Desgin.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.attendance_doctor.Data.QrCodeViewModel
import com.example.attendance_doctor.R
import kotlinx.android.synthetic.main.fragment_qr_code_generated.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class qr_code_generated : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_qr_code_generated, container, false)

        val qrCodeViewModel = ViewModelProvider(this).get(QrCodeViewModel::class.java)

        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            qrCodeViewModel.showProgressBar()
            val data = mCode.text.toString()
            lifecycleScope.launch(Dispatchers.Main) {
                qrCodeViewModel.generateQrCode(data)
            }
        }

        qrCodeViewModel.bmp.observe(viewLifecycleOwner, Observer {
            if (it.width == 1000) {
                mQrCodeImageView.setImageBitmap(it)
                mQrCodeImageView.visibility = View.VISIBLE
            }
        })

        qrCodeViewModel.showProgressbar.observe(viewLifecycleOwner, Observer {
            if (!it) {
                mQrCodeProgressBar.visibility = View.INVISIBLE
            } else {
                mQrCodeProgressBar.visibility = View.VISIBLE
            }
        })

        qrCodeViewModel.error.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }


}