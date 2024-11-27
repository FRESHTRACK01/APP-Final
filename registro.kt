package com.example.fresh_track

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class registro : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var correoEditText: EditText
    private lateinit var numeroEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfEditText: EditText
    private lateinit var togglePasswordButton: ImageButton
    private lateinit var togglePasswordConfButton: ImageButton

    private var isPasswordVisible = false
    private var isPasswordConfVisible = false
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        dbHelper = DatabaseHelper(this)

        // Inicializa los EditTexts
        usernameEditText = findViewById(R.id.username)
        correoEditText = findViewById(R.id.correo)
        numeroEditText = findViewById(R.id.numero)
        passwordEditText = findViewById(R.id.password)
        passwordConfEditText = findViewById(R.id.passwordconf)

        // Inicializa los botones para mostrar/ocultar contraseñas
        togglePasswordButton = findViewById(R.id.btn_show_password)
        togglePasswordConfButton = findViewById(R.id.btn_show_password1)

        togglePasswordButton.setOnClickListener {
            isPasswordVisible = togglePasswordVisibility(passwordEditText, togglePasswordButton, isPasswordVisible)
        }

        togglePasswordConfButton.setOnClickListener {
            isPasswordConfVisible = togglePasswordVisibility(passwordConfEditText, togglePasswordConfButton, isPasswordConfVisible)
        }

        val buttonGuardar = findViewById<Button>(R.id.guardar)
        buttonGuardar.setOnClickListener {
            // Verificar si todos los campos están llenos y las contraseñas coinciden
            if (isFormValid()) {
                // Mostrar el diálogo de términos y condiciones
                showTermsAndConditionsDialog()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonBack = findViewById<Button>(R.id.register_button)
        buttonBack.setOnClickListener {
            finish() // Regresar a la pantalla anterior
        }
    }

    // Función para alternar la visibilidad de las contraseñas
    private fun togglePasswordVisibility(editText: EditText, button: ImageButton, isVisible: Boolean): Boolean {
        // Guarda la posición del cursor antes de cambiar el inputType
        val cursorPosition = editText.selectionStart

        if (isVisible) {
            // Ocultar contraseña
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            button.setImageResource(R.drawable.ver) // Cambia el ícono a "cerrado"
        } else {
            // Mostrar contraseña
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            button.setImageResource(R.drawable.no__ver) // Cambia el ícono a "abierto"
        }

        // Restaurar la posición del cursor
        editText.setSelection(cursorPosition)

        // Retorna el nuevo estado de visibilidad
        return !isVisible
    }

    // Función para verificar si todos los campos están llenos y las contraseñas coinciden
    private fun isFormValid(): Boolean {
        val username = usernameEditText.text.toString().trim()
        val correo = correoEditText.text.toString().trim()
        val numero = numeroEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val passwordConf = passwordConfEditText.text.toString().trim()

        // Verificar que todos los campos estén llenos
        if (username.isEmpty() || correo.isEmpty() || numero.isEmpty() || password.isEmpty() || passwordConf.isEmpty()) {
            return false
        }

        // Verificar que el número de teléfono solo tenga números
        if (!numero.matches(Regex("^[0-9]+$"))) {
            numeroEditText.error = "Solo se aceptan números"
            return false
        }

        // Verificar que el correo electrónico sea válido
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            correoEditText.error = "Por favor, ingresa un correo electrónico válido"
            return false
        }

        // Verificar que las contraseñas coincidan
        if (password != passwordConf) {
            passwordConfEditText.error = "Las contraseñas no coinciden"
            return false
        }

        return true
    }

    // Función para mostrar el diálogo de términos y condiciones
    private fun showTermsAndConditionsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Términos y Condiciones")
        builder.setMessage("Al registrarte, aceptas nuestros términos y condiciones. \n\n" +
                "1. **Uso de tus datos**: Los datos proporcionados en este formulario, como tu nombre, correo electrónico y número de teléfono, serán utilizados únicamente para fines relacionados con tu cuenta y servicios dentro de nuestra aplicación. No se compartirán con terceros sin tu consentimiento expreso.\n\n" +
                "2. **Seguridad**: **Nos comprometemos a proteger la seguridad de tu información personal y tomar las medidas adecuadas para evitar accesos no autorizados.**\n\n" +
                "3. **Propósito de la recopilación de datos**: Tus datos serán utilizados exclusivamente para el registro, gestión de tu cuenta y para mejorar la experiencia en la plataforma.\n\n" +
                "4. **No uso indebido**: En ningún momento utilizaremos tus datos personales de forma que pueda considerarse inapropiado, malintencionado o ilegal.\n\n" +
                "Al hacer clic en 'Aceptar', confirmas que has leído y comprendido estos términos. Si no estás de acuerdo, puedes cancelar el registro.")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            // Registrar al usuario en la base de datos
            registerUser(
                usernameEditText.text.toString(),
                correoEditText.text.toString(),
                numeroEditText.text.toString(),
                passwordEditText.text.toString()
            )
            Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show()

            // Limpiar los campos de EditText
            clearEditTexts()

            // Iniciar la actividad principal
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        // Mostrar el diálogo
        builder.create().show()
    }

    // Función para registrar al usuario en la base de datos
    private fun registerUser(nombre: String, correo: String, telefono: String, contrasena: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("correo", correo)
            put("telefono", telefono)
            put("contrasena", contrasena)
        }
        db.insert("registro", null, values)
        db.close()
    }

    // Función para limpiar los EditTexts
    private fun clearEditTexts() {
        usernameEditText.text.clear()
        correoEditText.text.clear()
        numeroEditText.text.clear()
        passwordEditText.text.clear()
        passwordConfEditText.text.clear()
    }
}