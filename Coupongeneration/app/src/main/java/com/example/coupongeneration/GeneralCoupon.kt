package com.example.coupongeneration

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GeneralCoupon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_coupon)

        val database = FirebaseDatabase.getInstance().getReference()
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


        discountTypeButton.setOnClickListener {
            if(discountTypeButton.text.equals("Percent")){
                discountTypeButton.setText("Flat Rate")
            }
            else{
                discountTypeButton.setText("Percent")
            }
        }

        dateButton.setOnClickListener{
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
        }

        calculateButton.setOnClickListener{
            if (discountTypeButton.text.equals("Percent"))
            {
                if(TextUtils.isEmpty(discountAmount.text) == false && TextUtils.isEmpty(minimumPurchase.text) == false)
                {
                    var discountPercent = discountAmount.text.toString().toDouble()
                    discountedPrice = minimumPurchase.text.toString().toDouble() - ((discountPercent/100) * (minimumPurchase.text.toString().toDouble()))
                    newPrice.setText("New Discounted Price: $" + priceFormat.format(discountedPrice) + " after user's cart has a total cost of $" + priceFormat.format(minimumPurchase.text.toString().toDouble()) + " before the discount")
                }

            }
            else
            {
                if(TextUtils.isEmpty(discountAmount.text) == false && TextUtils.isEmpty(minimumPurchase.text) == false)
                {
                    var discountPercent = discountAmount.text.toString().toDouble()
                    discountedPrice = (minimumPurchase.text.toString().toDouble()) - discountPercent
                    newPrice.setText("New Discounted Price: $" + priceFormat.format(discountedPrice) + " after user's cart has a total cost of $" + priceFormat.format(minimumPurchase.text.toString().toDouble()) + " before the discount")
                }
            }

            if (discountedPrice > -.001 && expirationDateSelected != "")
            {
                createCouponButton.visibility = View.VISIBLE
            }
        }

        createCouponButton.setOnClickListener{
            val specificCoupon = GenCoupon(discountedPrice, minimumPurchase.text.toString().toDouble(), totalUseAmount.text.toString().toInt(), 0, expirationDateSelected)
            val couponID = database.child("GeneralCoupons").push().key
            database.child("GeneralCoupons/" + couponID).setValue(specificCoupon)
        }

    }

    private fun UpdateLabel(MyCalander : Calendar): String {
        val myFormat = "MM-dd-yyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(MyCalander.time)
    }
}