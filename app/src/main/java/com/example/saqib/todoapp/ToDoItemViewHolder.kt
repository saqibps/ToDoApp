package com.example.saqib.todoapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ToDoItemViewHolder(myView:View): RecyclerView.ViewHolder(myView) {

    val descTv:TextView =itemView.findViewById(R.id.description_tv)
    val timeTv:TextView =itemView.findViewById(R.id.time_tv)

    fun bindView(toDOItem:ToDoItem) {
        descTv.text = toDOItem.desc
        timeTv.text = toDOItem.time
    }
}