//package com.example.realtimedatabasereusuablecodedoc
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import com.paypal.checkout.PayPalCheckout
//import com.paypal.checkout.approve.OnApprove
//import com.paypal.checkout.config.CheckoutConfig
//import com.paypal.checkout.config.Environment
//import com.paypal.checkout.config.SettingsConfig
//import com.paypal.checkout.createorder.CreateOrder
//import com.paypal.checkout.createorder.CurrencyCode
//import com.paypal.checkout.createorder.OrderIntent
//import com.paypal.checkout.createorder.UserAction
//import com.paypal.checkout.order.Amount
//import com.paypal.checkout.order.AppContext
//import com.paypal.checkout.order.Order
//import com.paypal.checkout.order.PurchaseUnit
//import com.paypal.checkout.paymentbutton.PaymentButtonContainer
//import kotlin.collections.List
//import kotlin.collections.ArrayList
//import kotlin.properties.Delegates
//
//class CheckoutActivity : AppCompatActivity() {
//    private lateinit var cartRecyclerView: RecyclerView
//    private lateinit var couponRecyclerView: RecyclerView
//    private lateinit var orderItemList: ArrayList<OrderItemDC>
//    private lateinit var couponList: MutableList<ListItem>
//    private lateinit var adapter: CartItemAdapter
//    private lateinit var couponAdapter: CouponAdapter
//    private lateinit var cart: ArrayList<PurchaseUnit>
//    private lateinit var itemNames: ArrayList<String>
//    private lateinit var prices: ArrayList<String>
//    private lateinit var subtotalDisplay: TextView
//    private lateinit var taxDisplay: TextView
//    private lateinit var discountDisplay: TextView
//    private lateinit var grandTotalDisplay: TextView
//    private lateinit var paymentButtonContainer: PaymentButtonContainer
//    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_checkout)
//
//        itemNames = intent.getStringArrayListExtra("items") as ArrayList<String>
//        prices = intent.getStringArrayListExtra("prices") as ArrayList<String>
//
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        cartRecyclerView = findViewById(R.id.cartRecyclerView)
//        subtotalDisplay = findViewById(R.id.subtotal)
//        taxDisplay = findViewById(R.id.tax)
//        orderItemList = ArrayList()
//        cart = ArrayList()
//        adapter = CartItemAdapter(this, orderItemList)
//        cartRecyclerView.layoutManager = LinearLayoutManager(this)
//        cartRecyclerView.adapter = adapter
//        var subtotal: Float = 0.0F
//        orderItemList.clear()
//        for (i in 0..itemNames.size) {
//            val currentItem: PurchaseUnit = PurchaseUnit(
//                amount = Amount(currencyCode = CurrencyCode.USD, value = prices.elementAt(i))
//            )
//            cart.add(currentItem)
//            val newItem: OrderItemDC = OrderItemDC(itemNames.elementAt(i).toString(), prices.elementAt(i).toString())
//            subtotal += prices.elementAt(i).toFloat()
//            orderItemList.add(newItem)
//        }
//
//        adapter.notifyDataSetChanged()
//        subtotalDisplay.setText(subtotal.toString())
//        var tax: Float = subtotal * 0.1F
//        taxDisplay.setText(tax.toString())
//
//        couponList = mutableListOf<ListItem>()
//
//        couponAdapter = CouponAdapter(couponList)
//        couponRecyclerView.layoutManager = LinearLayoutManager(this)
//        couponRecyclerView.adapter = couponAdapter
//
//        database.child("GeneralCoupons").addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(coupon in couponList) {
//                    var currentCoupon: Any = coupon
//                    if (currentCoupon is GeneralCoupon) {
//                        couponList.remove(coupon)
//                    }
//                }
//                //userList.clear()
//                for(postSnapshot in snapshot.children) {
//                    val currentCoupon = postSnapshot.getValue(GeneralCoupon::class.java)
//                    if(subtotal >= currentCoupon?.quantityNeeded) {
//                        couponList.add(currentCoupon!! as ListItem)
//                    }
//                }
//                couponAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//
//        database.child("SpecificCoupon").addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(coupon in couponList) {
//                    var currentCoupon: Any = coupon
//                    if (currentCoupon is SpecificCoupon) {
//                        couponList.remove(coupon)
//                    }
//                }
//                //userList.clear()
//                for(postSnapshot in snapshot.children) {
//                    val currentCoupon = postSnapshot.getValue(SpecificCoupon::class.java)
//                    val requiredItem: String = currentCoupon?.couponFor
//                    val predicate: (String) -> Boolean = {it == requiredItem}
//                    if(itemNames.count(predicate) >= currentCoupon?.quantityNeeded) {
//                        couponList.add(currentCoupon!! as ListItem)
//                    }
//                }
//                couponAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//
//        var discount: Float = 0.0F
//        couponAdapter.onItemClick = {
//            if (it is GeneralCoupon) {
//                val selectedCoupon: GeneralCoupon = it
//                discount = selectedCoupon.discountedPrice
//            }
//            else if (it is SpecificCoupon) {
//                val selectedCoupon: SpecificCoupon = it
//                discount = selectedCoupon.discountedPrice
//            }
//        }
//
//        discountDisplay.setText(discount.toString())
//
//        var grandTotal = subtotal + tax - discount
//        grandTotalDisplay.setText(grandTotal.toString())
//
//        val config = CheckoutConfig(
//            application,
//            clientId = "AbBw9JwhPcD0-5wZRCi_LpmDiHyGXuYK_FnfNZfVkQCuRk_PdscpI4VvgWz-D39JJV4re4E0V9rIYEP_",
//            environment = Environment.SANDBOX,
//            returnUrl = "com.example.realtimedatabasereusuablecodedoc://paypalpay",
//            currencyCode = CurrencyCode.USD,
//            userAction = UserAction.PAY_NOW,
//            settingsConfig = SettingsConfig(
//                loggingEnabled = true
//            )
//        )
//        PayPalCheckout.setConfig(config)
//
//        paymentButtonContainer.setup(
//            createOrder =
//            CreateOrder { createOrderActions ->
//                val order =
//                    Order(
//                        intent = OrderIntent.CAPTURE,
//                        appContext = AppContext(userAction = UserAction.PAY_NOW),
//                        purchaseUnitList =
//                        listOf(
//                            PurchaseUnit(
//                                amount =
//                                Amount(currencyCode = CurrencyCode.USD, value = grandTotal.toString())
//                            )
//                        )
//                    )
//                createOrderActions.create(order)
//            },
//            onApprove =
//            OnApprove { approval ->
//                approval.orderActions.capture { captureOrderResult ->
//                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
//                }
//            }
//        )
//    }
//}
