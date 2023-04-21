package com.example.realtimedatabasereusuablecodedoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class OrderReceiptDetails : AppCompatActivity() {

    lateinit var qrCode : ImageView
    lateinit var word : EditText
    lateinit var generateCode : Button
    lateinit var orderID : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_receipt_details)


        qrCode = findViewById(R.id.qrCode)


        //val data = word.text.toString().trim()

        /*
        perhaps have a check to see if the order is active
         */

        val bundle = intent.extras
        if (bundle != null){
            orderID = bundle.getString("orderID")!!
            //itemNameArrayList = bundle.getStringArrayList("itemName")!!
            //itemPriceArrayList = bundle.getStringArrayList("itemPrice")!!
        }

        val data = orderID
        if (data.isEmpty()){
            Toast.makeText(this, "CRITICAL ERROR", Toast.LENGTH_SHORT).show()
        }else{
            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height

                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for(x in 0 until width){
                    for (y in 0 until height){
                        bmp.setPixel(x, y, if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                qrCode.setImageBitmap(bmp)
            }catch (e: WriterException){
                e.printStackTrace()
            }
        }
    }
}