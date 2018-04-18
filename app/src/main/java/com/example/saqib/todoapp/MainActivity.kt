package com.example.saqib.todoapp

import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.RecyclerView
import android.view.View


class MainActivity : AppCompatActivity() {

    lateinit var databaseReference:DatabaseReference
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var uid:String
    lateinit var toDoList:ArrayList<ToDoItem>
    lateinit var toDoAdapter: ToDoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid
//         FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ToDoList").child(uid)
        toDoList = arrayListOf()
        toDoAdapter = ToDoAdapter(toDoList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(VerticalSpaceItemDecoration(48))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.getItemId()) {
                R.id.signout -> {
                    auth.signOut()
                    startActivity(Intent(this,SignUpActivity::class.java) )
                    finish()
                    return true
                }
                R.id.add -> {
                    startActivity(Intent(this@MainActivity, AddNew::class.java))
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    inner class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}
