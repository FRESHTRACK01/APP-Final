package com.example.fresh_track

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    private lateinit var passwordEditText: EditText
    private lateinit var togglePasswordButton: ImageButton
    private var isPasswordVisible = false
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        passwordEditText = findViewById(R.id.editTextPassword)
        togglePasswordButton = findViewById(R.id.btn_show_password)

        togglePasswordButton.setOnClickListener {
            // Guarda la posición del cursor antes de cambiar el inputType
            val cursorPosition = passwordEditText.selectionStart

            if (isPasswordVisible) {
                // Ocultar contraseña
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordButton.setImageResource(R.drawable.ver) // Cambia el ícono a "cerrado"
                isPasswordVisible = false
            } else {
                // Mostrar contraseña
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePasswordButton.setImageResource(R.drawable.no__ver) // Cambia el ícono a "abierto"
                isPasswordVisible = true
            }

            // Restaurar la posición del cursor
            passwordEditText.setSelection(cursorPosition)
        }

        val ol = findViewById<Button>(R.id.olvidar_button)
        ol.setOnClickListener {
            val intent = Intent(this, cambiarcontra::class.java)
            startActivity(intent)
        }
        val cm = findViewById<Button>(R.id.register_button)
        cm.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
        val ini = findViewById<Button>(R.id.inicio_button)
        ini.setOnClickListener {
            val intent = Intent(this, inicio::class.java)
            startActivity(intent)
        }

        val pre8Button = findViewById<Button>(R.id.title)
        pre8Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("Bienvenido a Fresh Track")

            // Mensaje del diálogo
            builder.setMessage("Fresh Track es una aplicación de gestión de inventarios fundada en 2024 por cuatro jóvenes emprendedores de Ingeniería en Sistemas Computacionales. Nuestra misión es facilitar a las empresas el control de sus inventarios con una solución moderna, confiable y fácil de usar, que permite realizar un seguimiento preciso de productos y generar reportes en tiempo real. ¡Gracias por elegirnos para optimizar la administración de tu inventario!")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        dbHelper = DatabaseHelper(this)
        Log.d("MainActivity", "Conexión a la base de datos establecida.")

        val buttonLogin: Button = findViewById(R.id.inicio_button)
        val buttonRegister: Button = findViewById(R.id.register_button)
        val editTextEmail: EditText = findViewById(R.id.editTextUsername)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)


        buttonLogin.setOnClickListener {
            val username = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (checkCredentials(username, password)) {
                // Obtener información del usuario
                val userData = getUserData(username)
                if (userData != null) {
                    // Almacenar datos en SharedPreferences
                    val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("id", userData.first)
                    editor.putString("nombre", userData.second)
                    editor.putString("correo", username)
                    editor.putString("telefono", userData.third)
                    editor.apply()

                    val notificationHelper = NotificationHelper(this)
                    notificationHelper.sendLoginNotification()

                    // Redirigir a la actividad de inicio
                    val intentInicio = Intent(this, inicio::class.java)
                    startActivity(intentInicio)

                    // Opcional: cierra MainActivity para que no vuelva atrás
                    finish()
                } else {
                    Toast.makeText(this, "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }


        buttonRegister.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }



    private fun checkCredentials(username: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("correo", "contrasena")
        val selection = "correo = ? AND contrasena = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(
            "registro", projection, selection, selectionArgs, null, null, null
        )
        val found = cursor.count > 0
        cursor.close()
        return found
    }

    private fun getUserData(correo: String): Triple<String, String, String>? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("id", "nombre", "telefono")
        val selection = "correo = ?"
        val selectionArgs = arrayOf(correo)
        val cursor: Cursor = db.query(
            "registro", projection, selection, selectionArgs, null, null, null
        )

        var userData: Triple<String, String, String>? = null
        if (cursor.moveToFirst()) {
            try {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
                userData = Triple(id, nombre, telefono)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error al obtener datos del cursor: ${e.message}")
            }
        }
        cursor.close()
        return userData
    }

    override fun onDestroy() {
        dbHelper.close()
        Log.d("MainActivity", "Conexión a la base de datos cerrada.")
        super.onDestroy()
    }
}
