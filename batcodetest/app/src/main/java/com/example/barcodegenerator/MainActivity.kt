package com.example.barcodegenerator

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.barcodegenerator.util.BarcodeGenerator
import com.google.zxing.BarcodeFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.inputContent)
        val spinner = findViewById<Spinner>(R.id.formatSpinner)
        val generateBtn = findViewById<Button>(R.id.generateButton)
        val imageView = findViewById<ImageView>(R.id.resultImage)

        val items = listOf(
            FormatItem("二维码 (QR Code)", BarcodeFormat.QR_CODE, is2D = true),
            FormatItem("Code 128", BarcodeFormat.CODE_128),
            FormatItem("EAN-13", BarcodeFormat.EAN_13),
            FormatItem("Code 39", BarcodeFormat.CODE_39),
            FormatItem("ITF-14", BarcodeFormat.ITF),
            FormatItem("PDF417", BarcodeFormat.PDF_417, is2D = true),
            FormatItem("Data Matrix", BarcodeFormat.DATA_MATRIX, is2D = true),
            FormatItem("Aztec", BarcodeFormat.AZTEC, is2D = true),
            FormatItem("UPC-A", BarcodeFormat.UPC_A)
        )

        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            items.map { it.display }
        )

        generateBtn.setOnClickListener {
            val text = input.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val index = spinner.selectedItemPosition.coerceIn(0, items.lastIndex)
            val selected = items[index]

            try {
                val size = if (selected.is2D) 1024 else 300
                val width = if (selected.is2D) 1024 else 1024
                val height = if (selected.is2D) 1024 else size

                val bitmap: Bitmap = BarcodeGenerator.encodeToBitmap(
                    content = text,
                    format = selected.format,
                    width = width,
                    height = height
                )
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Toast.makeText(this, "生成失败: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

data class FormatItem(
    val display: String,
    val format: BarcodeFormat,
    val is2D: Boolean = false
)