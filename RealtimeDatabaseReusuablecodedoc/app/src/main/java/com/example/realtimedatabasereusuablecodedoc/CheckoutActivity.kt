package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import kotlin.collections.List
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class CheckoutActivity : AppCompatActivity() {
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var genCouponRecyclerView: RecyclerView
    private lateinit var itemCouponRecyclerView: RecyclerView
    private lateinit var orderItemList: ArrayList<OrderItemDC>
    private lateinit var couponList: List<Any>
    private lateinit var adapter: CartItemAdapter
    private lateinit var generalAdapter: CouponAdapter
    private lateinit var cart: ArrayList<PurchaseUnit>
    private lateinit var itemNames: ArrayList<String>
    private lateinit var prices: ArrayList<String>
    private lateinit var subtotalDisplay: TextView
    private lateinit var taxDisplay: TextView
    private lateinit var paymentButtonContainer: PaymentButtonContainer
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        itemNames = intent.getStringArrayListExtra("items") as ArrayList<String>
        prices = intent.getStringArrayListExtra("prices") as ArrayList<String>

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        subtotalDisplay = findViewById(R.id.subtotal)
        taxDisplay = findViewById(R.id.tax)
        orderItemList = ArrayList()
        cart = ArrayList()
        adapter = CartItemAdapter(this, orderItemList)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = adapter
        var subtotal: Float = 0.0F
        orderItemList.clear()
        for (i in 0..itemNames.size) {
            val currentItem: PurchaseUnit = PurchaseUnit(
                amount = Amount(currencyCode = CurrencyCode.USD, value = prices.elementAt(i))
            )
            cart.add(currentItem)
            val newItem: OrderItemDC = OrderItemDC(itemNames.elementAt(i).toString(), prices.elementAt(i).toString())
            subtotal += prices.elementAt(i).toFloat()
            orderItemList.add(newItem)
        }

        adapter.notifyDataSetChanged()
        subtotalDisplay.setText(subtotal.toString())
        var tax: Float = subtotal * 0.1F
        taxDisplay.setText(tax.toString())

        couponList = mutableListOf<Any>()
        
        val config = CheckoutConfig(
            application,
            clientId = "AbBw9JwhPcD0-5wZRCi_LpmDiHyGXuYK_FnfNZfVkQCuRk_PdscpI4VvgWz-D39JJV4re4E0V9rIYEP_",
            environment = Environment.SANDBOX,
            returnUrl = "com.example.realtimedatabasereusuablecodedoc://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)

        paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList = cart
//                        listOf(
//                            PurchaseUnit(
//                                amount =
//                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
//                            )
//                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                }
            }
        )
    }
}