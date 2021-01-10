package com.mattgarrett.todonotes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mattgarrett.todonotes.data.ToDoItem
import com.mattgarrett.todonotes.databinding.ToDoItemBinding

class ToDoItemAdapter(
        private val toDosList: List<ToDoItem>,
) : RecyclerView.Adapter<ToDoItemAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding : ToDoItemBinding = ToDoItemBinding.inflate(view,parent,false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            tvItemTitle.text = toDosList[position].toDoTitle
            tvItemDescription.text = toDosList[position].toDoDescription
            itemCheckBox.isChecked = toDosList[position].isFinished
        }

    }

    override fun getItemCount(): Int {
        return toDosList.size
    }

    inner class ToDoViewHolder(val binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root)
}