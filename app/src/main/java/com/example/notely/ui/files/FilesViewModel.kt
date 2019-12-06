package com.example.notely.ui.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilesViewModel : ViewModel() {
    val permissionsRequest = MutableLiveData<String>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is files Fragment"
        /* Show the file explorer here
        *
        * */
    }
    val text: LiveData<String> = _text
}