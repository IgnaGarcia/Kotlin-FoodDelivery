package com.example.ejercicio3.entities

import com.example.ejercicio3.network.responses.PlateResponse

data class ShopBox(
        val plateList : MutableList<PlateResponse> = mutableListOf()
)