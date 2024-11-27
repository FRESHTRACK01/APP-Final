package com.example.fresh_track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos
    private static final String DATABASE_NAME = "registro.db";
    // Versión de la base de datos
    private static final int DATABASE_VERSION = 2; // Incrementa la versión si realizas cambios

    private static final String TABLE_REGISTRO = "registro";

    // Sentencia SQL para crear la tabla de registros
    private static final String CREATE_TABLE_REGISTRO = "CREATE TABLE " + TABLE_REGISTRO + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT," +
            "correo TEXT UNIQUE," + // Aseguramos que el correo sea único
            "telefono TEXT," + // Nueva columna para teléfono
            "contrasena TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DatabaseHelper", "Conectado con la base de datos " + DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de registros
        db.execSQL(CREATE_TABLE_REGISTRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla anterior si existía
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRO);
        // Crear las tablas nuevamente
        onCreate(db);
    }
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contrasena", newPassword);

        int result = db.update(TABLE_REGISTRO, contentValues, "correo = ?", new String[]{email});
        return result > 0; // Returns true if the update was successful
    }

    // Método para obtener todos los registros
    public Cursor getAllRegistros() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REGISTRO, null);
    }

    // Método para insertar un nuevo registro
    public long insertRegistro(String nombre, String correo, String telefono, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("correo", correo);
        values.put("telefono", telefono); // Agregar teléfono
        values.put("contrasena", contrasena);
        return db.insert(TABLE_REGISTRO, null, values);
    }

    // Método para obtener un registro específico por correo y contraseña
    public Cursor getRegistro(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, nombre, correo, telefono FROM " + TABLE_REGISTRO + " WHERE correo = ? AND contrasena = ?", new String[]{correo, contrasena});
    }

    // Método para obtener un registro por correo
    public Cursor getRegistro(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, nombre, correo, telefono FROM " + TABLE_REGISTRO + " WHERE correo = ?", new String[]{correo});
    }
}
