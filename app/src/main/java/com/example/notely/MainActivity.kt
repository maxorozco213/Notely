package com.example.notely

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
                .setPositiveButton("Ok") { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                    Log.i("Permissions", "User pressed OK")
                }
                .setNegativeButton("Cancel") { _, _ ->
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