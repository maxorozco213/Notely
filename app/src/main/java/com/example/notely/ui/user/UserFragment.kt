package com.example.notely.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.notely.R
import com.example.notely.databinding.FragmentFilesBinding
import com.example.notely.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentUserBinding>(
            inflater, R.layout.fragment_user, container, false)
        binding.user = this

        val textView: TextView = binding.textUser
        userViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return binding.root
    }
}