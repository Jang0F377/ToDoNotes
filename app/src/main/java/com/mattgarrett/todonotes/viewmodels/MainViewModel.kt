package com.mattgarrett.todonotes.viewmodels

import androidx.lifecycle.ViewModel
import com.mattgarrett.todonotes.data.ToDoItem

class MainViewModel : ViewModel() {

    var dummyData = mutableListOf(
            ToDoItem("Cats","Replace the Litter","",false),
            ToDoItem("House","Need to Take out Trash and clean a little","",false),
            ToDoItem("Office","Need to Call Someone","",false),
            ToDoItem("Drive","Take Sarah to Work","",true)
    )




}