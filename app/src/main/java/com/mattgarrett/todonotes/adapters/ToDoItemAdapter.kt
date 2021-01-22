package com.mattgarrett.todonotes.adapters

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import com.mattgarrett.todonotes.MainActivity
import com.mattgarrett.todonotes.R
import com.mattgarrett.todonotes.data.ToDoItem
import com.mattgarrett.todonotes.databinding.ToDoItemBinding

class ToDoItemAdapter(context: Context) : RecyclerView.Adapter<ToDoItemAdapter.ToDoViewHolder>() {
    companion object{
        const val TAG = "AdapterActivity"
    }



    private val diffCallback = object : DiffUtil.ItemCallback<ToDoItem>(){
        override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem.timestamp == newItem.timestamp

        }

        override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var todos: List<ToDoItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding : ToDoItemBinding = ToDoItemBinding.inflate(view,parent,false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            val todo = todos[position]
            tvItemTitle.text = todo.toDoTitle
            tvItemDescription.text = todo.toDoDescription
            itemCheckBox.isChecked = todo.isFinished
        }


    }

    override fun getItemCount() = todos.size

    inner class ToDoViewHolder(val binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root)




}