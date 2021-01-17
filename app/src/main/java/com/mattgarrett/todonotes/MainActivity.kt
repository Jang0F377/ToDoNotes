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
import com.mattgarrett.todonotes.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var todoItemAdapter: ToDoItemAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var calendar: Calendar



    companion object{
        const val TAG = "MainActivity"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ToDoNotes)
        supportActionBar?.title = "Today's List"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        checkLoggedInState()
        viewModel.getCurrentDate().apply {
            binding.tvMonth.text = viewModel.monthString
            binding.tvDate.text = viewModel.date
            binding.tvDayOfWeek.text = viewModel.dayString
        }
        setupRecyclerView()
        CoroutineScope(Dispatchers.IO).launch { getItemsFromFirebase() }







    }

    private fun setupRecyclerView() = binding.rvActivityMain.apply {
        todoItemAdapter = ToDoItemAdapter()
        adapter = todoItemAdapter
        addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
    }

    fun getItemsFromFirebase() {
        database = Firebase.database
        calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val date = calendar.get(Calendar.DATE)
        val dateFormat = "ITEMS/${month}-${date}-${MainViewModel.year}"
        val databaseRef = database.getReference(dateFormat)
//        val key =
//        Log.d(TAG,"Key: $key")
//        Log.d(TAG, "We are making it here ${dateFormat}")
        databaseRef.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val item = snapshot.getValue(ToDoItem::class.java)
//                Log.d(TAG,"Snapshot: $item" )
                val items = if (item != null) {
                    listOf<ToDoItem>(item)
                } else viewModel.dummyData

                todoItemAdapter.todos = items


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



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