package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Locale

class ChatSearch : AppCompatActivity() {
    //Initialize views, list, adapter, and firebase
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var userList: ArrayList<UserDC>
    private lateinit var adapter: UserAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search)

        //initialize firebase auth and reference
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        //initialize views, list, and user adapter
        recyclerView = findViewById(R.id.chatRecyclerView)
        searchView = findViewById(R.id.chatSearchView)
        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        //setup layout for recyclerview and connect adapter to recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //update the user list whenever a change is detected in the database
        //only list users other than the current user
        database.child("Users/Merchants").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isMerchant: Boolean = false
                for(user in userList) {
                    isMerchant = checkMerchant(user.uid)
                    if (isMerchant) {
                        userList.remove(user)
                    }
                }
                //userList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(UserDC::class.java)
                    if(firebaseAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        database.child("Users/Customers").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isCustomer: Boolean = false
                for(user in userList) {
                    isCustomer = checkMerchant(user.uid)
                    if (isCustomer) {
                        userList.remove(user)
                    }
                }
                //userList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(UserDC::class.java)
                    if(firebaseAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        //Update displayed users in response to searchbox text
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                filterList(p0)
                return true
            }

        })
    }

    //filter the list to correspond to the query
    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<UserDC>()
            for (i in userList) {
                if (i.uName.toString().lowercase(Locale.ROOT).contains(query.lowercase())) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No data present", Toast.LENGTH_SHORT).show()
            }
            else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun checkMerchant(uid: String?): Boolean {
        database = FirebaseDatabase.getInstance().getReference("Users/Merchants")
        var itExists: Boolean = false
        database.child(uid!!).get().addOnSuccessListener {
            //If a node/entry of that specific UserName exists
            if(it.exists()) {
                itExists = true
            }
        }
        return itExists
    }

    private fun checkCustomer(uid: String?): Boolean {
        database = FirebaseDatabase.getInstance().getReference("Users/Customers")
        var itExists: Boolean = false
        database.child(uid!!).get().addOnSuccessListener {
            //If a node/entry of that specific UserName exists
            if(it.exists()) {
                itExists = true
            }
        }
        return itExists
    }
}