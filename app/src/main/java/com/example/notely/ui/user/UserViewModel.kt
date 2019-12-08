package com.example.notely.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is user Fragment"
    }
    val user = MutableLiveData<FirebaseUser?>()
    val text: LiveData<String> = _text

}