package com.example.saqib.todoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var databaseReference:DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var uid:String
    lateinit var toDoList:ArrayList<ToDoItem>
    lateinit var toDoAdapter: ToDoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("ToDoList").child(uid)
        toDoList = arrayListOf()
        toDoAdapter = ToDoAdapter(toDoList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = toDoAdapter


        databaseReference.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                if (snapshot != null) {
                    val toDoItem:ToDoItem = snapshot.getValue(ToDoItem::class.java)!!
                    toDoList.add(toDoItem)
                    toDoAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot != null) {
                    val toDoItem:ToDoItem = snapshot.getValue(ToDoItem::class.java)!!
                    if (toDoList.contains(toDoItem)) {
                        toDoList.remove(toDoItem)
                        toDoAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }
}
