package com.example.notely

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.notely.databinding.FragmentFoldersBinding
import droidninja.filepicker.FilePickerBuilder

class Folders: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentFoldersBinding>(
            inflater, R.layout.fragment_folders, container, false)
        binding.folders = this

//        FilePickerBuilder.instance
//            .setSelectedFiles()
//            .pickPhoto(this)

        return binding.root
    }
}