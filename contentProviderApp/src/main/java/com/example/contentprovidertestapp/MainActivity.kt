package com.example.contentprovidertestapp

import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.contentprovidertestapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {

            launchButton.setOnClickListener{
                Toast.makeText(this@MainActivity,"test textview clicked!",Toast.LENGTH_SHORT).show()

                CoroutineScope(Dispatchers.IO).launch{

                    val cursor = contentResolver.query(Uri.parse("content://com.test.provider.datasaverexampleapp/all"),null,null,null,null)

                    cursor?.apply {

                        while (moveToNext())
                        {
                            Log.i("TAG","ID: ${getInt(0)}")
                            Log.i("TAG","Name: ${getString(1)}")
                            Log.i("TAG","Last name: ${getString(2)}")
                            Log.i("TAG","Age: ${getInt(3)}")

                        }

                        close()
                    }
                }
            }


            getUserButton.setOnClickListener {

                val userID = userIdInput.text.toString()

                if (userID.isNotBlank())
                {
                    CoroutineScope(Dispatchers.IO).launch{

                        val cursor = contentResolver.query(Uri.parse("content://com.test.provider.datasaverexampleapp/user"),null,"id",
                            arrayOf(userID),null)

                        ///$userID
                        cursor?.apply {

                            while (moveToNext())
                            {
                                Log.i("TAG","User found with ID: $userID")
                                Log.i("TAG","ID: ${getInt(0)}")
                                Log.i("TAG","Name: ${getString(1)}")
                                Log.i("TAG","Last name: ${getString(2)}")
                                Log.i("TAG","Age: ${getInt(3)}")
                            }

                            close()
                        }
                    }
                }
            }


            deleteUserButton.setOnClickListener {

                val userID = deleteUserIdInput.text.toString()

                if (userID.isNotBlank())
                {
                    CoroutineScope(Dispatchers.IO).launch{

                        val result = contentResolver.delete(Uri.parse("content://com.test.provider.datasaverexampleapp/user"),"id",arrayOf(userID))

                        CoroutineScope(Dispatchers.Main).launch {

                            if (result != -1)
                            {
                                Toast.makeText(this@MainActivity,"User deleted!",Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity,"Failed to delete user",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            insertUserButton.setOnClickListener {

                val name = userNameInput.text.toString()
                val lastname = userLastNameInput.text.toString()
                val age = userAgeInput.text.toString()

                if (name.isNotBlank() && lastname.isNotBlank() && age.isNotBlank())
                {
                    try {
                        val ageInt = age.toInt()

                        CoroutineScope(Dispatchers.IO).launch{

                            val contentValues = ContentValues()
                            contentValues.put("name",name)
                            contentValues.put("lastname",lastname)
                            contentValues.put("age",ageInt)

                            val result = contentResolver.insert(Uri.parse("content://com.test.provider.datasaverexampleapp/user"),contentValues)

                            CoroutineScope(Dispatchers.Main).launch {

                                if (result != null)
                                {
                                    Toast.makeText(this@MainActivity,"User inserted!",Toast.LENGTH_SHORT).show()
                                    Log.i("TAG","Uri: $result")
                                } else {
                                    Toast.makeText(this@MainActivity,"Failed to inserting user",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }catch (e:Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }


            updateUserButton.setOnClickListener {

                val name = updateUserNameInput.text.toString()
                val lastname = updateUserLastNameInput.text.toString()
                val age = updateUserAgeInput.text.toString()

                if (name.isNotBlank())
                {
                    CoroutineScope(Dispatchers.IO).launch{

                        val contentValues = ContentValues()
                        val ageInt: Int?

                        if (age.isNotBlank())
                        {
                            try {
                                ageInt = age.toInt()
                                contentValues.put("age",ageInt)
                            }catch (e:Exception)
                            {
                                e.printStackTrace()
                            }
                        }

                        if (lastname.isNotBlank())
                            contentValues.put("lastname",lastname)

                        val result = contentResolver.update(Uri.parse("content://com.test.provider.datasaverexampleapp/user/update"),contentValues,name,null)

                        CoroutineScope(Dispatchers.Main).launch {

                            if (result != -1)
                            {
                                Toast.makeText(this@MainActivity,"User Updated!",Toast.LENGTH_SHORT).show()
                                Log.i("TAG","ID: $result")
                            } else {
                                Toast.makeText(this@MainActivity,"Failed updating user",Toast.LENGTH_SHORT).show()
                            }
                        }


//                    val contentValues = ContentValues()
//                    contentValues.put("name",name)
//                    contentValues.put("lastname",lastname)
//                    contentValues.put("age",ageInt)
//
//                    val result = contentResolver.insert(Uri.parse("content://com.test.provider.datasaverexampleapp/user"),contentValues)
//
//                    CoroutineScope(Dispatchers.Main).launch {
//
//                        if (result != null)
//                        {
//                            Toast.makeText(this@MainActivity,"User inserted!",Toast.LENGTH_SHORT).show()
//                            Log.i("TAG","Uri: $result")
//                        } else {
//                            Toast.makeText(this@MainActivity,"Failed to inserting user",Toast.LENGTH_SHORT).show()
//                        }
//                    }
                    }
                }

//            if (name.isNotBlank() && lastname.isNotBlank() && age.isNotBlank())
//            {
//                try {
//                    val ageInt = age.toInt()
//
//                    CoroutineScope(Dispatchers.IO).launch{
//
//                        val contentValues = ContentValues()
//                        contentValues.put("name",name)
//                        contentValues.put("lastname",lastname)
//                        contentValues.put("age",ageInt)
//
//                        val result = contentResolver.insert(Uri.parse("content://com.test.provider.datasaverexampleapp/user"),contentValues)
//
//                        CoroutineScope(Dispatchers.Main).launch {
//
//                            if (result != null)
//                            {
//                                Toast.makeText(this@MainActivity,"User inserted!",Toast.LENGTH_SHORT).show()
//                                Log.i("TAG","Uri: $result")
//                            } else {
//                                Toast.makeText(this@MainActivity,"Failed to inserting user",Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//
//                }catch (e:Exception)
//                {
//                    e.printStackTrace()
//                }
//            }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
            }
        }
    }
}