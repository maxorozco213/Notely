package com.example.notely

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1
    private val permissionsArray = arrayOf(
            INTERNET,
            READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_user, R.id.navigation_camera, R.id.navigation_files
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkPermissions()
    }



    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.i("PERMISSIONS", "Storage granted")
        } else if (ContextCompat.checkSelfPermission(this, INTERNET) == PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSIONS", "Internet granted")
        }
        else {
            this.sendPermissionRequest()
        }
    }

    private fun sendPermissionRequest() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("Notely needs permissions to save and read files on your device")
                .setPositiveButton("Ok") { _, _ ->
                    ActivityCompat.requestPermissions(this, permissionsArray, STORAGE_PERMISSION_CODE)
                    Log.i("Permissions", "Permissions granted")
                }
                .setNegativeButton("Cancel") { _, _ ->
                    Log.i("Permissions", "Storage permission denied")
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(this, permissionsArray, STORAGE_PERMISSION_CODE)
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