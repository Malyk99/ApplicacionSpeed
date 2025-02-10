package com.example.speed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CocheAdapter(
    private val coches: List<Coche>,
    private val onItemClick: (Coche) -> Unit
) : RecyclerView.Adapter<CocheAdapter.CocheViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocheViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coche, parent, false)
        return CocheViewHolder(view)
    }

    override fun onBindViewHolder(holder: CocheViewHolder, position: Int) {
        val coche = coches[position]
        holder.bind(coche)
    }

    override fun getItemCount() = coches.size

    inner class CocheViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNombre: TextView = view.findViewById(R.id.tvNombreCoche)
        private val ivImagen: ImageView = view.findViewById(R.id.ivImagenCoche)

        fun bind(coche: Coche) {
            tvNombre.text = coche.nombre
            Glide.with(itemView.context).load(coche.imageResId).into(ivImagen)
            itemView.setOnClickListener { onItemClick(coche) }
        }
    }
}

