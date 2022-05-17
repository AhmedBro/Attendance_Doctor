package com.example.attendance_doctor.Desgin.Activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.attendance_doctor.R


class MainActivity : AppCompatActivity() {
    companion object{
        var context : Activity? = null
    }

    override fun onStart() {
        super.onStart()
        requestPermissions()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val window: Window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green4))
        requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun externalStoragePermission()=
        ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
 private fun StoragePermission()=
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
private fun readStoragePermission()=
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermissions(){
        var perm= mutableListOf<String>()
        if (!externalStoragePermission()){
            perm.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        if (!StoragePermission()){
            perm.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readStoragePermission()){
            perm.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (perm.isNotEmpty()){
            ActivityCompat.requestPermissions(this,perm.toTypedArray(),0)
        }
    }
    init {
        context =this
    }
}