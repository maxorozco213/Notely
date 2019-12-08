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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notely.R
import com.example.notely.databinding.FragmentFilesBinding
import com.example.notely.ui.user.UserViewModel
import com.example.notely.utils.getFileModelsFromFiles
import com.example.notely.utils.getFilesFromPath
import kotlinx.android.synthetic.main.fragment_files.*
import kotlinx.android.synthetic.main.item_recycler_file.*
import kotlinx.android.synthetic.main.item_recycler_file.filesRecyclerView
import com.google.firebase.storage.FirebaseStorage

class FilesFragment : Fragment() {
    private lateinit var filesViewModel: FilesViewModel
    private val PICK_PHOTO_REQUEST = 4
    private var image: Uri? = null
    private lateinit var mFilesAdapter: FilesRecyclerAdapter
    private lateinit var PATH: String
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
        // Check which request we're responding to
        if (requestCode == PICK_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            image = data?.data
            Toast.makeText(requireContext(), image.toString(), Toast.LENGTH_LONG).show()
        }
    }

}


//            val textView: TextView = binding.text
//
//        filesViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val filePath = arguments?.getString(ARG_PATH)
//        if (filePath == null) {
//            Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
//            return
//        }
//        PATH = filePath
//
//        initViews()
//    }

//    private fun initViews() {
//        filesRecyclerView.layoutManager = LinearLayoutManager(context)
//        mFilesAdapter = FilesRecyclerAdapter()
//        filesRecyclerView.adapter = mFilesAdapter
//        updateDate()
//    }


//    fun updateDate() {
//        val files = getFileModelsFromFiles(getFilesFromPath(PATH))
//
//        if (files.isEmpty()) {
//            emptyFolderLayout.visibility = View.VISIBLE
//        } else {
//            emptyFolderLayout.visibility = View.INVISIBLE
//        }
//
//        mFilesAdapter.updateData(files)
//    }
//
//    companion object {
//        private const val ARG_PATH: String = "com.example.notely.fileslist.path"
//        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
//    }
//
//    class Builder {
//        var path: String = ""
//
//        fun build(): FilesFragment {
//            val fragment = FilesFragment()
//            val args = Bundle()
//            args.putString(ARG_PATH, path)
//            fragment.arguments = args;
//            return fragment
//        }
//    }

