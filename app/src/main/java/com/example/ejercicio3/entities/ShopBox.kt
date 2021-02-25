package com.example.ejercicio3.entities

import com.example.ejercicio3.network.responses.PlateResponse

data class ShopBox(
        val plateList : MutableList<PlateResponse> = mutableListOf(),
        val totalPrice : Double = 0.0,
){

    fun calcTotal() : Double{
        var acc = 0.0
        plateList.forEach { plate -> acc + plate.pricePerServing }
        return acc
    }
}