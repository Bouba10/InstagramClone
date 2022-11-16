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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signin_link_btn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        signeup_btn.setOnClickListener{
            createAccount()
        }
    }

    private fun createAccount() {
        val fullName=fullname_signup.text.toString()
        val userName=username_signup.text.toString()
        val email=email_signup.text.toString()
        val password=password_signup.text.toString()

        when{
            TextUtils.isEmpty(fullName) ->  Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(userName) ->  Toast.makeText(this, " username is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(email) ->  Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(password) ->  Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()

            else ->{
                val progressDialog=ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage(("Plese wait ,this may take a while"))
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                //val mAuth :FirebaseAuth=FirebaseAuth.getInstance()
                val mAuth :FirebaseAuth= Firebase.auth

                mAuth.createUserWithEmailAndPassword(email,password)

                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            saveInfo(fullName,userName,email,progressDialog)

                        }else{
                            val message=task.exception.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                            mAuth.signOut()
                            progressDialog.dismiss()

                        }
                    }

            }
        }
    }

    private fun saveInfo(fullName: String, userName: String, email: String,progressDialog: ProgressDialog) {

         val currentUserID=FirebaseAuth.getInstance().currentUser!!.uid
         val usersRef:DatabaseReference=FirebaseDatabase.getInstance().reference.child("Users")
         val userMap=HashMap<String,Any>()

        userMap["uid"]=currentUserID
        userMap["fullname"]=fullName
        userMap["username"]=userName
        userMap["email"]=email
        userMap["bio"]="Heho ich benutze  Instagram clone"
        userMap["image"]="https://firebasestorage.googleapis.com/v0/b/instagram-clone-ca923.appspot.com/o/Default%20Images%2Fprofile.png?alt=media&token=4e6b6b67-a69b-424d-894a-b7e1e2bbc282"

        progressDialog.dismiss()
        Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_SHORT).show()

        val intent=Intent(this@SignUpActivity,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)



    }
}