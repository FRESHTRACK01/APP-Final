package com.example.fresh_track

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val channelId = "default_channel"
    private val channelName = "Título del Canal"
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // IDs únicos para cada tipo de notificación
    companion object {
        private const val NOTIFICATION_ID_LOGIN = 1001
        private const val NOTIFICATION_ID_LOGOUT = 1002
        private const val NOTIFICATION_ID_CHANGE_PASSWORD = 1003
        private const val NOTIFICATION_ID_INVENTORY_REGISTRATION = 1004
        private const val NOTIFICATION_ID_BARCODE_DOWNLOAD = 1005
        private const val NOTIFICATION_ID_BUSQUEDA = 1006
        private const val NOTIFICATION_ID_PRODUCT_EXPIRATION = 1007 // ID para notificación de expiración
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de la aplicación"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(notificationId: Int, title: String, messageBody: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo) // Asegúrate de tener este ícono en tus recursos
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun sendLoginNotification() {
        sendNotification(NOTIFICATION_ID_LOGIN, "Notificación de Sesión", "Se ha iniciado sesión exitosamente.")
    }

    fun sendLogoutNotification() {
        sendNotification(NOTIFICATION_ID_LOGOUT, "Notificación de Cierre de Sesión", "Has cerrado sesión exitosamente.")
    }

    fun sendChangePasswordNotification() {
        sendNotification(NOTIFICATION_ID_CHANGE_PASSWORD, "Notificación de Cambio de Contraseña", "Tu contraseña ha sido cambiada exitosamente.")
    }

    fun sendInventoryRegistrationNotification() {
        sendNotification(NOTIFICATION_ID_INVENTORY_REGISTRATION, "Registro de Inventario", "El inventario ha sido registrado exitosamente.")
    }

    fun sendBarcodeDownloadNotification() {
        sendNotification(NOTIFICATION_ID_BARCODE_DOWNLOAD, "Descarga de Código de Barras", "El código de barras se ha descargado exitosamente.")
    }

    fun sendBusquedaNotification() {
        sendNotification(NOTIFICATION_ID_BUSQUEDA, "Escaneo", "El código de barras se escaneó exitosamente.")
    }

    // Método para enviar notificación sobre productos que están a punto de vencer
    fun sendExpirationNotification(productName: String, expirationDate: String) {
        sendNotification(NOTIFICATION_ID_PRODUCT_EXPIRATION, "Producto a Punto de Vencer",
            "El producto '$productName' se acerca a su fecha de salida: $expirationDate.")
    }

    // Método para verificar productos cercanos a la fecha de salida
    fun checkForExpiringProducts(dbHelper: DatabaseHelper2, days: Int) {
        val expiringProducts = dbHelper.getExpiringProducts(days)

        for (product in expiringProducts) {
            sendExpirationNotification(product.nombreProducto, product.fechaSalida)
        }
    }
}