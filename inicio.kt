package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class inicio : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)

        dbHelper = DatabaseHelper2(this)

        val men = findViewById<ImageButton>(R.id.icon_two)
        men.setOnClickListener {
            // Crear un Intent para iniciar menu
            val intent = Intent(this, menu::class.java)
            startActivity(intent)
        }

        val per = findViewById<ImageButton>(R.id.icon_one)
        per.setOnClickListener {
            // Crear un Intent para iniciar perfil
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
        }

        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            // Crear un Intent para iniciar bienvenida
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
        }

        val bust = findViewById<ImageButton>(R.id.icon_tres)
        bust.setOnClickListener {
            // Crear un Intent para iniciar busqueda
            val intent = Intent(this, busqueda::class.java)
            startActivity(intent)
        }

        // Cargar productos próximos a vencer
        loadExpiringProducts()
    }

    private fun loadExpiringProducts() {
        val expiringProducts = dbHelper.getExpiringProducts(7) // Filtrar productos que vencen en 7 días

        if (expiringProducts.isNotEmpty()) {
            setupRecyclerView(expiringProducts)
        } else {
            Toast.makeText(this, "No hay productos próximos a vencer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(productList: List<Producto>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_inventario) // Asegúrate de tener esta vista en tu layout
        val adapter = InventarioAdapter(productList, dbHelper, { producto ->
            // Acción para modificar
            val intent = Intent(this, edit::class.java).apply {
                putExtra("PRODUCT_ID", producto.id)
            }
            startActivity(intent)
        }, { producto ->
            // Acción para eliminar
            dbHelper.deleteProducto(producto.id) // Método para eliminar de la base de datos
            loadExpiringProducts() // Recargar datos para reflejar cambios
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}