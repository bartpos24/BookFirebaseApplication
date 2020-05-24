package com.example.firebaseapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebaseapp.MainActivity
import com.example.firebaseapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        signUpButton = findViewById(R.id.signUp_button)
        loginButton = findViewById(R.id.button)
        emailEditText = findViewById(R.id.email_editText)
        passwordEditText = findViewById(R.id.password_editText)


        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        loginButton.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {

        if (emailEditText.text.toString().isEmpty()) {
            emailEditText.error = "Please enter a email"
            emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
            emailEditText.error = "Please enter valid email"
            emailEditText.requestFocus()
            return
        }
        if (passwordEditText.text.toString().isEmpty()) {
            passwordEditText.error = "Please enter a password"
            passwordEditText.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        baseContext, "Login failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(baseContext, "Please verify your email address", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(baseContext, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}
