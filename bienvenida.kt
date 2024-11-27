package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)
        val men = findViewById<ImageButton>(R.id.icon_two)
        men.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, menu::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }

        val per = findViewById<ImageButton>(R.id.icon_one)
        per.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, perfil::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }

        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }
    }
}