package com.mattgarrett.todonotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mattgarrett.todonotes.databinding.ActivityNewToDoItemBinding
import java.util.*

class NewToDoItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewToDoItemBinding

    companion object {
        const val TAG = "NewItemActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewToDoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }



}