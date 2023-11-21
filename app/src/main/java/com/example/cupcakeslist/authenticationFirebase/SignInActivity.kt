package com.example.cupcakeslist.authenticationFirebase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cupcakeslist.MainActivity
import com.example.cupcakeslist.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth



class SignInActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signin)
        val buttonSignIn = findViewById<Button>(R.id.singIn)

        val emailEnter: EditText = findViewById(R.id.enterEmail)
        val emailLayout: TextInputLayout = findViewById(R.id.editEmail)
        val passwordEnter: EditText = findViewById(R.id.enterPassword)
        val passwordLayout: TextInputLayout = findViewById(R.id.editPassword)
        val register: TextView = findViewById(R.id.registerUser)

        auth = FirebaseAuth.getInstance()

        fun EditText.addValidationTextWatcher(layout: TextInputLayout, validationLambda: (String) -> Pair<Boolean, String>) {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val (isValid, errorMessage) = validationLambda(text.toString().trim())
                    layout.error = if (!isValid) errorMessage else null
                }
            })
        }

        emailEnter.addValidationTextWatcher(emailLayout) { email ->
            when {
                email.isEmpty() -> Pair(false, "Пусте поле")
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(false, "Введіть валідний емейл")
                else -> Pair(true, "")
            }
        }

        passwordEnter.addValidationTextWatcher(passwordLayout) { password ->
            when {
                password.isEmpty() -> Pair(false, "Пусте поле")
                password.length < 8 -> Pair(false, "Пароль більше 8 символів")
                else -> Pair(true, "")
            }
        }
        register.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        buttonSignIn.setOnClickListener {
            val email = emailEnter.text.toString()
            val password = passwordEnter?.text.toString()
            signInDatabase(email, password)
        }
    }
    private fun signInDatabase(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Вхід успішний", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Неправильний email/пароль", Toast.LENGTH_SHORT).show()
                }
            }
    }
}