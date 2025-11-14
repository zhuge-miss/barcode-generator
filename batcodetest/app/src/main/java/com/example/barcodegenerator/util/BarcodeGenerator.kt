package com.example.barcodegenerator.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object BarcodeGenerator {

    @JvmStatic
    fun encodeToBitmap(
        content: String,
        format: BarcodeFormat,
        width: Int,
        height: Int
    ): Bitmap {
        val hints = mutableMapOf<EncodeHintType, Any>(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.MARGIN to 1
        )

        if (format == BarcodeFormat.QR_CODE) {
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
        }

        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            format,
            width,
            height,
            hints
        )
        return bitMatrix.toBitmap()
    }

    private fun BitMatrix.toBitmap(): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(width * height)
        val black = 0xFF000000.toInt()
        val white = 0xFFFFFFFF.toInt()
        var offset = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[offset + x] = if (get(x, y)) black else white
            }
            offset += width
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height)
        return bmp
    }
}