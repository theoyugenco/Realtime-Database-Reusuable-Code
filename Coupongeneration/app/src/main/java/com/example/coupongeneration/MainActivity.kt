package com.example.coupongeneration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val specificCouponButton = findViewById<Button>(R.id.specificCoupon)
        specificCouponButton.setOnClickListener{
            val specificCouponGeneratorPage = Intent(this,ItemCoupon::class.java)
            startActivity(specificCouponGeneratorPage)
        }

        val generalCouponCouponButton = findViewById<Button>(R.id.generalCoupon)
        generalCouponCouponButton.setOnClickListener{
            val generalCouponGeneratorPage = Intent(this,GeneralCoupon::class.java)
            startActivity(generalCouponGeneratorPage)
        }
    }

}