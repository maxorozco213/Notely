package com.example.notely.ui.files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage

class FilesViewModel : ViewModel() {
    private val PICK_PHOTO_REQUEST = 4

    private val _text = MutableLiveData<String>().apply {
        value = "This is files Fragment"
    }

    val text: LiveData<String>
        get() = _text

    fun selectImage(frag: Fragment) {

        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        frag.startActivityForResult(intent, PICK_PHOTO_REQUEST)
    }

    fun uploadImage(uid: String , image: Uri?, context: Context) {

        val storageRef = FirebaseStorage.getInstance().reference
        val upImage = storageRef.child("$uid/${image?.lastPathSegment}")

        if (image != null) {
            val uploadTask = upImage.putFile(image)
            uploadTask
                .addOnSuccessListener {
                    Toast.makeText(context, "Upload Successful", Toast.LENGTH_LONG).show()
                    Log.i("FILE UPLOAD", "Success")
                    println("Download url:")
                    upImage.downloadUrl.addOnCompleteListener{
                        println(it.result.toString())
                    }
                }
                .addOnFailureListener { Log.i("FILE UPLOAD", "Failure") }
        } else {
            Log.i("FILE UPLOAD", "more failure")
        }
    }

    fun numberOfFiles(uid: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val folder = storageRef.child(uid)
        folder.listAll().addOnSuccessListener {
            println("Items: ")
            println(it.items.size)
            println("[ ")
            for ( x in it.items ) {
                println(x.name)
            }
            println("]")
        }
    }

}