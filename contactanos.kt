package com.example.fresh_track

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class contactanos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contactanos)

        // Acción de abrir el menú
        val men = findViewById<ImageButton>(R.id.icon_two)
        men.setOnClickListener {
            val intent = Intent(this, menu::class.java)
            startActivity(intent)
        }

        // Acción de abrir la bienvenida
        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
        }

        // Acción de abrir el perfil
        val per = findViewById<ImageButton>(R.id.icon_one)
        per.setOnClickListener {
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
        }

        // Botón para enviar WhatsApp al primer número
        val btnWhatsApp1 = findViewById<Button>(R.id.btnWhatsApp1)
        btnWhatsApp1.setOnClickListener {
            val phoneNumber1 = "5611708704"
            enviarWhatsApp(phoneNumber1)
        }

        // Botón para enviar WhatsApp al segundo número
        val btnWhatsApp2 = findViewById<Button>(R.id.btnWhatsApp2)
        btnWhatsApp2.setOnClickListener {
            val phoneNumber2 = "5517782551"
            enviarWhatsApp(phoneNumber2)
        }

        // Agregar funcionalidad para enviar correo
        val editTextProblema = findViewById<EditText>(R.id.editTextProblema)
        val editTextDescripcion = findViewById<EditText>(R.id.editTextDescripcion)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            val problema = editTextProblema.text.toString()
            val descripcion = editTextDescripcion.text.toString()

            // Crear un Intent para enviar el correo electrónico con la aplicación de Gmail
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("diegvil14@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, problema)
                putExtra(Intent.EXTRA_TEXT, descripcion)
            }

            // Verificar si hay una aplicación de Gmail disponible para enviar correos electrónicos
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)

                // Limpiar los campos de EditText
                editTextProblema.text.clear()
                editTextDescripcion.text.clear()
            } else {
                // Mostrar un mensaje de error si no hay ninguna aplicación de Gmail disponible
                Toast.makeText(this, "No se encontró la aplicación de Gmail para enviar correos electrónicos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para enviar un mensaje de WhatsApp
    private fun enviarWhatsApp(phoneNumber: String) {
        // Formatear el número para WhatsApp (número con código de país para México)
        val formattedPhoneNumber = "52$phoneNumber"

        // Mensaje predefinido para WhatsApp
        val message = "Hola, necesito ayuda de soporte."

        // URL de WhatsApp
        val url = "https://wa.me/$formattedPhoneNumber?text=${Uri.encode(message)}"

        try {
            // Intent para abrir WhatsApp con el mensaje
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url) // URL de WhatsApp correctamente formateada
            }
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir WhatsApp. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
        }
    }
}
