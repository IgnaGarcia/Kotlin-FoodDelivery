package com.example.ejercicio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio3.R
import com.example.ejercicio3.databinding.ActivitySingupBinding
import com.example.ejercicio3.entities.User
import com.example.ejercicio3.local.SharedPreferencesManager

class SingupActivity : AppCompatActivity() {
    private val sharedPrefManager : SharedPreferencesManager = SharedPreferencesManager
    private lateinit var binding : ActivitySingupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBar()

        val inputName = binding.inputName
        val inputSurname = binding.inputSurname
        val inputMail = binding.inputMail
        val inputUsername = binding.inputUsername
        val inputPass = binding.inputPassword
        val inputPass2 = binding.inputConfirmPassword
        val aceptTerms = binding.inputCheckBox

        val btnSingUp = binding.btnSendSingup

        btnSingUp.setOnClickListener{
            validate(aceptTerms, inputName, inputSurname,
                inputMail, inputUsername, inputPass, inputPass2)
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


    private fun validate(aceptTerms : CheckBox,
                         inputName : EditText, inputSurname : EditText,
                         inputMail : EditText, inputUser : EditText,
                         inputPass : EditText, inputPass2 : EditText){
        val name : String? = inputName.text.toString()
        var isValidName  = false
        val surname : String? = inputSurname.text.toString()
        var isValidSurname = false
        val mail : String? = inputMail.text.toString()
        var isValidMail = false
        val username : String? = inputUser.text.toString()
        var isValidUsername = false
        val password : String? = inputPass.text.toString()
        var isValidPassword = false
        val confPassword : String? = inputPass2.text.toString()
        var isEqualsPassword = false

        //Validacion de nombre
        if(!checkNull(name)) inputName.error = "El nombre es requerido"
        else if(!checkLength(name!!, 3))
            inputName.error = "El nombre debe tener al menos 3 caracteres"
        else if(!checkPatron(name)) inputName.error = "El nombre debe contener solo letras"
        else isValidName = true

        //Validacion de apellido
        if(!checkNull(surname)) inputSurname.error = "El apellido es requerido"
        else if(!checkLength(surname!!, 3))
            inputSurname.error = "El apellido debe tener al menos 3 caracteres"
        else if(!checkPatron(surname)) inputSurname.error = "El apellido debe contener solo letras"
        else isValidSurname = true

        //Validacion de mail
        if(!checkNull(mail)) inputMail.error = "El email es requerido"
        else if(!checkMail(mail!!)) inputMail.error = "El email no es valido"
        else isValidMail = true

        //Validacion de nombre de usuario
        if(!checkNull(username)) inputUser.error = "El nombre de usuario es requerido"
        else if(!checkLength(username!!, 3))
            inputUser.error = "El nombre de usuario debe contener mas de 3 caracteres"
        else isValidUsername = true

        //Validacion de contraseña
        if(!checkNull(password)) inputPass.setError("La contraseña es requerida", null)
        else if(!checkLength(password!!, 5))
            inputPass.setError("La contraseña debe contener mas de 5 caracteres", null)
        else isValidPassword = true

        //Validacion de confirmacion de contraseña
        if(!checkNull(confPassword))
            inputPass2.setError("La contraseña es requerida", null)
        else if(!checkPassword(password!!, confPassword!!))
            inputPass2.setError("La contraseña no es la misma", null)
        else isEqualsPassword = true

        //Validacion de checkbox
        if(!aceptTerms.isChecked) aceptTerms.error = "" else aceptTerms.error = null

        if(aceptTerms.isChecked && isEqualsPassword && isValidPassword &&
                isValidUsername && isValidMail && isValidSurname && isValidName) {

            goToHome(User(name = name!!, surname = surname!!, email = mail!!,
                    username = username!!, password = password!!))
        }
    }

    private fun goToHome(user : User){
        val i = Intent(this@SingupActivity, MainActivity::class.java)
        sharedPrefManager.saveUser(this@SingupActivity, user)
        startActivity(i)
    }
}