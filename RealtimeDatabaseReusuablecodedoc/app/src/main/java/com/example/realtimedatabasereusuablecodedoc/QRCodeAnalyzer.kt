/*
package com.example.realtimedatabasereusuablecodedoc
import android.graphics.ImageFormat
import androidx.camera.core.ImageProxy
import androidx.camera.core.ImageAnalysis
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

/*
This is a component part of the QRCode Scanner that we failed to successfully integrate (it didn't even compile)
 This is for the merchant to scan the customer's screen of their order's qrcode with
 */

class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit
): ImageAnalysis.Analyzer {
    /*
        We specify the QRCode image formats we are able to scan and analyze
    */
    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    override fun analyze(image: ImageProxy){
        if(image.format in supportedImageFormats){
        //only if the image is a QRCode do we even bother analyzing
            val bytes = image.planes.first().buffer.toByteArray()


            //This is the parameters of the QR code that we scanned
            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )


            //we assist the binarybitmap reader to analyze the qr code
            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(
                            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                                BarcodeFormat.QR_CODE
                            )
                        )
                    )
                }.decode(binaryBmp)
                onQRCodeScanned(result.text)
                //we decode the qrcode into the resulting string (should be the order id)
            } catch(e: Exception) {
                e.printStackTrace()
            } finally {
                image.close()
            }
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {

        //Processes the image row by row putting each into a ByteArray
        rewind()
        return ByteArray(remaining()).also{
            get(it)
        }
    }
}
*/