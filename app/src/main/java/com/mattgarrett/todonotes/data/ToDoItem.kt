package com.mattgarrett.todonotes.data

data class ToDoItem(
        val toDoTitle: String? = "",
        val toDoMessage: String? = "",
        val date: String? = "",
        val isFinished: Boolean = false

)
