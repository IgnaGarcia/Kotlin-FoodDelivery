package com.example.ejercicio3.entities

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.ejercicio3.R

class Categorie(
        val id : Int,
        val text: String,
        val icon: Drawable
)

public fun getCategories(context : Context) : List<Categorie> {
    return listOf(Categorie(1,context.getString(R.string.favourites), context.getDrawable(R.drawable.ic_favorite)!!),
        Categorie(2, context.getString(R.string.offers), context.getDrawable(R.drawable.ic_offer)!!),
        Categorie(3, context.getString(R.string.trends), context.getDrawable(R.drawable.ic_trend)!!),
        Categorie(4, context.getString(R.string.more), context.getDrawable(R.drawable.ic_more)!!))
}