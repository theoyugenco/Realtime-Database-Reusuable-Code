package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class UserAdapter (val context: Context, var userList: ArrayList<UserDC>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textUser)
    }

    fun setFilteredList(uList: ArrayList<UserDC>) {
        this.userList = uList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

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