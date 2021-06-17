package com.example.ejercicio3.ui.main.homeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ItemCategorieBinding
import com.example.ejercicio3.data.entities.Categorie


class CategorieAdapter(var categories : List<Categorie>,
                       var onClickCategorie : OnClickCategorie
)
    : RecyclerView.Adapter<CategorieAdapter.BaseViewHolder>(){

    inner class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCategorieBinding.bind(view)

        fun onBind(categorie : Categorie){
            binding.icCategorieIcon.background = categorie.icon
            binding.tvCategorieText.text = categorie.text

            itemView.setOnClickListener{
                onClickCategorie.onClickCategorie(categorie.id)
            }
        }
    }

    interface OnClickCategorie{
        fun onClickCategorie(categorie : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BaseViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categorie, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}