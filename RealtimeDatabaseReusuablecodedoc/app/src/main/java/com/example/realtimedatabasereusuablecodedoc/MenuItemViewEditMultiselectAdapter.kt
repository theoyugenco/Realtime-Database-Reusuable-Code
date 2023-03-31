package com.example.realtimedatabasereusuablecodedoc

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class MenuItemViewEditMultiselectAdapter (private val menuItemList : ArrayList<MenuItemDC>):RecyclerView.Adapter<MenuItemViewEditMultiselectAdapter.MultiselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri

    /*
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MultiselectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val item = menuItemList[position]
        keyTBR = item.menuItemID

        //This is each card is created.
        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        holder.price.text = item.description.toString()
        holder.check.visibility = View.GONE


        holder.card.setOnClickListener() {
            Log.d(TAG, "A CLICK HAPPENED!")
            keyTBR = item.menuItemID
            //is the item already checked
            if (itemSelectedList.contains(keyTBR)) {
                itemSelectedList.remove(keyTBR)
                holder.check.visibility = View.GONE

                //It wouldn't make sense to show the options if nothing were to be selected
                //if (itemSelectedList.isEmpty()) {
                //showMenuDelete(false)
                //}
            }
            else{
                holder.check.visibility = View.VISIBLE
                //keyTBR = item.restaurantID //Change this code
                itemSelectedList.add(keyTBR!!)

                //showMenuDelete(true)
            }


        }
    }

    override fun getItemCount(): Int {
        return menuItemList.size
    }

    class MultiselectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.tvName)
        val description : TextView = itemView.findViewById(R.id.tvDescription)
        val price : TextView = itemView.findViewById(R.id.tvPrice)
        val card: CardView = itemView.findViewById(R.id.card)
        val check: ImageView = itemView.findViewById(R.id.checkSelect)

        /*
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }

         */
    }




}