package com.mattgarrett.todonotes.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Error

class RegistrationViewModel :  ViewModel() {
    companion object {
        const val TAG = "RegVMActivity"
    }

    lateinit var auth: FirebaseAuth

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun registerNewUser(email: String,password: String) {
        auth = Firebase.auth
        CoroutineScope(Dispatchers.IO).launch {
            _loginUiState.value = LoginUiState.Loading
            delay(2500L)
            try {
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) _loginUiState.value = LoginUiState.Success
                        else {
                            _loginUiState.value = LoginUiState.Error(it.exception.toString())
                            return@addOnCompleteListener
                        }

                    }

            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error("${e.message}")
                Log.d(TAG,"${e.message}")
            }
        }

    }

    fun loginUser(email: String,password: String) {
        auth = Firebase.auth
        CoroutineScope(Dispatchers.IO).launch {
            _loginUiState.value = LoginUiState.Loading
            delay(2500L)
            try {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            _loginUiState.value = LoginUiState.Success
                        } else {
                            _loginUiState.value = LoginUiState.Error(it.exception.toString())
                            return@addOnCompleteListener
                        }

                    }

            } catch (e: Exception) {
                _loginUiState.value = LoginUiState.Error("${e.message}")
                Log.d(TAG,"Catch block ${e.message}")
            }
        }
    }





    sealed class LoginUiState {
        object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        object Loading : LoginUiState()
        object Empty : LoginUiState()
    }
}