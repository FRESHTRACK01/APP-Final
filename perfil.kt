package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class perfil : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var textViewID: TextView
    private lateinit var textViewNombre: TextView
    private lateinit var textViewCorreo: TextView
    private lateinit var textViewTelef: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        dbHelper = DatabaseHelper(this)

        // Inicializando los TextViews
        textViewID = findViewById(R.id.textViewID)
        textViewNombre = findViewById(R.id.textViewNombre)
        textViewCorreo = findViewById(R.id.textViewCorreo)
        textViewTelef = findViewById(R.id.textViewtelefo)
// Obtener datos de SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        val nombre = sharedPreferences.getString("nombre", null)
        val correo = sharedPreferences.getString("correo", null)
        val telefono = sharedPreferences.getString("telefono", null)

        // Mostrar los datos en TextViews
        textViewID.text = id ?: "No se encontró ID"
        textViewNombre.text = nombre ?: "No se encontró nombre"
        textViewCorreo.text = correo ?: "No se encontró correo"
        textViewTelef.text = telefono ?: "No se encontró teléfono"

        // Cargar datos adicionales desde la base de datos si es necesario
        if (correo != null) {
            loadDataFromDatabase(correo)
        }

        setUpNavigationButtons()
    }


    private fun setUpNavigationButtons() {
        val buttonOlvidar: Button = findViewById(R.id.olvidar_button2)
        buttonOlvidar.setOnClickListener {
            val intent = Intent(this, cambiarcontra2::class.java)
            startActivity(intent)
        }

        val buttonLogout: Button = findViewById(R.id.logoutButton)
        buttonLogout.setOnClickListener {

            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            // Volver a MainActivity después de 1 segundo
            buttonLogout.postDelayed({

                val notificationHelper = NotificationHelper(this)
                notificationHelper.sendLogoutNotification()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Opcional: cierra la actividad actual
            }, 1000)
        }

        val buttonBienvenida: Button = findViewById(R.id.title)
        buttonBienvenida.setOnClickListener {
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
        }

        val buttonInicio: ImageButton = findViewById(R.id.imageButton)
        buttonInicio.setOnClickListener {
            val intent = Intent(this, inicio::class.java)
            startActivity(intent)
        }
    }
    private fun loadDataFromDatabase(correo: String) {
        val cursor = dbHelper.getRegistro(correo) // Asumiendo que este método devuelve un cursor con datos
        if (cursor != null && cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id")
            val nombreIndex = cursor.getColumnIndex("nombre")
            val telefonoIndex = cursor.getColumnIndex("telefono")

            if (idIndex != -1 && nombreIndex != -1 && telefonoIndex != -1) {
                val id = cursor.getString(idIndex)
                val nombre = cursor.getString(nombreIndex)
                val telefono = cursor.getString(telefonoIndex)

                textViewID.text = "$id"
                textViewNombre.text = nombre
                textViewCorreo.text = correo
                textViewTelef.text = telefono
            } else {
                Log.e("Database", "Las columnas no se encontraron")
                displayNoData()
            }
        } else {
            displayNoData()
        }
        cursor?.close()
    }

    private fun displayNoData() {
        textViewID.text = "No se encontraron datos"
        textViewNombre.text = "No se encontraron datos"
        textViewCorreo.text = "No se encontraron datos"
        textViewTelef.text = "No se encontraron datos"
    }
}
