package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//This class manages the private messaging between two users.
class Messaging : AppCompatActivity() {
    //initialize recycler view, input box, send button
    //adapter, list, and database
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var inputBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var database: DatabaseReference

    //Setup rooms for creating channels in database
    var receiverRoom: String? = null
    var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        //Receive extra member variables from user adapter to represent
        //recipient in channel
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        //retrieve sender UID and setup the database
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference()

        //creates the unique IDs for sender and receiver rooms in
        //database
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        //creates label for chat window at top of screen
        supportActionBar?.title = name

        //initialize recyclerview, input, send button, message list,
        //and message adapter
        chatRecyclerView = findViewById(R.id.messageRecyclerView)
        inputBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        //setup the layout for the chat and link the recycler view to
        //the adapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //Updates the database in real time whenever a message is sent
        database.child("Chats").child(senderRoom!!).child("Messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //Adds the message to the sender and receiver channels
        //on the database upon tapping send button
        sendButton.setOnClickListener {
            val message = inputBox.text.toString()
            val messageObject = Message(message, senderUid)

            database.child("Chats").child(senderRoom!!).child("Messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    database.child("Chats").child(receiverRoom!!).child("Messages").push()
                        .setValue(messageObject)
                }
            inputBox.setText("")
        }
    }
}