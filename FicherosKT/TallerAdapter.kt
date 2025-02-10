package com.example.speed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TallerAdapter(
    private val listaTalleres: List<Taller>
) : RecyclerView.Adapter<TallerAdapter.TallerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_taller, parent, false)
        return TallerViewHolder(vista)
    }

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val taller = listaTalleres[position]
        holder.bind(taller)
    }

    override fun getItemCount(): Int = listaTalleres.size

    class TallerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivTaller: ImageView = itemView.findViewById(R.id.ivTaller)
        private val tvNombreTaller: TextView = itemView.findViewById(R.id.tvNombreTaller)
        private val tvEstadoStock: TextView = itemView.findViewById(R.id.tvEstadoStock)

        fun bind(taller: Taller) {
            ivTaller.setImageResource(taller.imagenResId)
            tvNombreTaller.text = taller.nombre

            if (taller.tieneStock) {
                tvEstadoStock.text = "Con stock"
                tvEstadoStock.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
                )
            } else {
                tvEstadoStock.text = "Sin stock"
                tvEstadoStock.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
                )
            }
        }
    }
}