package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)


        val ol = findViewById<Button>(R.id.btn_home)
        ol.setOnClickListener {
            val intent = Intent(this, inicio::class.java)
            startActivity(intent)
        }
        val ini = findViewById<Button>(R.id.btn_register)
        ini.setOnClickListener {
            val intent = Intent(this, registro_inventario::class.java)
            startActivity(intent)
        }

        val no = findViewById<Button>(R.id.inventario)
        no.setOnClickListener {
            val intent = Intent(this, inventario::class.java)
            startActivity(intent)
        }

        val ayu = findViewById<Button>(R.id.ayuda)
        ayu.setOnClickListener {
            val intent = Intent(this, ayuda::class.java)
            startActivity(intent)
        }
        val con = findViewById<Button>(R.id.contacta)
        con.setOnClickListener {
            val intent = Intent(this, contactanos::class.java)
            startActivity(intent)
        }
        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }
        val nos = findViewById<ImageButton>(R.id.busco)
        nos.setOnClickListener {
            val intent = Intent(this, busqueda::class.java)
            startActivity(intent)
        }
        val ay = findViewById<ImageButton>(R.id.ayudar)
        ay.setOnClickListener {
            val intent = Intent(this, ayuda::class.java)
            startActivity(intent)
        }

        val homp = findViewById<ImageButton>(R.id.btn_hom)
        homp.setOnClickListener {
            val intent = Intent(this, inicio::class.java)
            startActivity(intent)
        }
        val reg = findViewById<ImageButton>(R.id.btn_registe)
        reg.setOnClickListener {
            val intent = Intent(this, registro_inventario::class.java)
            startActivity(intent)
        }

        val nop = findViewById<ImageButton>(R.id.inventari)
        nop.setOnClickListener {
            val intent = Intent(this, inventario::class.java)
            startActivity(intent)
        }

        val cont = findViewById<ImageButton>(R.id.contactar)
        cont.setOnClickListener {
            val intent = Intent(this, contactanos::class.java)
            startActivity(intent)
        }
    }
}