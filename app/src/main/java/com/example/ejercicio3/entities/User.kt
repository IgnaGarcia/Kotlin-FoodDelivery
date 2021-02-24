package com.example.ejercicio3.entities

//Valores default para simular getUser() del login
data class User(
        val username: String,
        val password: String,
        val name: String?= "Igna",
        val surname: String? = "Garcia",
        val email: String? = "gnachoxp@gmail.xom",
        val location: String? = "Del Lazo 4476"
)