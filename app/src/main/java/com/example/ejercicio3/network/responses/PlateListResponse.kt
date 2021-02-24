package com.example.ejercicio3.network.responses

import com.example.ejercicio3.entities.Plate

data class PlateListResponse(
    val number: Int,
    val offset: Int,
    val results: List<Plate>,
    val totalResults: Int
)