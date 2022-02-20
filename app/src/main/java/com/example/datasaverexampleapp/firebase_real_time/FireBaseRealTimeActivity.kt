package com.example.datasaverexampleapp.firebase_real_time

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityFireBaseRealTimeBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FireBaseRealTimeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_real_time)

        DataBindingUtil.setContentView<ActivityFireBaseRealTimeBinding>(
            this, Layout.activity_fire_base_real_time
        ).apply {
            val database = Firebase.database
            val myRef = database.getReference("unique1")

            myRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val value = snapshot.getValue<String>()
                    value?.let {
                        firebaseText.text = value
                    }?: kotlin.run {

                        val storageRef = database.reference
                        val uniqueRef = storageRef.child("unique1")
                        uniqueRef.setValue("Initial value")
                        firebaseText.text = "Initial value"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    firebaseText.text = "Database error: ${error.message}"
                }
            })

            submitButton.setOnClickListener {

                val newValue = textInput.text.toString()
                myRef.setValue(newValue)
            }
        }
    }
}