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
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow

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
            binding.textViewTotalSize.text = formatBytes(it, 2)
        })
        binding.btnSignOut.setOnClickListener { userViewModel.signOut() }
        return binding.root
    }


    /*
    * This function to format into the correct byte units was translated to
    * kotlin (by Jesus Mendoza) from the source linked below,
    * that was originally written in JavaScript.
    * https://stackoverflow.com/questions/15900485/correct-way-to-convert-size-in-bytes-to-kb-mb-gb-in-javascript
    * */
    private fun formatBytes(bytes: Long, decimals: Int) : String {
        if (bytes == 0L) return "0 Bytes"
        val units: List<String> = listOf("Bytes", "KB", "MB", "GB", "TB")
        val kilo = 1024.0
        val dm = if ( decimals <= 0 ) 0 else decimals
        val i = floor( log2(bytes.toDouble()) / log2(kilo) )
        val num = bytes / kilo.pow(i)
        return "%.${dm}f ${units[i.toInt()]}".format(num)
    }

}