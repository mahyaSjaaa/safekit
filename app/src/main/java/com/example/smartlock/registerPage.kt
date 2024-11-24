package com.example.smartlock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class registerPage : AppCompatActivity() {

    private lateinit var uid:EditText
    private lateinit var  username:EditText
    private lateinit var password:EditText
    private lateinit var confPass:EditText
    private  lateinit var  registerSubmit : Button
    private lateinit var peringatan:TextView

    private fun initComponent(){
        uid = findViewById(R.id.editTextTextSignUpUID)
        username = findViewById(R.id.editTextTextSignUpUsername)
        password = findViewById(R.id.editTextTextSignUpPassword)
        confPass = findViewById(R.id.editTextTextSignUpConfirmPassword)
        registerSubmit = findViewById(R.id.btnSignup)
        peringatan = findViewById(R.id.alertReg)
    }

    private fun pindahHalaman(){
        Intent(this, login::class.java).also{
            startActivity(it)
            Toast.makeText(this, resources.getString(R.string.akunberhasil), Toast.LENGTH_SHORT).show()
        }
    }

    private fun register(uid:String, username:String, password:String, confPass:String){
        val database = Firebase.database
        val myRef = database.getReference(uid)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //if(snapshot.exists()){
                    Log.i("cekreg", uid)
                    if(password == confPass){
                        myRef.child(username).setValue(password)
                        pindahHalaman()
                    }else{
                        peringatan.text = resources.getString(R.string.passTidakCocokReg)
                    }
                //}else{
                    peringatan.text = resources.getString(R.string.uidusernameSalah)
               // }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("registerGagal", "onCancelled: ", error.toException())
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.registerpage)

        initComponent()

        registerSubmit.setOnClickListener {
            register(uid.text.toString(), username.text.toString(), password.text.toString(), confPass.text.toString())
        }

    }
}