package com.example.attendance_doctor.Desgin.Activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
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
        requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun externalStoragePermission()=
        ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
 private fun StoragePermission()=
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
private fun readStoragePermission()=
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED

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