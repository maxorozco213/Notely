package com.example.notely

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.notely.databinding.FragmentLoginRegisterBinding
import com.example.notely.ui.user.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.*

class LoginRegister : Fragment() {

    private lateinit var binding: FragmentLoginRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_register, container, false)
        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.setUpClient(getString(R.string.default_web_client_id), requireContext())
        userViewModel.user.observe(this, Observer {
            if ( it != null )
                findNavController().navigate(R.id.action_navigation_login_to_navigation_user)
        })
        binding.signInButton.setOnClickListener{ userViewModel.signIn(this) }
        return binding.root
    }

    private val job = Job()
    private var scope = CoroutineScope(Dispatchers.IO + job)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        scope.launch {
            oAR(requestCode, resultCode, data)
        }
    }

    private suspend fun oAR(requestCode: Int, resultCode: Int, data: Intent?) {
        withContext(Dispatchers.IO) {
            super.onActivityResult(requestCode, resultCode, data)

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == userViewModel.RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    userViewModel.firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    println("Google sign in failed")
                    Toast.makeText(requireContext(), "Google sign in failed", LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }

}
