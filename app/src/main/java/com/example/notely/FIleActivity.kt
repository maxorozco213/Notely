package com.example.notely

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.notely.ui.files.FilesFragment

class FIleActivity(): AppCompatActivity(){




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_files)

        if (savedInstanceState == null) {
            val filesListFragment = FilesFragment.build {
                path = Environment.getExternalStorageDirectory().absolutePath
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.container, filesListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()

        }

    }


}