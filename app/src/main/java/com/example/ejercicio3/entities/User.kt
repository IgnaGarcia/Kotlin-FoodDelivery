package com.example.ejercicio3.entities

import com.example.ejercicio3.network.responses.PlateResponse

//Valores default para simular getUser() del login
data class User(
        val username: String,
        val password: String,
        val name: String?= "Igna",
        val surname: String? = "Garcia",
        val email: String? = "gnachoxp@gmail.xom",
        val location: String? = "Del Lazo 4476",
        val favourites : MutableList<Plate> = mutableListOf()
){
        fun addToFav(plate : Plate) = favourites.add(plate)

        fun removeToFav(plate: Plate) = favourites.remove(plate)

        fun plateIsFav(plate : Plate) : Boolean{
                var res = false
                favourites.forEach {
                        if(plate.id == it.id){
                                res = true
                        }
                }
                return res
        }
}