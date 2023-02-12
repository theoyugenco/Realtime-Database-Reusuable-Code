package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

//Handles arraylist of users in real time updates
class UserAdapter (val context: Context, var userList: ArrayList<UserDC>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    //value to be displayed in the view
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textUser)
    }

    //Sets user list to filtered list
    fun setFilteredList(uList: ArrayList<UserDC>) {
        this.userList = uList
        notifyDataSetChanged()
    }

    //Setups the layout for a card view for a user in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    //get total number of users
    override fun getItemCount(): Int {
        return userList.size
    }

    //binds the current user's name to the view holder. carries over
    //name and UID of chosen user to represent a recipient in messaging
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.name.text = currentUser.uName

        holder.itemView.setOnClickListener {
            val intent = Intent(context, Messaging::class.java)
            intent.putExtra("name", currentUser.uName)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }
}