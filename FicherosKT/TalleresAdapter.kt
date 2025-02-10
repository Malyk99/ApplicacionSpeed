package com.example.speed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TalleresAdapter(
    private val talleresList: List<Talleres>,
    private val onClick: (Talleres) -> Unit
) : RecyclerView.Adapter<TalleresAdapter.TalleresViewHolder>() {

    class TalleresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val imagenImageView: ImageView = itemView.findViewById(R.id.imagenImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalleresViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.taller_item, parent, false)
        return TalleresViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TalleresViewHolder, position: Int) {
        val taller = talleresList[position]
        holder.nombreTextView.text = taller.nombre

        // Cargar imagen del taller
        val context = holder.imagenImageView.context
        val resourceId = context.resources.getIdentifier(
            taller.imagenId.substringAfter("R.drawable."),
            "drawable",
            context.packageName
        )
        holder.imagenImageView.setImageResource(resourceId)

        holder.itemView.setOnClickListener { onClick(taller) }
    }

    override fun getItemCount(): Int = talleresList.size
}
