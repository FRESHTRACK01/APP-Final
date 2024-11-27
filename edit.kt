package com.example.fresh_track

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class edit : AppCompatActivity() {
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null
    private lateinit var dbHelper: DatabaseHelper2
    private lateinit var imageView: ImageView
    private lateinit var nombreProductoEditText: EditText
    private lateinit var tipoEditText: EditText
    private lateinit var cantidadEditText: EditText
    private lateinit var fechaEntradaEditText: EditText
    private lateinit var fechaSalidaEditText: EditText
    private lateinit var precioEditText: EditText
    private lateinit var saveButton: Button
    private var existingImagePath: String? = null // Nueva variable para almacenar la ruta de la imagen existente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)

        dbHelper = DatabaseHelper2(this)

        // Inicializar los campos de entrada
        imageView = findViewById(R.id.camara3) // Asegúrate de que este ID esté en el XML
        nombreProductoEditText = findViewById(R.id.item_quantity4)
        tipoEditText = findViewById(R.id.item_quantity5)
        cantidadEditText = findViewById(R.id.item_quantity6)
        fechaEntradaEditText = findViewById(R.id.item_feE2)
        fechaSalidaEditText = findViewById(R.id.item_feS2)
        precioEditText = findViewById(R.id.item_Prec2)
        saveButton = findViewById(R.id.guardar_)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                imageView.setImageURI(it)
            }
        }

        imageView.setOnClickListener {
            openGallery()
        }

        // Obtener el ID del producto a editar
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        loadProductData(productId)

        // Configurar el botón de guardar
        saveButton.setOnClickListener {
            updateProduct(productId)
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun loadProductData(productId: Int) {
        val cursor = dbHelper.getProductById(productId)
        if (cursor != null && cursor.moveToFirst()) {
            nombreProductoEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto")))
            tipoEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("tipo")))
            cantidadEditText.setText(cursor.getInt(cursor.getColumnIndexOrThrow("cantidad")).toString())
            fechaEntradaEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_entrada")))
            fechaSalidaEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_salida")))
            precioEditText.setText(cursor.getDouble(cursor.getColumnIndexOrThrow("precio")).toString())
            existingImagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagen_path")) // Guardar la ruta de la imagen existente
            // Mostrar la imagen existente si está disponible
            if (!existingImagePath.isNullOrEmpty()) {
                imageView.setImageURI(Uri.parse(existingImagePath))
            }
        } else {
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad si no se encuentra el producto
        }
        cursor?.close() // Cerrar el cursor para liberar recursos
    }

    private fun updateProduct(productId: Int) {
        val nombre = nombreProductoEditText.text.toString()
        val tipo = tipoEditText.text.toString()
        val cantidad = cantidadEditText.text.toString().toIntOrNull() ?: 0
        val fechaEntrada = fechaEntradaEditText.text.toString()
        val fechaSalida = fechaSalidaEditText.text.toString()
        val precio = precioEditText.text.toString().toDoubleOrNull() ?: 0.0
        val imagenPath = selectedImageUri?.toString() ?: existingImagePath // Usar la nueva URI si existe, o la existente

        // Actualizar el producto en la base de datos
        val success = dbHelper.updateProducto(productId, nombre, tipo, cantidad, fechaEntrada, fechaSalida, precio, imagenPath)
        if (success > 0) { // Comprobar si se actualizó al menos un registro
            Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad después de actualizar
        } else {
            Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
        }
    }
}