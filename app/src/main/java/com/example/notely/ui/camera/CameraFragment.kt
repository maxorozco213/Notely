package com.example.notely.ui.camera

import android.Manifest
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
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
    private lateinit var cameraViewModel: CameraViewModel
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding: FragmentCameraBinding
    private val PERMISSION_REQUEST_CODE: Int = 101
    private var mCurrentPhotoPath: String? = null
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel =
            ViewModelProviders.of(this).get(CameraViewModel::class.java)

        binding = DataBindingUtil.inflate<FragmentCameraBinding>(
            inflater, R.layout.fragment_camera, container, false)
        binding.camera

        if (checkPersmission()) takePicture() else requestPermission()

        return binding.root
    }

    private fun checkPersmission(): Boolean {
        val camerperm = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        val readperm = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        val writeperm = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return (camerperm && readperm && writeperm)
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
                    &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    takePicture()
                }
                return
            }

            else -> {

            }
        }
    }

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

        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            binding.cropImageView.setImageUriAsync(imageUri)
            binding.cropImageView.setOnCropImageCompleteListener{

                    _: CropImageView, _: CropImageView.CropResult ->

                val cropped:Bitmap = binding.cropImageView.croppedImage

                binding.croppedimage.setImageBitmap(cropped)


                val newUrl = getImageUriFromBitmap(cropped, System.currentTimeMillis().toString())
                println(newUrl)
            }


            binding.crop.setOnClickListener{
                println("button clicked")

                binding.cropImageView.getCroppedImageAsync()
                println("yo dawggggg")

                binding.crop.visibility = View.GONE
                binding.cropImageView.visibility = View.GONE
                binding.croppedimage.visibility = View.VISIBLE

                Toast.makeText(requireContext(), "Cropped image saved", LENGTH_LONG).show()
            }

        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap, title: String): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, title, null)

        return Uri.parse(path.toString())
    }

}