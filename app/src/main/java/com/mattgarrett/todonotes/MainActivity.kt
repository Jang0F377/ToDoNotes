package com.mattgarrett.todonotes

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mattgarrett.todonotes.adapters.ToDoItemAdapter
import com.mattgarrett.todonotes.databinding.ActivityMainBinding
import com.mattgarrett.todonotes.registerlogin.LoginActivity
import com.mattgarrett.todonotes.registerlogin.RegistrationActivity
import com.mattgarrett.todonotes.viewmodels.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    companion object{
        const val TAG = "MainActivity"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ToDoNotes)
        supportActionBar?.title = "Today's Notes"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        checkLoggedInState()
        setDate()

        val adapter = ToDoItemAdapter(viewModel.dummyData)
        binding.rvActivityMain.adapter = adapter





    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDate() {
        val cal = Calendar.getInstance()
        val month: Int = cal.get(Calendar.MONTH)
        val monthString = checkMonth(month)
        val date = cal.get(Calendar.DATE)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val getDay = checkDayOfWeek(dayOfWeek)
        binding.tvMonth.text = monthString
        binding.tvDate.text = date.toString()
        binding.tvDayOfWeek.text = getDay
    }
    private fun checkMonth(month: Int): String =
        when (month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
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
    private fun checkLoggedInState() {
        auth = Firebase.auth
        if (auth.currentUser == null){
            Intent(this, RegistrationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewItem -> {
                Intent(this,NewToDoItemActivity::class.java).apply {
                    Log.d(TAG,"Creating New ToDoItem")
                    startActivity(this)
                }
            }
            R.id.menuSignOut -> {
                auth = Firebase.auth
                auth.signOut()
                Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(this)
                }

            }

        }
        return super.onOptionsItemSelected(item)
    }
}