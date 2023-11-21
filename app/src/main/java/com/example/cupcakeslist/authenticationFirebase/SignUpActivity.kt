package com.example.cupcakeslist.authenticationFirebase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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


class SignUpActivity() : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sign_up)
        val buttonSignUp = findViewById<Button>(R.id.singUp)

        auth = FirebaseAuth.getInstance()

        val alreadyRegistered = findViewById<TextView>(R.id.alreadyRegistered)
        val emailEnter: EditText = findViewById(R.id.enterEmail)
        val emailLayout: TextInputLayout = findViewById(R.id.editEmail)
        val passwordEnter: EditText = findViewById(R.id.enterPassword)
        val passwordLayout: TextInputLayout = findViewById(R.id.editPassword)
        val retypePassword: EditText = findViewById(R.id.retypePassword)
        val editPasswordRetype: TextInputLayout = findViewById(R.id.editRetypePassword)


        fun EditText.addValidationTextWatcher(
            layout: TextInputLayout,
            validationLambda: (String) -> Pair<Boolean, String>
        ) {
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
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(
                    false,
                    "Введіть валідний емейл"
                )

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

        retypePassword.addValidationTextWatcher(editPasswordRetype) { retypePassword ->
            when {
                retypePassword.isEmpty() -> Pair(false, "Пусте поле")
                retypePassword != passwordEnter.text.toString().trim() -> Pair(
                    false,
                    "Не відповідні паролі"
                )

                else -> Pair(true, "")
            }
        }

        alreadyRegistered.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


        buttonSignUp.setOnClickListener {
            val email = emailEnter.text.toString()
            val password = passwordEnter?.text.toString()
            signUpDatabase(email, password)
        }
    }

    private fun signUpDatabase(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Реєстрація успішна", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Унікальний email", Toast.LENGTH_SHORT).show()
                }

            }
    }

}