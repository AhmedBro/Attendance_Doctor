package com.example.attendance_doctor.Data

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.attendance_doctor.Desgin.Activities.MainActivity.Companion.context
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class QrCodeViewModel : ViewModel() {

    private val _bmp = MutableLiveData<Bitmap>()
    val bmp: LiveData<Bitmap>
        get() = _bmp

    private val _showProgressbar = MutableLiveData<Boolean>()
    val showProgressbar: LiveData<Boolean>
        get() = _showProgressbar

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun showProgressBar() {
        _showProgressbar.value = true
    }


    fun generateQrCode(data: String) {
        val data = data
        _showProgressbar.value = true
        if (data.isEmpty()) {
            Toast.makeText(context, "no data entered", Toast.LENGTH_SHORT).show()
            _showProgressbar.value = false
        } else {
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 1000, 1000)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                _bmp.value = bmp
                _showProgressbar.value = false
            } catch (e: WriterException) {
                _error.value = e.localizedMessage
                _showProgressbar.value = false
            }
        }
    }

}