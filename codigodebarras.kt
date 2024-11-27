package com.example.fresh_track

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class codigodebarras : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigodebarras)

        val codigo = intent.getStringExtra("CODIGO") ?: run {
            Toast.makeText(this, "No se proporcionó ningún código", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        mostrarCodigoDeBarras(codigo)
      findViewById<ImageButton>(R.id.inventari).setOnClickListener {
          startActivity(Intent(this, inventario::class.java))

        }
        findViewById<ImageButton>(R.id.icon_two).setOnClickListener {
            startActivity(Intent(this, registro_inventario::class.java))
        }

        findViewById<ImageButton>(R.id.icon_one).setOnClickListener {
            startActivity(Intent(this, perfil::class.java))
        }

        findViewById<Button>(R.id.title).setOnClickListener {
            startActivity(Intent(this, bienvenida::class.java))
        }

        findViewById<Button>(R.id.inicio_button3).setOnClickListener {
            val notificationHelper = NotificationHelper(this)
            notificationHelper.sendBarcodeDownloadNotification()

            descargarImagenCodigoBarrasEnPDF(codigo)
        }
    }

    private fun mostrarCodigoDeBarras(codigo: String) {
        val imageView = findViewById<ImageView>(R.id.cam)
        imageView.post {
            val width = imageView.width
            val height = imageView.height

            try {
                val bitMatrix = MultiFormatWriter().encode(codigo, BarcodeFormat.CODE_128, width, height)
                val barcodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        barcodeBitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                    }
                }

                imageView.setImageBitmap(barcodeBitmap)
            } catch (e: WriterException) {
                Log.e("BarcodeError", "Error al generar el código de barras", e)
                Toast.makeText(this, "Error al generar el código de barras", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun descargarImagenCodigoBarrasEnPDF(codigo: String) {
        val imageView = findViewById<ImageView>(R.id.cam)
        imageView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(imageView.drawingCache)
        imageView.isDrawingCacheEnabled = false

        // Ruta para guardar el archivo en la carpeta Descargas
        val pdfPath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "codigo_barras.pdf")

        try {
            // Crear el escritor y el documento PDF
            val pdfWriter = PdfWriter(FileOutputStream(pdfPath))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            // Convertir el Bitmap a una imagen compatible con iText
            val stream = java.io.ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imgData = com.itextpdf.io.image.ImageDataFactory.create(stream.toByteArray())
            val img = Image(imgData)

            // Agregar la imagen al documento
            document.add(img)

            // Agregar un párrafo con el código
            document.add(Paragraph("Código: $codigo").setFontColor(ColorConstants.BLACK))

            // Cerrar el documento
            document.close()

            Toast.makeText(this, "PDF guardado en Descargas: ${pdfPath.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("PDFError", "Error al generar el PDF", e)
            Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show()
        }
    }

}
