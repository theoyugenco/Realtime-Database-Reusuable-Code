package com.example.realtimedatabasereusuablecodedoc

import android.content.Intent
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
import android.widget.Button
import android.widget.TextView
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
import kotlin.math.roundToInt


class CustomerCheckout : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var restaurantMenu: Menu? = null
    private var itemNameArrayList : ArrayList<String> = ArrayList<String>()
    private var itemPriceArrayList : ArrayList<String> = ArrayList<String>()
    private lateinit var couponList: MutableList<ListItem>
    //private lateinit var itemNameSet: Set<String>
    //private lateinit var itemPriceSet: Set<String>
    private var itemNameDistinctArrayList : ArrayList<String> = ArrayList<String>()
    private var itemPriceDistinctArrayList : ArrayList<String> = ArrayList<String>()
    private var iNAL : ArrayList<String> = ArrayList<String>()
    private var itemCountArrayList : ArrayList<Int> = ArrayList<Int>()
    private lateinit var recyclerv: RecyclerView
    private lateinit var couponRecyclerView: RecyclerView
    private lateinit var msAdapterMenuItems: CustomerCheckoutAdapter
    private lateinit var couponAdapter: CouponAdapter
//    private lateinit var paymentButtonContainer: PaymentButtonContainer
    private var TAG: String? = null
    private lateinit var subtotalPrice: TextView
    private lateinit var taxPrice: TextView
    private lateinit var discountPrice: TextView
    private lateinit var totalPrice: TextView
    private lateinit var checkoutButton: Button

    //Test
    private lateinit var receiptButton: Button


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


        //Test
        receiptButton = findViewById(R.id.cc_receipt)
        receiptButton.setOnClickListener {
            val intent = Intent(this, CustomerReceipt::class.java)
            startActivity(intent)
        }

        /*
        Kenneth Valero was responsible for setting the calculations for subtotal, tax,
        grand total, and coupon discount
         */
        /*
        Totalling up the prices of items for subtotal
         */
        subtotalPrice = findViewById(R.id.cc_subtotal_price)
        var subtotal = 0.00
        for (i in itemPriceArrayList) {
            subtotal += i.toDouble()
        }
        val roundedsubtotal = (subtotal * 100.0).roundToInt()/100.0

        subtotalPrice.setText(roundedsubtotal.toString())

        /*
        Kenneth Valero
        Calculating the 10% tax
         */
        taxPrice = findViewById(R.id.cc_fee_price)
        val tax = ((roundedsubtotal * 0.10) * 100.0).roundToInt()/100.0
        taxPrice.setText(tax.toString())

        /*
        Kenneth Valero
        Initializing the coupon recyclerview
         */
        couponRecyclerView = findViewById(R.id.cc_couponRecycler)

        couponList = mutableListOf<ListItem>()

        couponAdapter = CouponAdapter(couponList)
        couponRecyclerView.layoutManager = LinearLayoutManager(this)
        couponRecyclerView.adapter = couponAdapter

        /*
        Kenneth Valero
        Setting up test coupons
         */
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance().getReference()

        val generalCoupon = GeneralCoupon(10.0, 20.0, 3, 2, "04-05-2023")
        val specificCoupon = SpecificCoupon("m8mi2", 3242.0, 2, 3, 2, "04-05-2023")

        val generalcouponID = database.child("GeneralCoupons").push().key
        database.child("GeneralCoupons/" + generalcouponID).setValue(generalCoupon)
        val specificcouponID = database.child("SpecificCoupons").push().key
        database.child("SpecificCoupons/" + specificcouponID).setValue(specificCoupon)
        /*
        Kenneth Valero
        Checking the current coupons in the database to see
        which coupons are eligible for the current order
         */
        database.child("GeneralCoupons").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                for(coupon in couponList) {
//                    var currentCoupon: Any = coupon
//                    if (currentCoupon is GeneralCoupon) {
//                        couponList.remove(coupon)
//                    }
//                }
                //userList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentCoupon: GeneralCoupon? = postSnapshot.getValue(GeneralCoupon::class.java)
                    if (currentCoupon != null) {
                        if(subtotal >= currentCoupon.quantityNeeded!!) {
                            couponList.add(currentCoupon!! as ListItem)
                        }
                    }
                }
                couponAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        database.child("SpecificCoupon").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                for(coupon in couponList) {
//                    var currentCoupon: Any = coupon
//                    if (currentCoupon is SpecificCoupon) {
//                        couponList.remove(coupon)
//                    }
//                }
                //userList.clear()
                for(postSnapshot in snapshot.children) {
                    val currentCoupon: SpecificCoupon? = postSnapshot.getValue(SpecificCoupon::class.java)
                    val requiredItem: String? = currentCoupon?.couponFor
                    val predicate: (String) -> Boolean = {it == requiredItem}
                    if(itemNameArrayList.count(predicate) >= currentCoupon?.quantityNeeded!!) {
                        couponList.add(currentCoupon!! as ListItem)
                    }
                }
                couponAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        /*
        Kenneth Valero
        Calculating the discount price from the user's chosen coupon if applicable.
         */
        var discount: Double = 0.0
        /*
        Kenneth Valero
        Sets the discount to the chosen coupon's assigned discount
         */
        couponAdapter.onItemClick = {
            if (it is GeneralCoupon) {
                val selectedCoupon: GeneralCoupon = it
                discount = selectedCoupon.discountedPrice!!
            }
            else if (it is SpecificCoupon) {
                val selectedCoupon: SpecificCoupon = it
                discount = selectedCoupon.discountedPrice!!
            }
        }

        discountPrice = findViewById(R.id.cc_coupon_number)
        discountPrice.setText("-" + discount.toString())

        /*
        Kenneth Valero
        Calculating the grand total of the order
         */
        totalPrice = findViewById(R.id.cc_total_price)
        var grandTotal = subtotal + tax - discount
        totalPrice.setText("$" + grandTotal.toString())

        checkoutButton = findViewById((R.id.cc_checkout))
        checkoutButton.setOnClickListener {
            val intent = Intent(this, OrderPayment::class.java)
            intent.putExtra("Total", grandTotal)
            startActivity(intent)
        }

        /*
        Kenneth Valero
        Setting the configuration for the paypal button to be linked to PayPal.
         */
//        paymentButtonContainer = findViewById(R.id.payPalButton)
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

        /*
        Kenneth Valero
        Configuring the paypal button to setup an order that costs the total amount
        of the order.
         */
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