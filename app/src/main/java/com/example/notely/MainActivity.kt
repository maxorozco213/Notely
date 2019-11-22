package com.example.notely

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.notely.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show()
        } else {
            this.sendPermissionRequest()
        }
    }

    private fun sendPermissionRequest() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This is needed")
                .setPositiveButton("Ok") { DialogInterface, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                    Log.i("Permissions", "User pressed OK")
                }
                .setNegativeButton("Cancel") { DialogInterface, which ->
                    Log.i("Permissions", "Storage permission denied")
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("", "Permission has been denied by user")
                } else {
                    Log.i("", "Permission has been granted by user")
                }
            }
        }
    }

}