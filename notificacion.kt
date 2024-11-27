package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class notificacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notificacion)


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
        val btnNotifications1 = findViewById<Button>(R.id.btn_notifications_1)
        btnNotifications1.setOnClickListener {
            val intent = Intent(this, inventario::class.java)
            intent.putExtra("show_noti1", true)  // Muestra noti1
            intent.putExtra("show_noti2", false) // Oculta noti2
            startActivity(intent)
        }

        val btnNotifications2 = findViewById<Button>(R.id.btn_notifications_2)
        btnNotifications2.setOnClickListener {
            val intent = Intent(this, inventario::class.java)
            intent.putExtra("show_noti1", false) // Oculta noti1
            intent.putExtra("show_noti2", true)  // Muestra noti2
            startActivity(intent)
        }





    }
}