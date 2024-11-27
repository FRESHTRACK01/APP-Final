package com.example.fresh_track

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class InventarioAdapter(
    private val itemList: List<Producto>,
    private val dbHelper: DatabaseHelper2,
    private val onItemModified: (Producto) -> Unit,
    private val onItemDeleted: (Producto) -> Unit
) : RecyclerView.Adapter<InventarioAdapter.InventoryViewHolder>() {

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.nom)
        val tipo: TextView = itemView.findViewById(R.id.tip)
        val cantidad: TextView = itemView.findViewById(R.id.can)
        val fechaEntrada: TextView = itemView.findViewById(R.id.fee)
        val fechaSalida: TextView = itemView.findViewById(R.id.fes)
        val precio: TextView = itemView.findViewById(R.id.pre)
        val codigo: TextView = itemView.findViewById(R.id.cod)
        val modificarButton: Button = itemView.findViewById(R.id.modi)
        val eliminarButton: Button = itemView.findViewById(R.id.eli)
        val productImage: ImageView = itemView.findViewById(R.id.product_image) // Nueva referencia a la ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inventario, parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = itemList[position]

        holder.nombreProducto.text = item.nombreProducto
        holder.tipo.text = item.tipo
        holder.cantidad.text = item.cantidad.toString()
        holder.fechaEntrada.text = item.fechaEntrada
        holder.fechaSalida.text = item.fechaSalida
        holder.precio.text = "$${item.precio}"
        holder.codigo.text = item.codigo

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.context)
            .load(item.imagenPath)
            .placeholder(R.color.black) // Color de fondo mientras se carga la imagen
            .into(holder.productImage)

        // Configurar el listener para el botón de modificar
        holder.modificarButton.setOnClickListener {
            onItemModified(item) // Invocar la acción de modificar
        }

        // Configurar el listener para el botón de eliminar
        holder.eliminarButton.setOnClickListener {
            onItemDeleted(item) // Invocar la acción de eliminar
        }
    }

    override fun getItemCount(): Int = itemList.size
}
