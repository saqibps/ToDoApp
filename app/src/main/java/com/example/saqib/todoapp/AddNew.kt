package com.example.saqib.todoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_new.*
import java.util.*

class AddNew : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var uid:String
    var key:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)

         if (intent.getStringExtra("key") != null) {
             key = intent.getStringExtra("key")
             val desc:String = intent.getStringExtra("desc")
             todo_et.setText(desc)
         }


        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid
        databaseReference = FirebaseDatabase.getInstance().reference.child("ToDoList")

        add_todo_bt.setOnClickListener {
            if (!todo_et.text.isEmpty()) {
                val desc:String = todo_et.text.toString().trim()
                val dateTime:String = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
                if (key.isEmpty()) {
                     key = databaseReference.child(uid).push().key
                }
                val toDoItem = ToDoItem(key,desc,dateTime)
                saveToDoItem(toDoItem)
                finish()

            }
        }


    }

    private fun saveToDoItem(toDoItem: ToDoItem) {
        databaseReference.child(uid).child(toDoItem.key).setValue(toDoItem)
    }
}
