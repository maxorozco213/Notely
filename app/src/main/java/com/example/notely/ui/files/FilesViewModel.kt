package com.example.notely.ui.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.ArrayList

class FilesViewModel : ViewModel() {
    private var photoPaths = ArrayList<String>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is files Fragment"
        /* Show the file explorer here
        *
        * */
    }


    val text: LiveData<String> = _text
}