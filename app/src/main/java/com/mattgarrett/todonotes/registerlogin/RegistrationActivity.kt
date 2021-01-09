package com.mattgarrett.todonotes.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mattgarrett.todonotes.MainActivity
import com.mattgarrett.todonotes.R
import com.mattgarrett.todonotes.databinding.ActivityRegistrationBinding
import com.mattgarrett.todonotes.viewmodels.RegistrationViewModel
import kotlinx.coroutines.flow.collect

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel

    companion object {
        val TAG = "RegistrationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        binding.tvAlreadyHaveAccount.setOnClickListener {
            Intent(this,LoginActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.btnRegister.setOnClickListener {
            Log.d(TAG,"Launching Registration")
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            initChecks(username, email, password, confirmPassword)
            Log.d(TAG,"Finished Init Checks")
            viewModel.registerNewUser(
                email, password
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect{
                when(it) {
                    is RegistrationViewModel.LoginUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Congratulations on joining the Guild",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar1.isVisible = false
                        Intent(this@RegistrationActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(this)
                        }
                    }
                    is RegistrationViewModel.LoginUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            "Error Occurred! Retry",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar1.isVisible = false

                    }
                    is RegistrationViewModel.LoginUiState.Loading -> {
                        binding.progressBar1.isVisible = true
                    }
                    else -> Unit
                }
            }
        }


    }


    private fun initChecks(username: String?, email: String?, password: String?, confirmPassword: String?){
        if (username!!.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Please Enter a Username!",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        } else if (email!!.isEmpty() || password!!.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Email/Passord cannot be blank",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        } else if (confirmPassword!!.isEmpty() || password != confirmPassword) {
            Snackbar.make(
                binding.root,
                "Passwords do not match!",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

    }

    }





