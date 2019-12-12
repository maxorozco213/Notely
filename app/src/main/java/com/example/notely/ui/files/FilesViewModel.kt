package com.example.notely.ui.files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notely.models.UserFileMetadata
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FilesViewModel : ViewModel() {
    private val PICK_PHOTO_REQUEST = 4
    private val storageRef = FirebaseStorage.getInstance().reference.child("users")
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

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
            val upImage = storageRef.child("$uid/${image?.lastPathSegment}")
            val uploadTask = upImage.putFile(image)
            uploadTask
                .addOnSuccessListener {
                    Toast.makeText(context, "Upload Successful", LENGTH_LONG).show()
                    Log.i("FILE UPLOAD", "Success")
                    println("Download url:")
                    upImage.downloadUrl.addOnCompleteListener{
                        println(it.result.toString())
                    }
                    println("Transfered [${it.bytesTransferred}] bytes")
                    val meta = UserFileMetadata(currNumFiles + 1, currStorageSize + it.bytesTransferred)
                    db.child(uid).setValue(meta)
                }
                .addOnFailureListener { Log.i("FILE UPLOAD", "Failure") }
        } else {
            Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

}