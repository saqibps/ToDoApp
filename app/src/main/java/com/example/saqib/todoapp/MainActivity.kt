package com.example.saqib.todoapp

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast


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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ToDoList").child(uid)
        toDoList = arrayListOf()
        toDoAdapter = ToDoAdapter(toDoList) {toDoItem ->
            val popupMenu = PopupMenu(this,recycler_view.getChildAt(toDoList.indexOf(toDoItem)))
            popupMenu.inflate(R.menu.item_menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                if (item != null) {
                    when(item.itemId){
                        R.id.edit -> {
                            val intent:Intent = Intent(this ,AddNew::class.java)
                            intent.putExtra("key",toDoItem.key)
                            intent.putExtra("desc",toDoItem.desc)
                            startActivity(intent)
                             true
                        }
                        R.id.delete -> {
                            AlertDialog.Builder(this).setMessage("Are you sure You want to delete this item?")
                                    .setNegativeButton("No",null)
                                    .setPositiveButton("Yes", { dialogInterface, i ->
                                        databaseReference.child(toDoItem.key).removeValue()
                                        true
                                    }).show()

                        }
                        else ->  false
                    }
                }
                 true
            }
            popupMenu.show()
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(VerticalSpaceItemDecoration(48))
        recycler_view.adapter = toDoAdapter



        databaseReference.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                if  (snapshot != null) {
                    val toDoItem:ToDoItem = snapshot.getValue(ToDoItem::class.java)!!
                    if (toDoList.contains(toDoItem)) {
                    toDoList.set(toDoList.indexOf(toDoItem),toDoItem)
                        toDoAdapter.notifyDataSetChanged()
                    }
                }
            }

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
                    val alertDialog:AlertDialog.Builder = AlertDialog.Builder(this)
                    alertDialog.setTitle("SignOut").setMessage("Are You Sure..??").setNegativeButton("No",null)
                            .setPositiveButton("Yes", { dialogInterface, i ->
                                auth.signOut()
                    startActivity(Intent(this,SignUpActivity::class.java) )
                    finish()
                                true
                            }).show()

//
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
