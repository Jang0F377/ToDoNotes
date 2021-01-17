package com.mattgarrett.todonotes.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mattgarrett.todonotes.data.ToDoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class NewToDoItemViewModel : ViewModel() {

    companion object {
        val TAG = "NewItemVMActivity"
    }

    private lateinit var database: FirebaseDatabase
    private val _firebasePostState = MutableStateFlow<FirebasePostState>(FirebasePostState.Empty)
    val firebasePostState: StateFlow<FirebasePostState> = _firebasePostState

    var title: String = ""
    var description: String = ""
    var month: Int? = null
    var date: Int? = null
    var year = 2021


    fun postToFirebase() {
        database = Firebase.database
        val timestamp = System.currentTimeMillis()
        val format = "${month}-${date}-$year"
        val dbRef = "ITEMS/$format/$description"
        val item = ToDoItem(title,description,format,false,timestamp,0)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.getReference(dbRef).apply {
                    setValue(item)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                _firebasePostState.value = FirebasePostState.Success
                                Log.d(TAG,"Successfully posted to Firebase")
                            }
                            else {
                                _firebasePostState.value = FirebasePostState.Error(it.exception.toString())
                                return@addOnCompleteListener
                            }
                        }
                }
            } catch (e: Exception) {
                _firebasePostState.value = FirebasePostState.Error(e.message.toString())
                Log.d(TAG,"CatchBlock MainVM ${e.message}")
            }
        }

    }





    sealed class FirebasePostState {
        object Success : FirebasePostState()
        object Empty : FirebasePostState()
        data class Error(val message: String) : FirebasePostState()
    }
}