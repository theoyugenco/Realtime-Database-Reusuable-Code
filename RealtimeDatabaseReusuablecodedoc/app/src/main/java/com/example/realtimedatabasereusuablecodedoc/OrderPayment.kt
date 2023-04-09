package com.example.realtimedatabasereusuablecodedoc

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
import com.paypal.checkout.paymentbutton.PaymentButtonContainer

class OrderPayment : AppCompatActivity() {
    private lateinit var paymentButtonContainer: PaymentButtonContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_payment)
        paymentButtonContainer = findViewById(R.id.payPalButton)
        val total = intent.getDoubleExtra("Total", 0.0)
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
                                Amount(currencyCode = CurrencyCode.USD, value = total.toString())
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
}