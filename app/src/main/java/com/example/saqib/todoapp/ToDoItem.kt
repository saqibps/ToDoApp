package com.example.saqib.todoapp


data class ToDoItem(val key:String,val desc:String,val time:String) {
    constructor():this("","","")
}