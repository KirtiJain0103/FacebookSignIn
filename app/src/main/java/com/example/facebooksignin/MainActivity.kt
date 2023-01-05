package com.example.facebooksignin

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.facebooksignin.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


  private lateinit var callbackManager : CallbackManager

  lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FacebookSdk.fullyInitialize()

        callbackManager = CallbackManager.Factory.create()

        binding.btnLogIn.setPermissions( listOf("email","public_profile"))

        auth = FirebaseAuth.getInstance()

        binding.btnLogIn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLogIn.visibility = View.GONE
               val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential).addOnCompleteListener(this@MainActivity) {
                    if(it.isSuccessful){
                        binding.progressBar.visibility = View.GONE
                        val intent = Intent(this@MainActivity, UserDetails::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            override fun onCancel() {
            }
            override fun onError(error: FacebookException) {
            }
        }
        )

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode,resultCode,data)
    }

    override fun onResume() {
        super.onResume()
        if(auth.currentUser!=null){
            val intent = Intent(this@MainActivity, UserDetails::class.java)
            startActivity(intent)
            finish()
        }
    }


}