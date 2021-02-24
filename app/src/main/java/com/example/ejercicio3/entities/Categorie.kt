package com.example.ejercicio3.entities

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.ejercicio3.R

class Categorie(
    val text: String,
    val icon: Drawable
)

public fun getCategories(context : Context) : List<Categorie> {
    return listOf(Categorie(context.getString(R.string.favourites), context.getDrawable(R.drawable.ic_favorite)!!),
        Categorie(context.getString(R.string.offers), context.getDrawable(R.drawable.ic_offer)!!),
        Categorie(context.getString(R.string.trends), context.getDrawable(R.drawable.ic_trend)!!),
        Categorie(context.getString(R.string.more), context.getDrawable(R.drawable.ic_more)!!))
}