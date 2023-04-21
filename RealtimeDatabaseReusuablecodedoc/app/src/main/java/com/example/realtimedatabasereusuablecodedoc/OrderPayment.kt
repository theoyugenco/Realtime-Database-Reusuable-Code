package com.example.realtimedatabasereusuablecodedoc

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
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
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

class OrderPayment : AppCompatActivity() {
    private lateinit var paymentButtonContainer: PaymentButtonContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_payment)


        paymentButtonContainer = findViewById(R.id.payPalButton)
        val total = intent.getDoubleExtra("Total", 0.0)
//        totalAmount = findViewById(R.id.total_cost)
//        totalAmount.setText("Total: $" + total.toString())
//        payButton = findViewById(R.id.paymentButton)
//
//        payButton.setOnClickListener {
//            setPayment(total.toString())
//        }


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
//    private fun setPayment(total: String) {
//        var payment: PayPalPayment = PayPalPayment(BigDecimal(total), "USD", "MealStealOrder", PayPalPayment.PAYMENT_INTENT_SALE)
//        intent = Intent(this, PaymentActivity::class.java)
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfiguration)
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
//
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PAYPAL_REQUEST_CODE) {
//            var config: PaymentConfirmation? = data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
//
//            if (config != null) {
//                try {
//                    var details: String = config.toJSONObject().toString()
//
//                    var paymentObject: JSONObject = JSONObject(details)
//                } catch (exception: JSONException) {
//                    exception.printStackTrace()
//
//                    Log.e("Error: ", "Something went wrong")
//                }
//            }
//            else if (requestCode == Activity.RESULT_CANCELED) {
//                Log.e("Error: ", "Action cancelled")
//            }
//            else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.e("Error: ", "Payment rendered invalid")
//            }
//        }
//    }
}