package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

//a message adapter that will keep track of all messages in any given
//chat channel
class MessageAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //values to record whether or not a message was to be sent or received
    val item_received = 1
    val item_sent = 2

    //represents the display for a sent message
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sent_text)
    }

    //represents the display for a received message
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.received_text)
    }

    //when a message is introduced to the adapter, choose the layout
    //depending on view's status
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)
            return ReceiveViewHolder(view)
        }
        else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            return SentViewHolder(view)
        }
    }

    //get the size of the list
    override fun getItemCount(): Int {
        return messageList.size
    }

    //Displays the specified message depending on if its
    //sent or received
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else {
            val currentMessage = messageList[position]
            val viewHolder = holder as ReceiveViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
    }

    //determines the status of sent or received to a given message
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderUid)) {
            return item_sent
        }
        else {
            return item_received
        }
    }
}
