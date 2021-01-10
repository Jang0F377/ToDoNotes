package com.mattgarrett.todonotes.data

data class ToDoItem(
        val toDoTitle: String? = "",
        val toDoDescription: String? = "",
        val date: String? = "",
        val isFinished: Boolean = false

)
