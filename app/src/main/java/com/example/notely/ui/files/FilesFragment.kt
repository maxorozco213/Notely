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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
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

        filesViewModel =
            ViewModelProviders.of(this).get(FilesViewModel::class.java)

        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)

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

        return binding.root
    }

    private fun upload(){
        val uid = userViewModel.user.value?.uid
        if(uid == null){
            findNavController().navigate(R.id.action_navigation_files_to_navigation_login)
            return
        }

        filesViewModel.uploadImage(uid,image,requireContext())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            image = data?.data
            Toast.makeText(requireContext(), "Photo selected", LENGTH_LONG).show()
        }
    }

}

