package com.example.datasaverexampleapp.firebase_real_time

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_fire_base_real_time.*

class FireBaseRealTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_real_time)

        val database = Firebase.database
        val myRef = database.getReference("unique1")

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val value = snapshot.getValue<String>()
                value?.let {
                    firebase_text?.text = value
                }?: kotlin.run {

                    val storageRef = database.reference
                    val uniqueRef = storageRef.child("unique1")
                    uniqueRef.setValue("Initial value")
                    firebase_text?.text = "Initial value"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                firebase_text?.text = "Database error: ${error.message}"
            }
        })

        submit_button?.setOnClickListener {

            val newValue = text_input?.text.toString()
            myRef.setValue(newValue)
        }
    }
}