package com.example.notely

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.notely.databinding.FragmentLoginRegisterBinding
import com.google.firebase.auth.FirebaseUser

class LoginRegister : Fragment() {

    private lateinit var binding: FragmentLoginRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_register, container, false)
        // Inflate the layout for this fragment

//        binding.signInButton.setOnClickListener{ signIn() }
//        binding.button.setOnClickListener { signOut() }
        return binding.root
    }





    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }



    private fun updateUI(user: FirebaseUser?) {
        binding.textView.text = user?.displayName ?: "Not signed in"
    }

}
