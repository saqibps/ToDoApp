package com.example.saqib.todoapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

class ToDoAdapter(val toDoList:ArrayList<ToDoItem>): RecyclerView.Adapter<ToDoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ToDoItemViewHolder {

        val itemView:View = LayoutInflater.from(parent?.context).inflate(R.layout.todo_item_layout,parent,false)
        return ToDoItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = toDoList.size

    override fun onBindViewHolder(holder: ToDoItemViewHolder?, position: Int) {
        holder?.bindView(toDoList[position])
    }
}