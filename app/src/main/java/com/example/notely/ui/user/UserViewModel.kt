package com.example.notely.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is user Fragment"
        /* Show login register views here if the user is not already signed in
        *
        * If the user is signed in then show their information
        *
        * Name, email, subscription status
        * */
    }
    val text: LiveData<String> = _text
}