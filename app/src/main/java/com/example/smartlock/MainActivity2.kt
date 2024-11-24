package com.example.smartlock

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
//import com.example.smartlock.databinding.ActivityMain2Binding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import coil3.transform.RoundedCornersTransformation


class MainActivity2 : AppCompatActivity() {

    private lateinit var welcome:TextView
    private lateinit var  viewImageButt:Button
    private lateinit var logoutButton: ImageButton
    private lateinit var  lockButton: Button
    private lateinit var isiPesan: EditText
    private lateinit var sendBut:ImageButton
    //private  lateinit var binding: ActivityMain2Binding

    private fun initComponent(){
        welcome = findViewById(R.id.textView4)
        viewImageButt = findViewById(R.id.button4)
        logoutButton = findViewById(R.id.imageButton3)
        lockButton = findViewById(R.id.button2)
        isiPesan = findViewById(R.id.editTextText)
        sendBut = findViewById(R.id.imageButton)
    }

//    //private fun downloadImage(nama: String) = CoroutineScope(Dispatchers.IO).launch {
//        val storage =FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//        //val gsReference = storage.getReferenceFromUrl("gs://espcam-e3bb4.appspot.com/qrwtEQTf12/busject3_1729773268860.jpg")
//        try {
//            val maxDownload = 5L * 1024 * 1024
//            val pathReference = storageRef.child("${nama}/busject3_1729773268860.jpg").getBytes(maxDownload).await()
//            val bitmap = BitmapFactory.decodeByteArray(pathReference, 0, pathReference.size)
//            withContext(Dispatchers.Main){
//                binding.imageViewHome.load(bitmap){
//                    crossfade(true)
//                    crossfade(500)
//                    transformations(RoundedCornersTransformation(15f))
//                }
//            }
//        }catch (e: Exception){
//            Log.w("gambarerror", "downloadImage: ${e.message}" )
//        }
//    }

//    private fun pindahPagePic(){
//        Intent(this, picPage::class.java).also{
//            startActivity(it)
//        }
//    }

    private fun pindahPageLogin(){
        Intent(this, login::class.java).also {
            startActivity(it)
        }
    }

    private fun logOut(){
        val sharedPref = getSharedPreferences(resources.getString(R.string.keyNama), Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        pindahPageLogin()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mainpage)

       // binding = ActivityMain2Binding.inflate(layoutInflater)
        //setContentView(binding.root)

        initComponent()

        val sharedPref = getSharedPreferences(resources.getString(R.string.keyNama), Context.MODE_PRIVATE)
        val nama = sharedPref.getString("UID", null)
        val nrp = sharedPref.getString("username", null)

        val database = Firebase.database

        val myRef = database.getReference("${nama}/buttonState")
        val kirimPesan = database.getReference("${nama}/pesan")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val state = snapshot.getValue<String>().toString()
                if(state == "0"){
                    lockButton.text = "unlocked"
                }else{
                    lockButton.text = "locked"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        welcome.text = resources.getString(R.string.welcomeLogged, nrp)

//        viewImageButt.setOnClickListener {
//            pindahPagePic()
//        }

        logoutButton.setOnClickListener {
            logOut()
        }

        lockButton.setOnClickListener {
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<String>()
                    if(value != "1"){
                        myRef.setValue("1")
                        lockButton.text = "locked"
                    }else if(value == "1"){
                        myRef.setValue("0")
                        lockButton.text = "unlocked"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("errorQuery", "onCancelled: ", error.toException())
                }
            })
        }

        sendBut.setOnClickListener {
                kirimPesan.setValue(isiPesan.text.toString())
                Toast.makeText(this, resources.getString(R.string.pesanterkirim), Toast.LENGTH_SHORT).show()
        }





    }
}