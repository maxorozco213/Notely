package com.example.notely.ui.files

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

class FilesFragment : Fragment() {
    private lateinit var filesViewModel: FilesViewModel

    companion object {
        private const val ARG_PATH: String = "com.example.notely.fileslist.path"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var path: String = ""

        fun build(): FilesFragment {
            val fragment = FilesFragment()
            val args = Bundle()
            args.putString(ARG_PATH, path)
            fragment.arguments = args;
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        filesViewModel =
            ViewModelProviders.of(this).get(FilesViewModel::class.java)

        val binding = DataBindingUtil.inflate<FragmentFilesBinding>(
            inflater, R.layout.fragment_files, container, false)
        binding.files = this

        val textView: TextView = binding.textNothing

        filesViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return binding.root
    }
}