package com.mattgarrett.todonotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mattgarrett.todonotes.databinding.ActivityNewToDoItemBinding
import com.mattgarrett.todonotes.viewmodels.MainViewModel2
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.util.*

class NewToDoItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewToDoItemBinding
    private lateinit var viewModel: MainViewModel2


    companion object {
        private const val TAG = "NewItemActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewToDoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel2::class.java)
        checkDate()

        binding.btnCreateNewItem.apply {
            setOnClickListener {
                val checkTitle = binding.etTitle.text.toString()
                val checkDescription = binding.etDescription.text.toString()
                if(initChecks(checkTitle,checkDescription)) {
                    viewModel.apply {
                        title = checkTitle
                        description = checkDescription
                        month = binding.numPickerMonth.value
                        date = binding.numPickerDay.value
                        postToFirebase()
                    }
                } else return@setOnClickListener

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.firebasePostState.collect{
                when (it) {
                    is MainViewModel2.FirebasePostState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "New Item Posted Successfully",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        delay(1500L)
                        Intent(this@NewToDoItemActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(this)
                        }
                    }
                    is MainViewModel2.FirebasePostState.Error -> {
                        Snackbar.make(
                            binding.root,
                            "Error ${it.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }

        }

    }

    private fun initChecks(title: String?,description: String?): Boolean {
        return when {
            title!!.isEmpty() -> {
                Snackbar.make(
                        binding.root,
                        "Title cannot be empty!",
                        Snackbar.LENGTH_SHORT
                ).show()
                false
            }
            description!!.isEmpty() -> {
                Snackbar.make(
                        binding.root,
                        "Description cannot be empty!",
                        Snackbar.LENGTH_SHORT
                ).show()
                false
            }
            else -> true
        }
    }

    private fun checkDate(){
        viewModel.getCurrentDate()
        setNumberPickers(viewModel.month!!,viewModel.date!!)

    }

    private fun setNumberPickers(month: Int,day: Int) {
        val numPickerMonth = binding.numPickerMonth.apply {
            minValue = 1
            maxValue = 12
            value = month
        }
        binding.numPickerDay.apply {
            minValue = 1
            maxValue = when (numPickerMonth.value) {
                1 -> 31
                2 -> 28
                3 -> 31
                4 -> 30
                5 -> 31
                6 -> 30
                7 -> 31
                8 -> 31
                9 -> 30
                10 -> 31
                11 -> 30
                12 -> 31
                else -> 31
            }
            value = day
        }
    }



}