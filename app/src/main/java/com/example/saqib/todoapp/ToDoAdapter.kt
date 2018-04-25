package com.example.saqib.todoapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView

class ToDoAdapter(val toDoList:ArrayList<ToDoItem>,val listener:(ToDoItem) -> Unit): RecyclerView.Adapter<ToDoAdapter.ToDoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ToDoItemViewHolder? {

        val itemView:View = LayoutInflater.from(parent?.context).inflate(R.layout.todo_item_layout,parent,false)
        return ToDoItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = toDoList.size

    override fun onBindViewHolder(holder: ToDoItemViewHolder?, position: Int) {
        holder?.bindView(toDoList[position])
    }
    inner class ToDoItemViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        val descTv: TextView =itemView.findViewById(R.id.description_tv)
        val timeTv: TextView =itemView.findViewById(R.id.time_tv)

        fun bindView(toDOItem:ToDoItem)= with(itemView){
            descTv.text = toDOItem.desc
            timeTv.text = toDOItem.time
            itemView.isLongClickable = true
            itemView.setOnLongClickListener {
                listener(toDOItem)
                 true
            }
        }
    }
}