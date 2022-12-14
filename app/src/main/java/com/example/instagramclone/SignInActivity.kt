package com.example.instagramclone

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignInActivity : AppCompatActivity() {
    private  var mAuth : FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signeup_link_btn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        signin_btn.setOnClickListener{
            loginUser()
        }
    }

    private fun loginUser() {
        val email=email_login.text.toString()
        val password=password_login.text.toString()

        when{
            TextUtils.isEmpty(email) ->  Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) ->  Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()

            else -> {
                val progressDialog= ProgressDialog(this)
                progressDialog.setTitle("Login")
                progressDialog.setMessage(("Plese wait ,this may take a while"))
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                           progressDialog.dismiss()
                            reload()

                        } else {
                            val message=task.exception.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                            mAuth.signOut()
                            progressDialog.dismiss()


                        }
                    }

            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
           reload();
        }
    }

    private fun reload() {
        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}