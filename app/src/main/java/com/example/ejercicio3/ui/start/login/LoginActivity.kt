package com.example.ejercicio3.ui.start.login

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivityLoginBinding
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBar()
        binding.btnSendLogin.setOnClickListener{ validate() }
    }

    //Color y Texto de appbar
    private fun setAppBar(){
        val toolbar = binding.appBar.tvToolbar
        toolbar.text = this.getString(R.string.login)
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun checkNull(content : String?) : Boolean = content != null
    private fun checkLength(content : String, minLength : Int)
            : Boolean = content.length >= minLength

    private fun validate(){
        val inputUser = binding.inputUsernameLogin
        val inputPass = binding.inputPasswordLogin
        var isValidUsername = false
        var isValidPassword = false

        //Validacion de usuario
        if(!checkNull(inputUser.text.toString())) {
            inputUser.error = "El nombre de usuario es requerido"
        }
        else if(!checkLength(inputUser.text.toString(), 3)) {
            inputUser.error = "El nombre de usuario debe contener mas de 3 caracteres"
        }
        else isValidUsername = true

        //Validacion de password
        if(!checkNull(inputPass.text.toString())) {
            inputPass.setError("La contraseña es requerida", null)
        }
        else if(!checkLength(inputPass.text.toString(), 5)) {
            inputPass.setError("La contraseña debe contener mas de 5 caracteres", null)
        }
        else isValidPassword = true


        if(isValidPassword && isValidUsername) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            SharedPreferencesManager.saveUser(this@LoginActivity,
                User(username= inputUser.text.toString(),
                    password= inputPass.text.toString()))
            startActivity(i)
        }
    }
}