package com.example.saqib.todoapp


data class ToDoItem(val key:String,val desc:String,val time:String) {
    constructor():this("","","")

    override fun equals(other: Any?): Boolean {

        if (other is ToDoItem) {
            if (this.key == other.key) {
                return true
            } else
                return false
        }
        return false
    }
}