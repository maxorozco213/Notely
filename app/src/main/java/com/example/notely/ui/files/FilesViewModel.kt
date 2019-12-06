package com.example.notely.ui.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is files Fragment"
    }
    val text: LiveData<String> = _text
}