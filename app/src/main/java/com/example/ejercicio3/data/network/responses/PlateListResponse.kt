package com.example.ejercicio3.data.network.responses

import com.example.ejercicio3.data.entities.Plate

data class PlateListResponse(
    val number: Int,
    val offset: Int,
    val results: List<Plate>,
    val totalResults: Int
)