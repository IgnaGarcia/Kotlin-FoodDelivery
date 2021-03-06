package com.example.ejercicio3.data.network.responses

data class PlateResponse(
    val id: Int,
    val image: String?,
    val glutenFree: Boolean,
    val cheap: Boolean,
    val cuisines: List<String>?,
    val dairyFree: Boolean,
    val extendedIngredients: List<ExtendedIngredient>,
    val pricePerServing: Double,
    val title: String?,
    val spoonacularScore: Double,
    val readyInMinutes: Int,
    val sourceName: String?,
    val veryPopular: Boolean,

    var hasFreeDelivery: Boolean = false,
)
