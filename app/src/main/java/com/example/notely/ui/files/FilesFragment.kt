package com.example.notely.ui.files

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notely.R
import com.example.notely.databinding.FragmentFilesBinding
import com.example.notely.ui.user.UserViewModel

class FilesFragment : Fragment() {
    private lateinit var filesViewModel: FilesViewModel
    private val PICK_PHOTO_REQUEST = 4
    private var image: Uri? = null
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = activity?.run {
            ViewModelProviders.of(this)[UserViewModel::class.java]
        } ?: throw Exception("Invalid Activity: attempted access in FilesFrag")

        filesViewModel = activity?.run {
            ViewModelProviders.of(this)[FilesViewModel::class.java]
        } ?: throw Exception("Invalid Activity: attempted access in FilesFrag")

        val binding = DataBindingUtil.inflate<FragmentFilesBinding>(
            inflater, R.layout.fragment_files, container, false)
        binding.files = this

        val testBtn2: Button = binding.button2
        val testBtn1: Button = binding.button3

        testBtn2.setOnClickListener {
            filesViewModel.selectImage(this)
        }
        testBtn1.setOnClickListener {
            upload()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        userViewModel.urls.observe(this, Observer {
            binding.recyclerView.adapter = UploadAdapter(userViewModel.urls.value ?: mutableListOf(), requireContext())
        })

        return binding.root
    }

    private fun upload(){
        if(userViewModel.user.value == null) {
            findNavController().navigate(R.id.action_navigation_files_to_navigation_login)
            return
        }
        val uid = userViewModel.user.value!!.uid
        val files = userViewModel.filesStored.value ?: 0
        val space = userViewModel.storageUsed.value ?: 0
        val urls = userViewModel.urls.value ?: mutableListOf()

        filesViewModel.uploadImage(uid, image, requireContext(), files, space, urls)
        // after image has been uploaded, reset so that user must select another image if desired
        image = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            image = data?.data
            Toast.makeText(requireContext(), "Photo selected", LENGTH_LONG).show()
        }
    }

}

