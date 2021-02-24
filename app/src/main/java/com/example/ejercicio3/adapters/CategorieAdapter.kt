package com.example.ejercicio3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.Categorie


class CategorieAdapter(var categories : List<Categorie>) : RecyclerView.Adapter<CategorieAdapter.BaseViewHolder>(){

    class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val icCategorieIcon = view.findViewById<View>(R.id.icCategorieIcon)
        val tvCategorieText = view.findViewById<TextView>(R.id.tvCategorieText)

        fun onBind(categorie : Categorie){
            icCategorieIcon.background = categorie.icon
            tvCategorieText.text = categorie.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieAdapter.BaseViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categorie, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategorieAdapter.BaseViewHolder, position: Int) {
        holder.onBind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}