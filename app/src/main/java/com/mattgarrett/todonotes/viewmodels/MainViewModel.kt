package com.mattgarrett.todonotes.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mattgarrett.todonotes.data.ToDoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Error
import java.util.*

class MainViewModel : ViewModel() {

//    lateinit var database: FirebaseDatabase
    lateinit var monthString: String
    lateinit var dayString: String
    lateinit var date: String
    private lateinit var calendar: Calendar



    companion object {
        const val TAG = "MainVMActivity"
        val year = 2021
    }

    fun getItemsFromFirebase() {

//        CoroutineScope(Dispatchers.IO).launch {
//            try { database.getReference("ITEMS/$dateFormat").apply {
//                Log.d(TAG, dateFormat)
//                    addChildEventListener(object : ChildEventListener {
//
//                        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                            val item: ToDoItem? = snapshot.getValue(ToDoItem::class.java)
//                            todoItem = listOf(item!!)
//
//
//                        }
//
//                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                        }
//
//                        override fun onChildRemoved(snapshot: DataSnapshot) {
//                        }
//
//                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                        }
//                    })
//                }
//
//            } catch (e: Exception){
//                Log.d(TAG,"CatchBlock retrieve from Firebase")
//            }
//        }




    }

    fun getCurrentDate() {
        calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        monthString = checkMonth(month)
        date = calendar.get(Calendar.DATE).toString()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        dayString = checkDayOfWeek(dayOfWeek)
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









    var dummyData = mutableListOf(
            ToDoItem("Office","Need to fix program to print to SV","",false),
            ToDoItem("Cats","Replace the Litter","",true),
            ToDoItem("House","Need to Take out Trash and clean a little","",false),
            ToDoItem("Office","Need to Call Someone","",false),
            ToDoItem("Drive","Take Sarah to Work","",true)
    )





}