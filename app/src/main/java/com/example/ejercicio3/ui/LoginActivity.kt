package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class LoginActivity : AppCompatActivity() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setAppBar()

        val inputUser = findViewById<EditText>(R.id.inputUsernameLogin)
        val inputPass = findViewById<EditText>(R.id.inputPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnSendLogin)

        btnLogin.setOnClickListener{ validate(inputUser, inputPass) }
    }

    //Color y Texto de appbar
    fun setAppBar(){
        val toolbar = findViewById<TextView>(R.id.tvToolbar)
        toolbar.text = this.getString(R.string.login)
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun checkNull(content : String?) : Boolean = content != null
    private fun checkLength(content : String, minLength : Int) : Boolean = content.length >= minLength

    private fun validate(inputUser : EditText, inputPass : EditText){
        var isValidUsername = false
        var isValidPassword = false
        val username : String? = inputUser.text.toString()
        val password : String? = inputPass.text.toString()

        //Validacion de usuario
        if(!checkNull(username)) inputUser.error = "El nombre de usuario es requerido"
        else if(!checkLength(username!!, 3)) inputUser.error = "El nombre de usuario debe contener mas de 3 caracteres"
        else isValidUsername = true

        //Validacion de password
        if(!checkNull(password)) inputPass.setError("La contraseña es requerida", null)
        else if(!checkLength(password!!, 5)) inputPass.setError("La contraseña debe contener mas de 5 caracteres", null)
        else isValidPassword = true


        if(isValidPassword && isValidUsername) {
            val i = Intent(this@LoginActivity, HomeActivity::class.java)
            sharedPrefManager.saveUser(this@LoginActivity, User(username= username!!, password= password!!))
            startActivity(i)
        }
    }
}