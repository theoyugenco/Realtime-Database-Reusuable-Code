/*package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlin.collections.List
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {
    private lateinit var cart: List<PurchaseUnit>
    private lateinit var itemNames: ArrayList<String>
    private lateinit var prices: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        itemNames = intent.getStringArrayListExtra("items")
        prices = intent.getStringArrayListExtra("prices")

        cart = listOf()

        for (i in 0..itemNames.size) {
            val currentItem: PurchaseUnit = PurchaseUnit(
                amount = prices.elementAt(i)
            )
            cart.add(currentItem)
        }
        
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
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
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
}*/