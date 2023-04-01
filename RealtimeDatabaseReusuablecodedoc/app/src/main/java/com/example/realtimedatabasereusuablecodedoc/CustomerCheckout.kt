package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import kotlin.math.roundToInt

class CustomerCheckout : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var restaurantMenu: Menu? = null
    private var itemNameArrayList : ArrayList<String> = ArrayList<String>()
    private var itemPriceArrayList : ArrayList<String> = ArrayList<String>()
    //private lateinit var itemNameSet: Set<String>
    //private lateinit var itemPriceSet: Set<String>
    private var itemNameDistinctArrayList : ArrayList<String> = ArrayList<String>()
    private var itemPriceDistinctArrayList : ArrayList<String> = ArrayList<String>()
    private var iNAL : ArrayList<String> = ArrayList<String>()
    private var itemCountArrayList : ArrayList<Int> = ArrayList<Int>()
    private lateinit var recyclerv: RecyclerView
    private lateinit var msAdapterMenuItems: CustomerCheckoutAdapter
    private var TAG: String? = null
    private lateinit var subtotalPrice: TextView
    private lateinit var taxPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_checkout)
        recyclerv = findViewById(R.id.cc_cartItemsRecycler)
        recyclerv.layoutManager = LinearLayoutManager(this)
        recyclerv.setHasFixedSize(true)

        val bundle = intent.extras
        if (bundle != null){
            itemNameArrayList = bundle.getStringArrayList("itemName")!!
            itemPriceArrayList = bundle.getStringArrayList("itemPrice")!!
        }

        iNAL = ArrayList(itemNameArrayList.toList())
        for (i in itemNameArrayList){
            Log.d(TAG, "Nx:" + i)
        }

        getUserData()

        subtotalPrice = findViewById(R.id.cc_subtotal_price)
        var subtotal = 0.00
        for (i in itemPriceArrayList) {
            subtotal += i.toDouble()
        }
        val roundedsubtotal = (subtotal * 100.0).roundToInt()/100.0

        subtotalPrice.setText(roundedsubtotal.toString())

        taxPrice = findViewById(R.id.cc_fee_price)
        val tax = ((roundedsubtotal * 0.10) * 100.0).roundToInt()/100.0
        taxPrice.setText(tax.toString())
    }


    private fun getUserData() {
        //itemNameSet = itemNameArrayList.toList().toSet()
        //itemPriceSet = itemPriceArrayList.toList().toSet()

        //We need a set because we want to efficiently display to the Customer what they ordered.
        itemNameDistinctArrayList = ArrayList(itemNameArrayList.toList().toSet().toList())
        itemPriceDistinctArrayList = ArrayList(itemPriceArrayList.toList().toSet().toList())

        Log.d(TAG, "this ran100")
        val originalSize : Int = iNAL.size
        Log.d(TAG, "inal size: " + originalSize.toString())

        //We are trying to get the item count/quantity of each distinct item
        for (i in itemNameDistinctArrayList){
            var c : Int = 0

            //iNAL.remove(i).equals(true)
            while (iNAL.remove(i) == true){
                c++
            }

            itemCountArrayList.add(c)
        }

        //This checks if the sum of all the counts matches the original size
        var sum : Int = 0

        for (j in itemCountArrayList)
        {
            sum += j
        }

        if (sum.equals(originalSize)){
            Log.d(TAG, "correct Math!")
        }
        else{
            Log.d(TAG, "dissapoint")
        }


        msAdapterMenuItems = CustomerCheckoutAdapter(iNAL, itemNameDistinctArrayList, itemPriceDistinctArrayList, itemCountArrayList)
        recyclerv.adapter = msAdapterMenuItems
    }


}