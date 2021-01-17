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

class MainViewModel2 : ViewModel() {
    companion object {
        const val TAG = "MainVM2"
        const val YEAR = 2021
    }

    private val _firebasePostState = MutableStateFlow<MainViewModel2.FirebasePostState>(MainViewModel2.FirebasePostState.Empty)
    val firebasePostState: StateFlow<MainViewModel2.FirebasePostState> = _firebasePostState

    private lateinit var database: FirebaseDatabase
    private lateinit var calendar: Calendar
    lateinit var dayOfWeek: String
    lateinit var monthString: String
    lateinit var format: String
    var month: Int? = null
    var date: Int? = null
    var title: String? = null
    var description: String? = null
    var numberId: Int = 0


    fun postToFirebase(){
        database = Firebase.database
        val timestamp = System.currentTimeMillis()
        format = "${month}-${date}-${YEAR}"
        val dbRef = database.getReference("ITEMS/$format/$numberId")
        val item = ToDoItem(title,description,format,false,timestamp,numberId)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dbRef.setValue(item)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                _firebasePostState.value = FirebasePostState.Success
                                numberId = numberId++
                                Log.d(TAG,"Successfully posted to Firebase\nNew Number: $numberId")

                            } else {
                                _firebasePostState.value = FirebasePostState.Error(it.exception.toString())
                                Log.d(TAG,"${it.exception}")
                            }
                        }

            } catch (e: Exception) {
                _firebasePostState.value = FirebasePostState.Error(e.message.toString())
                Log.d(TAG,"CatchBlock of MainVM")
            }
        }
    }


    fun getCurrentDate(){
        calendar = Calendar.getInstance()
        month = calendar.get(Calendar.MONTH) + 1
        date = calendar.get(Calendar.DATE)
        dayOfWeek = checkDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        monthString = checkMonth(month!!)

    }


    private fun checkMonth(month: Int): String =
            when (month) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> "ERROR"
            }
    private fun checkDayOfWeek(day: Int): String =
            when (day) {
                1 -> "Sunday "
                2 -> "Monday "
                3 -> "Tuesday "
                4 -> "Wednesday "
                5 -> "Thursday "
                6 -> "Friday "
                7 -> "Saturday "
                else -> day.toString()
            }


    sealed class FirebasePostState {
        object Success : FirebasePostState()
        object Empty : FirebasePostState()
        data class Error(val message: String) : FirebasePostState()
    }






}