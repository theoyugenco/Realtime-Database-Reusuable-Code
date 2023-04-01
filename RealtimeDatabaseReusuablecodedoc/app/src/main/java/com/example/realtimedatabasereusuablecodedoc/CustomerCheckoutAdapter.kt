package com.example.realtimedatabasereusuablecodedoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log



class CustomerCheckoutAdapter(
    private var iNAL : ArrayList<String>,
    private var itemNameDistinctArrayList : ArrayList<String>,
    private var itemPriceDistinctArrayList : ArrayList<String>,
    private var itemCountArrayList: ArrayList<Int>,
    ) : RecyclerView.Adapter<CustomerCheckoutAdapter.MultiselectViewHolder>() {

    private var keyTBR : String? = null
    private var TAG: String? = null


    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val quantity: TextView = view.findViewById(R.id.ci_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        Log.d(TAG, "this ran")
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.checkout_items, parent, false)
        return MultiselectViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        Log.d(TAG, "this ran2")
        //val size : Int = getItemCount()


        val name : String = itemNameDistinctArrayList[position]
        val price : String = itemPriceDistinctArrayList[position]
        val count = itemCountArrayList[position]

        Log.d(TAG, "Nd: " + name)
        Log.d(TAG, "Pd: " + price)
        Log.d(TAG, "Cd: " + count)

        //This is each card is created.
        holder.name.text = name
        holder.price.text = price
        holder.quantity.text = count.toString()


    }


    override fun getItemCount(): Int {
        return itemNameDistinctArrayList.size
    }


}