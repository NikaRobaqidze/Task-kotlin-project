package com.example.kotlintaskproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.kotlintaskproject.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding

    private val auth = Firebase.auth
    private val auth2 = FirebaseAuth.getInstance()

    private val db = FirebaseDatabase.getInstance().getReference("User")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        binding.apply {

            db.child(auth.currentUser!!.uid).addValueEventListener(
                object : ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userInfo = snapshot.getValue(User::class.java) ?: return
                        Glide.with(this@ProfileActivity).load(userInfo.link).into(imageView)
                        emailTextView.text = userInfo.email
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ProfileActivity, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            changePhotoButton.setOnClickListener {

                val uid = auth.uid.toString()
                val link = linkEditText.text.toString()
                val email = auth2.currentUser?.email.toString()

                val userInfo = User(uid,email, link)

                db.child(uid).setValue(userInfo)

                Glide.with(this@ProfileActivity).load(link).into(imageView)
            }

            signOutButton.setOnClickListener {
                Firebase.auth.signOut()
                startActivity(Intent(this@ProfileActivity,LoginActivity::class.java))
                finish()
            }

            profileUpdatePasswordButton.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,UpdatePasswordActivity::class.java))
            }
        }
    }
}