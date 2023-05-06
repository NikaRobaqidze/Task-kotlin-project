package com.example.kotlintaskproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlintaskproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        binding.apply {

            signUpButton.setOnClickListener {
                val email = signUpEmailEditText.text.toString()
                val password = signUpPasswordEditText.text.toString()

                if (email.isEmpty() || password.isEmpty() || password.length < 5 || password.contains(
                        ' '
                    )
                ) {
                    Toast.makeText(this@SignUpActivity, "error", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, "${task.exception?.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            bottomNav.setOnItemSelectedListener{

                when(it.itemId){

                    R.id.signUpAlreadyRegisteredButton -> {

                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()

                        it.setChecked(true)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}