
package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
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


class LocationViewEditMultiselectAdapter(
    private var locationList : ArrayList<RestaurantDC>,
    private val showMenuDelete: (Boolean) -> Unit

): RecyclerView.Adapter<LocationViewEditMultiselectAdapter.MultiselectViewHolder>() {

    private lateinit var database: DatabaseReference
    private var isEnable = false
    private var itemSelectedList = ArrayList<String>()
    private var keyTBR : String? = null
    private var TAG: String? = null

    class MultiselectViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.card)
        val name: TextView = view.findViewById(R.id.tvName)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val check: ImageView = view.findViewById(R.id.checkSelect)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiselectViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.restaurant, parent, false)
        Log.d(TAG, "do i run when deletion")
        return MultiselectViewHolder(adapterLayout)
    }


    override fun getItemCount(): Int {
        return locationList.size
    }

    private fun selectItem(
        holder: LocationViewEditMultiselectAdapter.MultiselectViewHolder,
        item: RestaurantDC,
        position: Int
    ) {
        //holder.check.
        isEnable = true
        keyTBR = item.restaurantID //Change this code
        itemSelectedList.add(keyTBR!!)

        /*
        if (itemSelectedList.size == 1){
            showMenuEdit
        }
        */
        showMenuDelete(true)
    }

    override fun onBindViewHolder(holder: MultiselectViewHolder, position: Int) {
        val item = locationList[position]
        keyTBR = item.restaurantID

        holder.name.text = item.name.toString()
        holder.description.text = item.description.toString()
        holder.check.visibility = View.GONE


        holder.card.setOnLongClickListener() {
            selectItem(holder, item, position)
            holder.check.visibility = View.VISIBLE

            true
        }
        holder.card.setOnClickListener() {
            //is the item already checked
            if (itemSelectedList.contains(keyTBR)) {
                itemSelectedList.remove(keyTBR)
                holder.check.visibility = View.GONE

                //It wouldn't make sense to show the options if nothing were to be selected
                if (itemSelectedList.isEmpty()) {
                    showMenuDelete(false)
                    isEnable = false
                }

            } else if (isEnable) {
                selectItem(holder, item, position)
            }
        }





    }

    fun deleteSelectedItem(){
        if(itemSelectedList.isNotEmpty()){
            val size: Int = itemSelectedList.size
            var i : Int = 0
            for (i in 0..(size-1)){
                Log.d(TAG,"BUT THE SIZE OF LOCATIONLIST IS: " + locationList.size)
                Log.d(TAG, "size: " + size.toString())
                keyTBR = itemSelectedList.get(i)

                database = FirebaseDatabase.getInstance().getReference("Users/Restaurants")

                val key: String? = keyTBR
                database.child(key!!).removeValue().addOnFailureListener(){
                    Log.d(TAG, "tell me the truth")
                    //Toast.makeText(this, "BIG CRITICAL NO NO!", Toast.LENGTH_SHORT)
                }.addOnSuccessListener {
                    Log.d(TAG, "so it actually worked?")
                }

                //locationList.removeAt(0)
                /*
                if (i == size-1){
                    i += 1
                    val intent = Intent(this@LocationViewEditMultiselectAdapter, RestaurantViewEdit::class.java)
                    startActivity(intent)
                }

                 */
            }


            //itemSelectedList = ArrayList<String>()
            itemSelectedList.clear()


            //val intent = Intent(this)

            Log.d(TAG, "a cool")
        }
        //notifyDataSetChanged()
    }
}
