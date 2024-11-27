package com.example.fresh_track

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ayuda)


        val men = findViewById<ImageButton>(R.id.icon_two)
        men.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, menu::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }
        val button = findViewById<Button>(R.id.title)
        button.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }
        val per = findViewById<ImageButton>(R.id.icon_one)
        per.setOnClickListener {
            // Crear un Intent para iniciar SecondActivity
            val intent = Intent(this, perfil::class.java)
            startActivity(intent) // Inicia la nueva actividad
        }

        val pre1Button = findViewById<Button>(R.id.pre1)
        pre1Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Cómo agrego un nuevo producto al inventario?")

            // Mensaje del diálogo
            builder.setMessage("Para agregar un nuevo producto, simplemente ve a la sección de 'Agregar Producto', " +
                    "completa la información requerida (nombre, descripción, cantidad, precio, etc.), " +
                    "y haz clic en 'Guardar'.")

            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        val pre2Button = findViewById<Button>(R.id.pre2)
        pre2Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Puedo editar la información de un producto ya registrado?")

            // Mensaje del diálogo
            builder.setMessage("Sí, puedes editar los productos existentes. Solo busca el producto en el inventario, haz clic en él y selecciona \"Editar\". Después de hacer los cambios, guarda los cambios para actualizar el producto.")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }

        val pre3Button = findViewById<Button>(R.id.pre3)
        pre3Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Cómo puedo eliminar un producto del inventario?")

            // Mensaje del diálogo
            builder.setMessage("Para eliminar un producto, busca el producto en el inventario y dale click en eliminar")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        val pre4Button = findViewById<Button>(R.id.pre4)
        pre4Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Cómo realizo un ajuste de inventario?")

            // Mensaje del diálogo
            builder.setMessage("Para realizar ajustes, selecciona el producto, ve a la opción Modificar, ingresa la nueva cantidad y confirma el ajuste")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        val pre5Button = findViewById<Button>(R.id.pre5)
        pre5Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿La aplicación me notifica cuando un producto se está agotando?")

            // Mensaje del diálogo
            builder.setMessage("Sí, la aplicación tiene una función de notificación que te alertará cuando los productos lleguen a un nivel mínimo de stock.")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        val pre6Button = findViewById<Button>(R.id.pre6)
        pre6Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Puedo buscar productos por código o nombre?")

            // Mensaje del diálogo
            builder.setMessage("Sí, haz clic en el ícono de buscar,puedes buscar productos utilizando el nombre, código de barras o código debarras en el lector.")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        val pre7Button = findViewById<Button>(R.id.pre7)
        pre7Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Cómo puedo cambiar mi contraseña de usuario")

            // Mensaje del diálogo
            builder.setMessage("Puedes cambiar tu contraseña accediendo a la sección de configuración de tu cuenta, seleccionando \"Cambiar contraseña\" e ingresando tu nueva contraseña.")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
        val pre8Button = findViewById<Button>(R.id.pre8)
        pre8Button.setOnClickListener {
            // Crea un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Título del diálogo
            builder.setTitle("¿Cómo puedo agregar imágenes de los productos?")

            // Mensaje del diálogo
            builder.setMessage("Puedes agregar imágenes de los productos al momento de registrar o editar un producto. Solo selecciona la opción para cargar la imagen desde tu dispositivo.")
            // Botón para cerrar el diálogo
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo cuando se presiona "Aceptar"
            }

            // Muestra el diálogo
            builder.show()
        }
    }
}