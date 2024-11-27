package com.example.fresh_track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper2 extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "inventario.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla
    private static final String TABLE_INVENTARIO = "inventario";

    // Sentencia SQL para crear la tabla de inventario
    private static final String CREATE_TABLE_INVENTARIO = "CREATE TABLE " + TABLE_INVENTARIO + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre_producto TEXT," +
            "tipo TEXT," +
            "cantidad INTEGER," +
            "fecha_entrada TEXT," +
            "fecha_salida TEXT," +
            "precio REAL," +
            "codigo TEXT UNIQUE," +
            "imagen_path TEXT);";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DatabaseHelper2", "Conectado con la base de datos " + DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INVENTARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTARIO);
        onCreate(db);
    }

    // Método para obtener todos los productos
    public Cursor getAllInventario() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_INVENTARIO, null, null, null, null, null, null);
    }

    // Método para insertar un nuevo producto
    public long insertInventario(String nombreProducto, String tipo, int cantidad, String fechaEntrada,
                                 String fechaSalida, double precio, String codigo, String imagenPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre_producto", nombreProducto);
        values.put("tipo", tipo);
        values.put("cantidad", cantidad);
        values.put("fecha_entrada", fechaEntrada);
        values.put("fecha_salida", fechaSalida);
        values.put("precio", precio);
        values.put("codigo", codigo);
        values.put("imagen_path", imagenPath);
        return db.insert(TABLE_INVENTARIO, null, values);
    }

    // Método para eliminar un producto por ID
    public int deleteProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INVENTARIO, "id = ?", new String[]{String.valueOf(id)});
    }

    // Método para actualizar un producto
    public int updateProducto(int id, String nombreProducto, String tipo, int cantidad, String fechaEntrada,
                              String fechaSalida, double precio, String imagenPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre_producto", nombreProducto);
        values.put("tipo", tipo);
        values.put("cantidad", cantidad);
        values.put("fecha_entrada", fechaEntrada);
        values.put("fecha_salida", fechaSalida);
        values.put("precio", precio);
        values.put("imagen_path", imagenPath);

        return db.update(TABLE_INVENTARIO, values, "id = ?", new String[]{String.valueOf(id)});
    }

    // Método para obtener un producto por ID
    public Cursor getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_INVENTARIO, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    // Método para buscar un producto por código
    public ProductDetails getProductDetailsByCode(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        ProductDetails productDetails = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_INVENTARIO + " WHERE codigo = ?", new String[]{codigo});
            if (cursor != null && cursor.moveToFirst()) {
                String nombreProducto = cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto"));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                String fechaEntrada = cursor.getString(cursor.getColumnIndexOrThrow("fecha_entrada"));
                String fechaSalida = cursor.getString(cursor.getColumnIndexOrThrow("fecha_salida"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                String imagenPath = cursor.getString(cursor.getColumnIndexOrThrow("imagen_path"));

                productDetails = new ProductDetails(nombreProducto, tipo, cantidad, fechaEntrada,
                        fechaSalida, precio, imagenPath);
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error al obtener detalles: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return productDetails;
    }

    // Método para obtener productos cuya fecha de salida está próxima
    public List<Producto> getExpiringProducts(int days) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Producto> expiringProducts = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Obtener la fecha actual
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, days);
            String dateLimit = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

            // Consultar productos cuya fecha de salida es menor o igual a la fecha límite
             cursor = db.rawQuery("SELECT * FROM " + TABLE_INVENTARIO + " WHERE fecha_salida <= ? ORDER BY fecha_salida ASC", new String[]{dateLimit});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String nombreProducto = cursor.getString(cursor.getColumnIndexOrThrow("nombre_producto"));
                    String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                    int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                    String fechaEntrada = cursor.getString(cursor.getColumnIndexOrThrow("fecha_entrada"));
                    String fechaSalida = cursor.getString(cursor.getColumnIndexOrThrow("fecha_salida"));
                    double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                    String codigo = cursor.getString(cursor.getColumnIndexOrThrow("codigo"));
                    String imagenPath = cursor.getString(cursor.getColumnIndexOrThrow("imagen_path"));

                    Producto producto = new Producto(id, nombreProducto, tipo, cantidad, fechaEntrada, fechaSalida, precio, codigo, imagenPath);
                    expiringProducts.add(producto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error al obtener productos que están por vencer: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return expiringProducts;
    }

    // Clase para representar los detalles del producto
    public static class ProductDetails {
        private final String nombreProducto;
        private final String tipo;
        private final int cantidad;
        private final String fechaEntrada;
        private final String fechaSalida;
        private final double precio;
        private final String imagenPath;

        public ProductDetails(String nombreProducto, String tipo, int cantidad, String fechaEntrada,
                              String fechaSalida, double precio, String imagenPath) {
            this.nombreProducto = nombreProducto;
            this.tipo = tipo;
            this.cantidad = cantidad;
            this.fechaEntrada = fechaEntrada;
            this.fechaSalida = fechaSalida;
            this.precio = precio;
            this.imagenPath = imagenPath;
        }

        @Override
        public String toString() {
            return "ProductDetails{" +
                    "nombreProducto='" + nombreProducto + '\'' +
                    ", tipo='" + tipo + '\'' +
                    ", cantidad=" + cantidad +
                    ", fechaEntrada='" + fechaEntrada + '\'' +
                    ", fechaSalida='" + fechaSalida + '\'' +
                    ", precio=" + precio +
                    ", imagenPath='" + imagenPath + '\'' +
                    '}';
        }

        // Getters
        public String getNombreProducto() { return nombreProducto; }
        public String getTipo() { return tipo; }
        public int getCantidad() { return cantidad; }
        public String getFechaEntrada() { return fechaEntrada; }
        public String getFechaSalida() { return fechaSalida; }
        public double getPrecio() { return precio; }
        public String getImagenPath() { return imagenPath; }
    }
}