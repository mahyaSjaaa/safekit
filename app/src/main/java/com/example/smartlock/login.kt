package com.example.smartlock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue


class login : AppCompatActivity() {
    private lateinit var uid:EditText
    private lateinit var username:EditText
    private lateinit var  password:EditText
    private lateinit var tombol:Button
    private lateinit var sayHello:TextView
    private lateinit var registerButt:Button

    private fun  initComponent(){
        uid = findViewById(R.id.editTextTextUID)
        username = findViewById(R.id.editTextTextUserName)
        password = findViewById(R.id.editTextTextPassword)
        tombol = findViewById(R.id.btnLogin)
        sayHello = findViewById(R.id.alert)
        registerButt = findViewById(R.id.butReg)
    }

    private fun pindahPage(){
        Intent(this, MainActivity2::class.java).also {
            startActivity(it)
        }
    }

    private fun pindahReg(){
        Intent(this, registerPage::class.java).also{
            startActivity(it)
        }
    }

    private fun loginFun(UID:String, username:String, passwordIn:String) {
        val database = Firebase.database
        val myRef = database.getReference("${UID}/${username}")
        val sharedPref = getSharedPreferences(resources.getString(R.string.keyNama), Context.MODE_PRIVATE)

        initComponent()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val password = snapshot.getValue<String>()
                    if(passwordIn == password){
                        with(sharedPref.edit()){
                            putString("UID", UID)
                            putString("username", username)
                            apply()
                        }
                        pindahPage()
                    }else{
                        sayHello.text = resources.getString(R.string.passwordSalah)
                    }
                }else{
                    sayHello.text = resources.getString(R.string.uidusernameSalah)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("errorLogin", "onCancelled: ", error.toException())
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        initComponent()

        tombol.setBackgroundColor(resources.getColor(R.color.tombol))
        registerButt.setBackgroundColor(resources.getColor(R.color.tombol))

        val sharedPref = getSharedPreferences(resources.getString(R.string.keyNama), Context.MODE_PRIVATE)
        val namaa = sharedPref.getString("username", "GUEST")
        sayHello.text = resources.getString(R.string.welcomeLogged, namaa)

        if(namaa != "GUEST"){
            pindahPage()
        }

        tombol.setOnClickListener {
            val UID = uid.text.toString()
            val username = username.text.toString()
            val password = password.text.toString()

            loginFun(UID, username, password)

            tombol.setBackgroundColor(resources.getColor(R.color.black, theme))
        }

        registerButt.setOnClickListener {
            pindahReg()
        }

    }
}