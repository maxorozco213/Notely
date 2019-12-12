@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.example.notely.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.notely.R
import com.example.notely.databinding.FragmentCameraBinding
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    val REQUEST_IMAGE_CAPTURE = 1
    private val PERMISSION_REQUEST_CODE: Int = 101
    private val PICK_PHOTO_REQUEST = 4
    private var mCurrentPhotoPath: String? = null
    private var imageUri: Uri? = null
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel =
            ViewModelProviders.of(this).get(CameraViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_camera, container, false)
        binding.camera

        if(!checkPermissions()) {
            requestPermission()
        }

        binding.takePhoto.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        {view: View ->
            takePicture()
        }

        binding.chooseToCrop.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        {view: View ->
            pickPhoto()
        }
        // onCompletion for the crop module
        binding.cropImageView.setOnCropImageCompleteListener{
                _: CropImageView, _: CropImageView.CropResult ->

            val cropped:Bitmap = binding.cropImageView.croppedImage
            binding.croppedImage.setImageBitmap(cropped)
            val newUrl = getImageUriFromBitmap(cropped, System.currentTimeMillis().toString())
            println(newUrl)
        }
        // onClick for crop button
        binding.cropBtn.setOnClickListener{
            println("button clicked")
            binding.cropImageView.getCroppedImageAsync()
            println("yo dawggggg")
            binding.cropBtn.visibility = View.GONE
            binding.cropImageView.visibility = View.GONE
            binding.croppedImage.visibility = View.VISIBLE

            Toast.makeText(requireContext(), "Cropped image saved", LENGTH_LONG).show()
        }

        return binding.root
    }

    private fun pickPhoto() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(intent, PICK_PHOTO_REQUEST)
    }

    private fun checkPermissions(): Boolean {
        val cameraPerm = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        val readPerm = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        val writePerm = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return (cameraPerm && readPerm && writePerm)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ),
            PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePicture()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.notely.fileprovider",
            file
        )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            binding.cropImageView.setImageUriAsync(imageUri)
            actResultSetVisibility()

        } else if (requestCode === PICK_PHOTO_REQUEST && resultCode === Activity.RESULT_OK) {
            imageUri = data?.data

            binding.cropImageView.setImageUriAsync(imageUri)
            actResultSetVisibility()

            Toast.makeText(requireContext(), "Photo selected", LENGTH_LONG).show()

        }
    }

    private fun actResultSetVisibility() {
        binding.chooseToCrop.isVisible = false
        binding.takePhoto.isVisible = false
        binding.camIconView.isVisible = false
        binding.albumIconView.isVisible = false
        binding.imagePlaceholder.isVisible = false
        binding.cropBtn.isVisible = true
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap, title: String): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, title, null)

        return Uri.parse(path.toString())
    }

}