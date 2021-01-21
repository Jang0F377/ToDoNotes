package com.mattgarrett.todonotes.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mattgarrett.todonotes.adapters.ToDoItemAdapter
import com.mattgarrett.todonotes.data.ToDoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel2 : ViewModel() {
    companion object {
        private const val TAG = "MainVM2Activity"
        const val YEAR = 2021
    }

    private val _firebasePostState = MutableStateFlow<MainViewModel2.FirebasePostState>(MainViewModel2.FirebasePostState.Empty)
    val firebasePostState: StateFlow<MainViewModel2.FirebasePostState> = _firebasePostState

    private lateinit var database: FirebaseDatabase
    private lateinit var calendar: Calendar
    lateinit var dayOfWeek: String
    lateinit var monthString: String
    var format: String = ""
    var month: Int? = null
    var date: Int? = null
    var title: String? = null
    var description: String? = null


    fun postToFirebase(){
        database = Firebase.database
        val timestamp = System.currentTimeMillis()
        format = "${month}-${date}-${YEAR}"
        val dbRef = database.reference.child("ITEMS/$format").child("$timestamp")
        val item = ToDoItem(title,description,format,false,timestamp)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dbRef.setValue(item)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                _firebasePostState.value = FirebasePostState.Success
                                Log.d(TAG,"Successfully posted to Firebase")

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

    fun getItemsFromFirebase(adapter: ToDoItemAdapter) {
        format = "ITEMS/${month}-${date}-${YEAR}"
        database = Firebase.database
        val ref =  database.getReference(format)
        ref.addChildEventListener( object : ChildEventListener {
            val todoList = mutableListOf<ToDoItem>()
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(ToDoItem::class.java) as ToDoItem

                todoList.add(item)
                adapter.todos = todoList

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO()
            }
        })
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

//    var dummyData = mutableListOf(
//            ToDoItem("Office","Need to fix program to print to SV","",false),
//            ToDoItem("Cats","Replace the Litter","",true),
//            ToDoItem("House","Need to Take out Trash and clean a little","",false),
//            ToDoItem("Office","Need to Call Someone","",false),
//            ToDoItem("Drive","Take Sarah to Work","",true)
//    )






}