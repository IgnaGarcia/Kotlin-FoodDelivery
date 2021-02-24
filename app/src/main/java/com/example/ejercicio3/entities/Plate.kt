package com.example.ejercicio3.entities

import com.example.ejercicio3.network.responses.ExtendedIngredient

data class Plate(
        val id: Int,
        val image: String?,
        val glutenFree: Boolean,
        val cuisines: List<String>?,
        val dairyFree: Boolean,
        val extendedIngredients: List<ExtendedIngredient>,
        val pricePerServing: Double,
        val title: String?,
        val spoonacularScore: Double,
        val readyInMinutes: Int,
        val sourceName: String?,
        val veryPopular: Boolean,

        var isFavourite: Boolean = false,
        var hasFreeDelivery: Boolean = false,
)