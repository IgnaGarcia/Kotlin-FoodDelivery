package com.example.ejercicio3.ui.start.signup

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivitySignupBinding
import com.example.ejercicio3.data.entities.User
import com.example.ejercicio3.data.local.SharedPreferencesManager
import com.example.ejercicio3.ui.main.MainActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBar()

        binding.btnSendSingup.setOnClickListener{
            validate()
        }
    }

    //Color y Texto de appbar
    private fun setAppBar(){
        val toolbar = binding.appBar.tvToolbar
        toolbar.text = this.getString(R.string.singup)
        toolbar.setTextColor(this.getColorStateList(R.color.textPrimary))

        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun checkNull(content : String?) : Boolean = content != null

    private fun checkLength(content : String, minLength : Int)
            : Boolean = content.length >= minLength

    private fun checkMail(mail : String)
            : Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()

    private fun checkPatron(content : String)
            : Boolean = """[a-zA-Z\s]*""".toRegex().matches(content)

    private fun checkPassword(password : String, confPassword : String)
            : Boolean = password == confPassword


    private fun validate(){
        val inputName = binding.inputName
        val inputSurname = binding.inputSurname
        val inputMail = binding.inputMail
        val inputUser = binding.inputUsername
        val inputPass = binding.inputPassword
        val inputPass2 = binding.inputConfirmPassword
        val aceptTerms = binding.inputCheckBox

        var isValidName  = false
        var isValidSurname = false
        var isValidMail = false
        var isValidUsername = false
        var isValidPassword = false
        var isEqualsPassword = false

        //Validacion de nombre
        if(!checkNull(inputName.text.toString())){
            inputName.error = "El nombre es requerido"
        }
        else if(!checkLength(inputName.text.toString()!!, 3)){
            inputName.error = "El nombre debe tener al menos 3 caracteres"
        }
        else if(!checkPatron(inputName.text.toString())){
            inputName.error = "El nombre debe contener solo letras"
        }
        else isValidName = true

        //Validacion de apellido
        if(!checkNull(inputSurname.text.toString())) {
            inputSurname.error = "El apellido es requerido"
        }
        else if(!checkLength(inputSurname.text.toString()!!, 3)){
            inputSurname.error = "El apellido debe tener al menos 3 caracteres"
        }
        else if(!checkPatron(inputSurname.text.toString())) {
            inputSurname.error = "El apellido debe contener solo letras"
        }
        else isValidSurname = true

        //Validacion de mail
        if(!checkNull(inputMail.text.toString())) {
            inputMail.error = "El email es requerido"
        }
        else if(!checkMail(inputMail.text.toString()!!)) {
            inputMail.error = "El email no es valido"
        }
        else isValidMail = true

        //Validacion de nombre de usuario
        if(!checkNull(inputUser.text.toString())) {
            inputUser.error = "El nombre de usuario es requerido"
        }
        else if(!checkLength(inputUser.text.toString()!!, 3)) {
            inputUser.error = "El nombre de usuario debe contener mas de 3 caracteres"
        }
        else isValidUsername = true

        //Validacion de contraseña
        if(!checkNull(inputPass.text.toString())) {
            inputPass.setError("La contraseña es requerida", null)
        }
        else if(!checkLength(inputPass.text.toString()!!, 5)) {
            inputPass.setError("La contraseña debe contener mas de 5 caracteres", null)
        }
        else isValidPassword = true

        //Validacion de confirmacion de contraseña
        if(!checkNull(inputPass2.text.toString())){
            inputPass2.setError("La contraseña es requerida", null)
        }
        else if(!checkPassword(inputPass.text.toString()!!, inputPass2.text.toString()!!)){
            inputPass2.setError("La contraseña no es la misma", null)
        }
        else isEqualsPassword = true

        //Validacion de checkbox
        if(!aceptTerms.isChecked) {
            aceptTerms.error = ""
        } else aceptTerms.error = null

        if(aceptTerms.isChecked && isEqualsPassword && isValidPassword &&
                isValidUsername && isValidMail && isValidSurname && isValidName) {
            val user = User(name = inputName.text.toString()!!,
                surname = inputSurname.text.toString()!!,
                email = inputMail.text.toString()!!,
                username = inputUser.text.toString()!!,
                password = inputPass.text.toString()!!)
            goToHome(user)
        }
    }

    private fun goToHome(user : User){
        val i = Intent(this@SignupActivity, MainActivity::class.java)
        SharedPreferencesManager.saveUser(this@SignupActivity, user)
        startActivity(i)
    }
}