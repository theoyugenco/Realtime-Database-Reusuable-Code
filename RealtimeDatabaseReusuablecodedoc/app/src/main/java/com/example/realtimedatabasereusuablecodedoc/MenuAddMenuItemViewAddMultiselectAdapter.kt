package com.example.realtimedatabasereusuablecodedoc

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ActionMenuView.OnMenuItemClickListener
import android.widget.TextView

//class MenuAddMenuItemViewAddMultiselectAdapter (private val menuItemClickListener: ArrayList<MenuItemDC>):RecyclerView.Adapter<Menu>


class MenuAddMenuItemViewAddMultiselectAdapter (private val menuItemList : ArrayList<MenuItemDC>):RecyclerView.Adapter<MenuAddMenuItemViewAddMultiselectAdapter.MultiselectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MultiselectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val currentMenuItem = menuItemList[position]

        holder.name.text = currentMenuItem.name
        holder.description.text = currentMenuItem.description
        holder.price.text = currentMenuItem.price
    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    class MultiselectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.tvName)
        val description : TextView = itemView.findViewById(R.id.tvDescription)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
    }
}

