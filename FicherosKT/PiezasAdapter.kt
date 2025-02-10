package com.example.speed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PiezasAdapter(
    piezasList1: PiezasActivity,
    private val piezasList: List<Piezas>,
    private val onClick: (Piezas) -> Unit
) : RecyclerView.Adapter<PiezasAdapter.PiezasViewHolder>() {

    class PiezasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.ivImagenPieza)
        val nombre: TextView = itemView.findViewById(R.id.tvNombrePieza)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiezasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pieza_item, parent, false)
        return PiezasViewHolder(view)
    }

    override fun onBindViewHolder(holder: PiezasViewHolder, position: Int) {
        val pieza = piezasList[position]
        holder.nombre.text = pieza.nombre

        // Configuración de la imagen
        val context = holder.imagen.context
        val resourceId = context.resources.getIdentifier(
            pieza.imagenId.replace("R.drawable.", ""), // Extraer solo el nombre del recurso
            "drawable",
            context.packageName
        )

        if (resourceId != 0) {
            holder.imagen.setImageResource(resourceId)
        } else {
            // Usa un recurso por defecto si no se encuentra la imagen
            holder.imagen.setImageResource(R.drawable.placeholder) // Cambia a tu imagen por defecto
        }

        holder.itemView.setOnClickListener { onClick(pieza) }
    }

    override fun getItemCount(): Int = piezasList.size
}