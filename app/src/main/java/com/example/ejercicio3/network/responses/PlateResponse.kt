package com.example.ejercicio3.network.responses

data class PlateResponse(
        val aggregateLikes: Int,
        val analyzedInstructions: List<AnalyzedInstruction>,
        val cheap: Boolean,
        val creditsText: String,
        val cuisines: List<String>,
        val dairyFree: Boolean,
        val diets: List<String>,
        val dishTypes: List<String>,
        val extendedIngredients: List<ExtendedIngredient>,
        val gaps: String,
        val glutenFree: Boolean,
        val healthScore: Double,
        val id: Int,
        val image: String,
        val imageType: String,
        val instructions: String,
        val license: String,
        val lowFodmap: Boolean,
        val occasions: List<Any>,
        val originalId: Any,
        val pricePerServing: Double,
        val readyInMinutes: Int,
        val servings: Int,
        val sourceName: String,
        val sourceUrl: String,
        val spoonacularScore: Double,
        val spoonacularSourceUrl: String,
        val summary: String,
        val sustainable: Boolean,
        val title: String,
        val vegan: Boolean,
        val vegetarian: Boolean,
        val veryHealthy: Boolean,
        val veryPopular: Boolean,
        val weightWatcherSmartPoints: Int,
        val winePairing: WinePairing
)

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)

data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
)

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
)

data class Equipment(
    val id: Int,
    val image: String,
    val localizedName: String,
    val name: String
)

data class Ingredient(
    val id: Int,
    val image: String,
    val localizedName: String,
    val name: String
)

data class Length(
    val number: Int,
    val unit: String
)

data class Measures(
    val metric: Metric,
    val us: Us
)

data class Metric(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)

data class Us(
    val amount: Double,
    val unitLong: String,
    val unitShort: String
)

data class ProductMatche(
    val averageRating: Double,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val link: String,
    val price: String,
    val ratingCount: Double,
    val score: Double,
    val title: String
)


