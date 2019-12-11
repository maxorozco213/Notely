package com.example.notely.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.notely.R
import com.example.notely.databinding.FragmentUserBinding
import com.example.notely.ui.files.FilesViewModel

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var filesViewModel: FilesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        filesViewModel =
            ViewModelProviders.of(this).get(FilesViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentUserBinding>(
            inflater, R.layout.fragment_user, container, false)
        binding.user = this
        userViewModel.user.observe(this, Observer {
            if ( it == null ) {
                findNavController().navigate(R.id.action_navigation_user_to_navigation_login)
            } else {
                binding.apply {
                    name.text = it.displayName
                    textViewLinkedAcct.text = it.email
                }

                filesViewModel.numberOfFiles(userViewModel.user.value!!.uid)
            }
        })
        userViewModel.filesStored.observe(this, Observer {
            binding.textViewFilesStored.text = it.toString()
        })
        userViewModel.storageUsed.observe(this, Observer {
            binding.textViewTotalSize.text = it.toString()
        })
        binding.btnSignOut.setOnClickListener { userViewModel.signOut() }
        return binding.root
    }

}