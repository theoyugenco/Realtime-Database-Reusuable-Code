package com.example.coupongeneration

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.NonNull
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemCoupon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_coupon2)

        val menuSpinner = findViewById<Spinner>(R.id.menuSpinner)
        val items = ArrayList<String>()
        val database = FirebaseDatabase.getInstance().getReference()
        val originalPrice = findViewById<TextView>(R.id.priceView)
        var priceForCalc = 0.0
        var priceFormat = DecimalFormat("#.00")
        val createCouponButton = findViewById<Button>(R.id.createCouponButton)
        val discountTypeButton = findViewById<Button>(R.id.discountTypeButton)
        val calculateButton = findViewById<Button>(R.id.calcButton)
        val totalUseAmount = findViewById<EditText>(R.id.totalUses)
        val minimumPurchase = findViewById<EditText>(R.id.minimumPurchases)
        val discountAmount = findViewById<EditText>(R.id.discountAmount)
        val dateButton = findViewById<Button>(R.id.dateButton)
        val myCalandar = Calendar.getInstance()
        var expirationDateSelected = ""

        var discountedPrice = -1.0
        val newPrice = findViewById<TextView>(R.id.newPrice)
        var itemSelected = ""

        createCouponButton.visibility = View.INVISIBLE


        database.child("MenuItems").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                val loop = snapshot.children
                loop.forEach() {
                    var spinnerItem = it.child("name").value.toString()
                    if (spinnerItem != null) {
                        items.add(spinnerItem)
                    };
                }
                val aa = ArrayAdapter(this@ItemCoupon ,android.R.layout.simple_spinner_item, items);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
                with(menuSpinner)
                {
                    adapter = aa;
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    menuSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            itemSelected = menuSpinner.selectedItem.toString();
            database.child("MenuItems").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                    val originalPriceDouble = snapshot.child(itemSelected).child("price").value
                    originalPrice.setText("Original Price Per Item: $" + priceFormat.format(originalPriceDouble))
                    priceForCalc = originalPriceDouble.toString().toDouble();
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

    })



    discountTypeButton.setOnClickListener {
        if(discountTypeButton.text.equals("Percent")){
            discountTypeButton.setText("Flat Rate")
        }
        else{
            discountTypeButton.setText("Percent")
        }
    }

    dateButton.setOnClickListener{
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view, year, month, dayOfMonth ->
            myCalandar.set(Calendar.YEAR,year)
            myCalandar.set(Calendar.MONTH,month)
            myCalandar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            expirationDateSelected = UpdateLabel(myCalandar)
            if (discountedPrice > -.001 && expirationDateSelected != "")
            {
                createCouponButton.visibility = View.VISIBLE
            }
        }, myCalandar.get(Calendar.YEAR),myCalandar.get(Calendar.MONTH), myCalandar.get(Calendar.DAY_OF_MONTH))
        dpd.show()


    }

    calculateButton.setOnClickListener{
        if (discountTypeButton.text.equals("Percent"))
        {
            if(TextUtils.isEmpty(discountAmount.text) == false && TextUtils.isEmpty(minimumPurchase.text) == false)
            {
                var discountPercent = discountAmount.text.toString().toDouble()
                discountedPrice = priceForCalc - ((discountPercent/100) * (priceForCalc * (minimumPurchase.text.toString().toInt())))
                newPrice.setText("New Discounted Price: $" + priceFormat.format(discountedPrice) + " for " + minimumPurchase.text.toString() +" " + itemSelected)
            }

        }
        else
        {
            if(TextUtils.isEmpty(discountAmount.text) == false && TextUtils.isEmpty(minimumPurchase.text) == false)
            {
                var discountPercent = discountAmount.text.toString().toDouble()
                discountedPrice = (priceForCalc * (minimumPurchase.text.toString().toInt())) - discountPercent
                newPrice.setText("New Discounted Price: $" + priceFormat.format(discountedPrice) + " for " + minimumPurchase.text.toString() +" " + itemSelected)
            }
        }

        if (discountedPrice > -.001 && expirationDateSelected != "")
        {
            createCouponButton.visibility = View.VISIBLE
        }
    }

    createCouponButton.setOnClickListener{
        val specificCoupon = SpecificCoupon(itemSelected, discountedPrice, minimumPurchase.text.toString().toInt(), totalUseAmount.text.toString().toInt(), 0, expirationDateSelected)
        val couponID = database.child("SpecificCoupon").push().key
        database.child("SpecificCoupon/" + couponID).setValue(specificCoupon)
    }

    }

    private fun UpdateLabel(MyCalander : Calendar): String {
        val myFormat = "MM-dd-yyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(MyCalander.time)
    }
}