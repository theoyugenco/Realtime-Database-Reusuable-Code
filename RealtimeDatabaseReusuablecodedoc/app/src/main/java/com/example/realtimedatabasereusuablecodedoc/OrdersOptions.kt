package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OrdersOptions : AppCompatActivity() {

    private lateinit var activeButton: Button
    private lateinit var pastButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_options)

        activeButton = findViewById(R.id.activeOrders)
        activeButton.setOnClickListener(){
            val intent = Intent(this, CustomerActiveOrders::class.java)
            startActivity(intent)
        }

        pastButton = findViewById(R.id.pastOrders)
        pastButton.setOnClickListener(){
            val intent = Intent(this, CustomerPastOrders::class.java)
            startActivity(intent)
        }
    }
}