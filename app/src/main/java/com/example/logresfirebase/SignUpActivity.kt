package com.example.logresfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var ferUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mAuth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            signUpUser()
        }

    }

    private fun signUpUser() {
        val username: String = et_username_signUp.text.toString()
        val email: String = et_email_signUp.text.toString()
        val password: String = et_password_signUp.text.toString()

        if (username == "") {
            Toast.makeText(this, getString(R.string.text_message_username), Toast.LENGTH_SHORT)
                .show()
        } else if (email == "") {
            Toast.makeText(this, getString((R.string.text_message_email)), Toast.LENGTH_SHORT)
                .show()
        } else if (password == "") {
            Toast.makeText(this, getString((R.string.text_message_password)), Toast.LENGTH_SHORT)
                .show()
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    firebaseUserID = mAuth.currentUser!!.uid
                    ferUsers = FirebaseDatabase.getInstance()
                        .reference.child("Users").child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username

                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()

                    ferUsers.updateChildren(userHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()

                        }
                    }

                } else {
                    Toast.makeText(this, "Error Message : " + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
