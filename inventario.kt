package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class inventario : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inventoryAdapter: InventarioAdapter
    private lateinit var dbHelper: DatabaseHelper2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventario)

        dbHelper = DatabaseHelper2(this)

        // Inicializar el RecyclerView
        recyclerView = findViewById(R.id.recycler_view_inventario)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cargar datos del inventario
        loadInventoryData()

        // Configurar botones
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        // Recargar datos cada vez que la actividad se vuelve visible
        loadInventoryData()
    }

    private fun setupButtons() {
        val men = findViewById<ImageButton>(R.id.icon_two)
        men.setOnClickListener {
            startActivity(Intent(this, menu::class.java))
        }

        val per = findViewById<ImageButton>(R.id.icon_one)
        per.setOnClickListener {
            startActivity(Intent(this, perfil::class.java))
        }

        val lupita = findViewById<ImageButton>(R.id.lupa)
        lupita.setOnClickListener {
            startActivity(Intent(this, busqueda::class.java))
        }

        val agregar = findViewById<ImageButton>(R.id.agregar)
        agregar.setOnClickListener {
            startActivity(Intent(this, registro_inventario::class.java))
        }

        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            startActivity(Intent(this, bienvenida::class.java))
        }
        // Ejemplo de uso en una actividad
        val dbHelper = DatabaseHelper2(this)
        val notificationHelper = NotificationHelper(this)

// Verificar productos que expiran en 3 días
        notificationHelper.checkForExpiringProducts(dbHelper, 3)
    }

    private fun loadInventoryData() {
        val cursor = dbHelper.getAllInventario()
        Log.d("Inventario", "Cursor obtenido: $cursor")

        val inventoryList = mutableListOf<Producto>()
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val nombreProducto = it.getString(it.getColumnIndexOrThrow("nombre_producto"))
                    val tipo = it.getString(it.getColumnIndexOrThrow("tipo"))
                    val cantidad = it.getInt(it.getColumnIndexOrThrow("cantidad"))
                    val fechaEntrada = it.getString(it.getColumnIndexOrThrow("fecha_entrada"))
                    val fechaSalida = it.getString(it.getColumnIndexOrThrow("fecha_salida"))
                    val precio = it.getDouble(it.getColumnIndexOrThrow("precio"))
                    val codigo = it.getString(it.getColumnIndexOrThrow("codigo"))
                    val imagenPath = it.getString(it.getColumnIndexOrThrow("imagen_path"))

                    val producto = Producto(id, nombreProducto, tipo, cantidad, fechaEntrada, fechaSalida, precio, codigo, imagenPath)
                    inventoryList.add(producto)
                    Log.d("Inventario", "Producto añadido: $nombreProducto")
                } while (it.moveToNext())
            }
        }

        if (inventoryList.isNotEmpty()) {
            inventoryAdapter = InventarioAdapter(inventoryList, dbHelper, { producto ->
                // Manejar la modificación
                val intent = Intent(this, edit::class.java).apply {
                    putExtra("PRODUCT_ID", producto.id)
                }
                startActivity(intent)
            }, { producto ->
                // Manejar la eliminación
                dbHelper.deleteProducto(producto.id) // Método para eliminar de la base de datos
                loadInventoryData() // Recargar datos para reflejar cambios
            })
            recyclerView.adapter = inventoryAdapter
            Log.d("Inventario", "Datos cargados en el RecyclerView")
        } else {
            Toast.makeText(this, "No hay registros en el inventario", Toast.LENGTH_SHORT).show()
            Log.d("Inventario", "No hay datos en la base de datos")
        }
    }
}
