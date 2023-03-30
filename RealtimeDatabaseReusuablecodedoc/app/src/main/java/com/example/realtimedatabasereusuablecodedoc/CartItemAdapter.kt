/*
package com.example.realtimedatabasereusuablecodedoc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter (val context: Context, var cartList: ArrayList<OrderItemDC>): RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    //value to be displayed in the view
    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById<TextView>(R.id.textItem)
        val price : TextView = itemView.findViewById<TextView>(R.id.textPrice)
    }

    //Setups the layout for a card view for a user in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.cartitem_layout, parent, false)
        return CartItemViewHolder(view)
    }

    //get total number of users
    override fun getItemCount(): Int {
        return cartList.size
    }

    //binds the current user's name to the view holder. carries over
    //name and UID of chosen user to represent a recipient in messaging
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = cartList[position]

        holder.name.text = currentItem.name
        holder.price.text = currentItem.price
    }
}

 */