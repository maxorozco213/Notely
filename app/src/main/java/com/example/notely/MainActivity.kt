package com.example.notely

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.widget.Button
import android.widget.ImageView

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1

//    lateinit var imageView: ImageView
//    lateinit var captureButton: Button
//
//    val REQUEST_IMAGE_CAPTURE = 1
//
//
//    private val PERMISSION_REQUEST_CODE: Int = 101
//
//    private var mCurrentPhotoPath: String? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        captureButton = findViewById(R.id.btn_capture)
//        captureButton.setOnClickListener(View.OnClickListener {
//            if (checkPersmission()) takePicture() else requestPermission()
//        })

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

//        checkPermissions()
    }

//    private fun checkPersmission(): Boolean {
//        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
//                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
//            PERMISSION_REQUEST_CODE)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        when (requestCode) {
//            PERMISSION_REQUEST_CODE -> {
//
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                    takePicture()
//
//                } else {
//                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//
//            else -> {
//
//            }
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun createFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            mCurrentPhotoPath = absolutePath
//        }
//    }
//
//    private fun takePicture() {
//
//        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val file: File = createFile()
//
//        val uri: Uri = FileProvider.getUriForFile(
//            this,
//            "com.example.notely.fileprovider",
//            file
//        )
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
//
//            //To get the File for further usage
//            val auxFile = File(mCurrentPhotoPath)
//
//
//            var bitmap : Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
//            imageView.setImageBitmap(bitmap)
//
//        }
//    }


//    private fun checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show()
//        } else {
//            this.sendPermissionRequest()
//        }
//    }
//
//    private fun sendPermissionRequest() {
//        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            AlertDialog.Builder(this)
//                .setTitle("Permission needed")
//                .setMessage("This is needed")
//                .setPositiveButton("Ok") { _, _ ->
//                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
//                    Log.i("Permissions", "User pressed OK")
//                }
//                .setNegativeButton("Cancel") { _, _ ->
//                    Log.i("Permissions", "Storage permission denied")
//                }
//                .show()
//        } else {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            STORAGE_PERMISSION_CODE -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Log.i("", "Permission has been denied by user")
//                } else {
//                    Log.i("", "Permission has been granted by user")
//                }
//            }
//        }
//    }
}