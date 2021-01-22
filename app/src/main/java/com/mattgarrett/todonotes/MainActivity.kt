package com.mattgarrett.todonotes

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mattgarrett.todonotes.adapters.ToDoItemAdapter
import com.mattgarrett.todonotes.data.ToDoItem
import com.mattgarrett.todonotes.databinding.ActivityMainBinding
import com.mattgarrett.todonotes.registerlogin.LoginActivity
import com.mattgarrett.todonotes.viewmodels.MainViewModel2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel2
    private lateinit var todoItemAdapter: ToDoItemAdapter
    private lateinit var datePicker: DatePickerDialog

    companion object{
        private const val TAG = "MainActivity"
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ToDoNotes)
        supportActionBar?.title = "Today's List"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel2::class.java)
        checkLoggedInState()
        viewModel.getCurrentDate().apply {
            binding.tvMonth.text = viewModel.monthString
            binding.tvDate.text = viewModel.date.toString()
            binding.tvDayOfWeek.text = viewModel.dayOfWeek
        }
        setupRecyclerView()
        CoroutineScope(Dispatchers.Default).launch { viewModel.getItemsFromFirebase(todoItemAdapter,viewModel.month!! + 1,viewModel.date!!) }





    }

    private fun setupRecyclerView() = binding.rvActivityMain.apply {
        todoItemAdapter = ToDoItemAdapter(this@MainActivity)
        adapter = todoItemAdapter
//        addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
    }

    private fun checkLoggedInState() {
        auth = Firebase.auth
        if (auth.currentUser == null){
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewItem -> {
                Intent(this,NewToDoItemActivity::class.java).apply {
                    Log.d(TAG,"Creating New ToDoItem")
                    startActivity(this)
                }
            }
            R.id.menuGoToDate -> {
                showDatePicker()
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePicker() {
        val year = 2021

        datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            binding.tvMonth.text = viewModel.checkMonth(month)
            binding.tvDate.text = dayOfMonth.toString()
            Log.d(TAG,"Date Changed to ${viewModel.checkMonth(month)} ${dayOfMonth}, $year")
            viewModel.getItemsFromFirebase(todoItemAdapter,month +1,dayOfMonth)

        },year, viewModel.month!! ,viewModel.date!!)
        datePicker.show()

    }
}