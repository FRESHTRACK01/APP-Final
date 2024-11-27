package com.example.fresh_track


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class cambiarcontra2 : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var togglePasswordButton: ImageButton
    private lateinit var toggleConfirmPasswordButton: ImageButton
    private lateinit var toggleantiPasswordButton: ImageButton
    private lateinit var databaseHelper: DatabaseHelper
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambiarcontra)

        // Initialize EditTexts and DatabaseHelper
        usernameEditText = findViewById(R.id.username2)
        passwordEditText = findViewById(R.id.password2)
        confirmPasswordEditText = findViewById(R.id.password3)
        databaseHelper = DatabaseHelper(this)

        // Set input types for password fields
        usernameEditText.inputType = InputType.TYPE_CLASS_TEXT
        passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Initialize buttons for toggling password visibility
        togglePasswordButton = findViewById(R.id.btn_show_password2)
        toggleConfirmPasswordButton = findViewById(R.id.btn_show_password3)
        // Set up toggle listeners
        togglePasswordButton.setOnClickListener {
            isPasswordVisible = togglePasswordVisibility(passwordEditText, togglePasswordButton, isPasswordVisible)
        }
        toggleConfirmPasswordButton.setOnClickListener {
            isConfirmPasswordVisible = togglePasswordVisibility(confirmPasswordEditText, toggleConfirmPasswordButton, isConfirmPasswordVisible)
        }

        // Button to change password
        val changePasswordButton = findViewById<Button>(R.id.inicio_button4)
        changePasswordButton.setOnClickListener {
            changePassword()
        }
        // Botón para volver a la MainActivity
        val per = findViewById<ImageButton>(R.id.icon_two)
        per.setOnClickListener {
            // Crear un Intent para iniciar inicio
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

    private fun changePassword() {
        val email = usernameEditText.text.toString().trim()
        val newPassword = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Validate input
        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val notificationHelper = NotificationHelper(this)
        notificationHelper.sendChangePasswordNotification()
        // Update password in the database
        val isUpdated = databaseHelper.updatePassword(email, newPassword)
        if (isUpdated) {
            Toast.makeText(this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show()
            clearFields()
            navigateToMainActivity()
        } else {
            Toast.makeText(this, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        usernameEditText.text.clear()
        passwordEditText.text.clear()
        confirmPasswordEditText.text.clear()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, perfil::class.java)
        startActivity(intent)
        finish() // Finish this activity
    }

    private fun togglePasswordVisibility(editText: EditText, button: ImageButton, isVisible: Boolean): Boolean {
        val cursorPosition = editText.selectionStart

        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            button.setImageResource(R.drawable.ver) // Change icon to "closed"
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            button.setImageResource(R.drawable.no__ver) // Change icon to "open"
        }

        editText.setSelection(cursorPosition)
        return !isVisible
    }
}