package com.example.kotlintaskproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlintaskproject.databinding.ActivityUpdatePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            updatePasswordButton.setOnClickListener {

                val existPassword = existPasswordEditText.text.toString()
                val newPassword = updatePasswordEditText.text.toString()

                if(existPassword.isBlank()){

                    Toast.makeText(this@UpdatePasswordActivity, "Please enter exist password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(newPassword.isBlank()){

                    Toast.makeText(this@UpdatePasswordActivity, "Please enter new password!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val currentUserEmail = auth.currentUser?.email

                if (currentUserEmail == null) {

                    Toast.makeText(this@UpdatePasswordActivity, "Invalid exist password!", Toast.LENGTH_SHORT).show()
                    finish()

                }else{

                    auth.signInWithEmailAndPassword(currentUserEmail, existPassword).addOnCompleteListener{ task ->

                        if (task.isSuccessful){

                            FirebaseAuth.getInstance().currentUser?.updatePassword(newPassword)?.addOnCompleteListener{

                                if (it.isSuccessful){

                                    Toast.makeText(this@UpdatePasswordActivity, "password updated", Toast.LENGTH_SHORT).show()
                                    finish()

                                } else {

                                    Toast.makeText(this@UpdatePasswordActivity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }

                        } else {

                            Toast.makeText(this@UpdatePasswordActivity, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}