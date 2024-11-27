package com.example.fresh_track

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class registro_inventario : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null

    // Elementos de entrada
    private lateinit var nombreProductoEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var cantidadEditText: EditText
    private lateinit var fechaEntradaEditText: EditText
    private lateinit var fechaSalidaEditText: EditText
    private lateinit var precioEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_inventario)

        imageView = findViewById(R.id.camara)

        nombreProductoEditText = findViewById(R.id.item_quantity2)
        tipoEditText = findViewById(R.id.item_quantity3)
        cantidadEditText = findViewById(R.id.item_quantity)
        fechaEntradaEditText = findViewById(R.id.item_feE)
        fechaSalidaEditText = findViewById(R.id.item_feS)
        precioEditText = findViewById(R.id.item_Prec)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                imageView.setImageURI(it)
            }
        }

        imageView.setOnClickListener {
            openGallery()
        }

        findViewById<ImageButton>(R.id.icon_two).setOnClickListener {
            startActivity(Intent(this, menu::class.java))
        }

        findViewById<ImageButton>(R.id.icon_one).setOnClickListener {
            startActivity(Intent(this, perfil::class.java))
        }

        findViewById<Button>(R.id.guardar_2).setOnClickListener {
            saveInventory()
        }

        findViewById<Button>(R.id.title).setOnClickListener {
            startActivity(Intent(this, bienvenida::class.java))
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun saveInventory() {
        val nombreProducto = nombreProductoEditText.text.toString().trim()
        val tipo = tipoEditText.text.toString().trim()
        val cantidad = cantidadEditText.text.toString().toIntOrNull() ?: 0
        val fechaEntrada = fechaEntradaEditText.text.toString().trim()
        val fechaSalida = fechaSalidaEditText.text.toString().trim()
        val precio = precioEditText.text.toString().toDoubleOrNull() ?: 0.0
        val codigo = (100000000000..999999999999).random().toString()

        // Validar campos
        if (nombreProducto.isEmpty() || tipo.isEmpty() || cantidad <= 0 ||
            fechaEntrada.isEmpty() || fechaSalida.isEmpty() || precio <= 0.0 ||
            selectedImageUri == null) { // Verificación de la imagen
            Toast.makeText(this, "Por favor completa todos los campos correctamente y selecciona una imagen.", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper2(this)
        val resultado = dbHelper.insertInventario(
            nombreProducto,
            tipo,
            cantidad,
            fechaEntrada,
            fechaSalida,
            precio,
            codigo,
            selectedImageUri?.toString()
        )

        val notificationHelper = NotificationHelper(this)
        notificationHelper.sendInventoryRegistrationNotification()

        if (resultado != -1L) {
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show()
            selectedImageUri = null
            imageView.setImageResource(0)

            Intent(this, codigodebarras::class.java).apply {
                putExtra("CODIGO", codigo)
                startActivity(this)
            }
        } else {
            Toast.makeText(this, "Error al guardar el registro", Toast.LENGTH_SHORT).show()
        }
    }
}