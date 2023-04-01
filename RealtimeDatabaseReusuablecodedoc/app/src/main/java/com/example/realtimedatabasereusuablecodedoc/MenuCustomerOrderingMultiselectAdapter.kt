package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text

//class MenuAddMenuItemViewAddMultiselectAdapter (private val menuItemClickListener: ArrayList<MenuItemDC>):RecyclerView.Adapter<Menu>


class MenuCustomerOrderingMultiselectAdapter (
    private val currentMenuItemArrayList : ArrayList<MenuItemDC>,
    private val id : String,
    private val menuItemNameList : ArrayList<String>,
    private val menuItemPriceList: ArrayList<String>,
    private val showMenuCart: (Boolean) -> Unit
):RecyclerView.Adapter<MenuCustomerOrderingMultiselectAdapter.MultiselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    //private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null
    private lateinit var storage: StorageReference
    private lateinit var selectedImg: Uri

    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val add: ImageView = view.findViewById(R.id.ci_add)
        val delete: ImageView = view.findViewById(R.id.ci_minus)
        val counter: TextView = view.findViewById(R.id.ci_quantity)
        //val check: ImageView = view.findViewById(R.id.checkSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return MultiselectViewHolder(adapterLayout)
    }
    /*
        override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
            val currentMenuItem = menuItemList[position]

            holder.name.text = currentMenuItem.name
            holder.description.text = currentMenuItem.description
            holder.price.text = currentMenuItem.price
        }
    */
    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {

        val item = currentMenuItemArrayList[position]
        //keyTBR = item.menuItemID

        //This is each card is created.
        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        holder.price.text = item.price.toString()
        val name: String? = item.name.toString()
        val price: String? = item.price.toString()
        //var count: String? = holder.counter.text.toString()

        holder.add.setOnClickListener() {
            menuItemNameList.add(name!!)
            menuItemPriceList.add(price!!)
            for (i in menuItemNameList){
                Log.d(TAG, "N:" + i)
            }
            for (j in menuItemPriceList){
                Log.d(TAG, "N:" + j)
            }
            var count: String? = holder.counter.text.toString()
            var c : Int = count!!.toInt()
            c = c + 1
            Log.d(TAG, "c is: " + c)
            holder.counter.setText(c.toString())
            showMenuCart(true)
        }

        holder.delete.setOnClickListener(){
            menuItemNameList.remove(name!!)
            menuItemPriceList.remove(price!!)
            for (i in menuItemNameList){
                Log.d(TAG, "N:" + i)
            }
            for (j in menuItemPriceList){
                Log.d(TAG, "N:" + j)
            }
            var count: String? = holder.counter.text.toString()
            var c : Int = count!!.toInt()
            c = c - 1
            Log.d(TAG, "c is: " + c)
            if (c < 0){
                c = 0
            }
            holder.counter.setText(c.toString())
            if (menuItemNameList.isEmpty()) {
                showMenuCart(false)
            }
        }
    }


    override fun getItemCount(): Int {
        return currentMenuItemArrayList.size
    }


}

