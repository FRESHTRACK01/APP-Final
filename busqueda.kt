package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class busqueda : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nombreProductoEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var cantidadEditText: EditText
    private lateinit var fechaEntradaEditText: EditText
    private lateinit var fechaSalidaEditText: EditText
    private lateinit var precioEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_busqueda)

        // Inicializar los campos
        imageView = findViewById(R.id.icon_escan)
        nombreProductoEditText = findViewById(R.id.item_quantity2)
        tipoEditText = findViewById(R.id.item_quantity3)
        cantidadEditText = findViewById(R.id.item_quantity)
        fechaEntradaEditText = findViewById(R.id.item_feE)
        fechaSalidaEditText = findViewById(R.id.item_feS)
        precioEditText = findViewById(R.id.item_Prec)

        val men = findViewById<ImageButton>(R.id.icon_two)
        men.setOnClickListener {
            val intent = Intent(this, inventario::class.java)
            startActivity(intent)
        }

        val btn1 = findViewById<Button>(R.id.btn_guar)
        btn1.setOnClickListener {
            // Mostrar un mensaje de registro guardado
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()

            btn1.postDelayed({
                val intent = Intent(this, inventario::class.java)
                startActivity(intent)
            }, 1000)
        }

        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            // Crear un Intent para iniciar bienvenida
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }

        val scanButton = findViewById<ImageView>(R.id.icon_escan)
        scanButton.setOnClickListener {
            initiateScan()
        }
    }

    private fun initiateScan() {
        val integrator = IntentIntegrator(this)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_LONG).show()
            } else {
                // Obtener el código escaneado
                val scannedCode = result.contents

                // Llenar los campos con los detalles del producto escaneado
                fillFieldsWithScannedData(scannedCode)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun fillFieldsWithScannedData(codigo: String) {
        val dbHelper = DatabaseHelper2(this)
        val productDetails = dbHelper.getProductDetailsByCode(codigo) // Implementa este método

        productDetails?.let {
            // Llenar los campos con los datos del producto
            nombreProductoEditText.setText(it.nombreProducto)
            tipoEditText.setText(it.tipo)
            cantidadEditText.setText(it.cantidad.toString())
            fechaEntradaEditText.setText(it.fechaEntrada)
            fechaSalidaEditText.setText(it.fechaSalida)
            precioEditText.setText(it.precio.toString())
            // Si tienes una URI para la imagen, la puedes cargar aquí
            // imageView.setImageURI(it.imageUri)
        } ?: run {
            Toast.makeText(this, "No se encontró el producto.", Toast.LENGTH_SHORT).show()
        }
    }
}