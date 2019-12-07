package com.example.notely.ui.files

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.notely.R
import com.example.notely.databinding.FragmentFilesBinding
import com.google.firebase.storage.FirebaseStorage

class FilesFragment : Fragment() {
    private lateinit var filesViewModel: FilesViewModel
    private val PICK_PHOTO_REQUEST = 4
    private var image: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filesViewModel =
            ViewModelProviders.of(this).get(FilesViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentFilesBinding>(
            inflater, R.layout.fragment_files, container, false)
        binding.files = this

        val textView: TextView = binding.textFiles
        val testBtn2: Button = binding.button2
        val testBtn1: Button = binding.button3

        testBtn2.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        {view: View ->
            selectImage()
        }

        testBtn1.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            uploadImage(image)
        }

        filesViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_PHOTO_REQUEST)
    }

    private fun uploadImage(image: Uri?) {
        val storageRef = FirebaseStorage.getInstance().reference
        val upImage = storageRef.child("image/${image?.lastPathSegment}")

        if (image != null) {
            val uploadTask = upImage.putFile(image)
            uploadTask
                .addOnSuccessListener { Log.i("FILE UPLOAD", "Success") }
                .addOnFailureListener { Log.i("FILE UPLOAD", "Failure") }
        } else {
            Log.i("FILE UPLOAD", "more failure")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == PICK_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            image = data?.data
            Toast.makeText(requireContext(), image.toString(), LENGTH_LONG).show()
        }
    }

}