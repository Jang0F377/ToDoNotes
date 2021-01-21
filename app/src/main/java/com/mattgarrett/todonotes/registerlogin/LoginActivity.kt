package com.mattgarrett.todonotes.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mattgarrett.todonotes.MainActivity
import com.mattgarrett.todonotes.R
import com.mattgarrett.todonotes.databinding.ActivityLoginBinding
import com.mattgarrett.todonotes.viewmodels.RegistrationViewModel
import kotlinx.coroutines.flow.collect

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: RegistrationViewModel

    companion object{
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        binding.tvCreateAnAccount.setOnClickListener {
            Intent(this,RegistrationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        }

        binding.btnLogin.setOnClickListener {
            Log.d(TAG,"Begin Login Authentication")
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            initChecks(email,password)
            viewModel.loginUser(email, password)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect{
                when(it) {
                    is RegistrationViewModel.LoginUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Welcome Back to the Guild",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBarLogin.isVisible = false
                        Intent(this@LoginActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(this)
                        }
                    }
                    is RegistrationViewModel.LoginUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            "Error Occurred! ${it.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBarLogin.isVisible = false

                    }
                    is RegistrationViewModel.LoginUiState.Loading -> {
                        binding.progressBarLogin.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }



    private fun initChecks(email: String?,password: String?) {
        if (email!!.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Email Cannot be Empty!",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        } else if (password!!.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Password Cannot be Empty",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

    }
}