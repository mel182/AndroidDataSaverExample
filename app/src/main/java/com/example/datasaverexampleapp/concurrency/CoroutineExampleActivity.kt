package com.example.datasaverexampleapp.concurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_coroutine_example.*
import kotlinx.coroutines.*

class CoroutineExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_example)
        this.title = "Coroutine Example"

        start_button?.setOnClickListener {

            status_textView.text = "Calculating...."

            CoroutineScope(Dispatchers.IO).launch {

                runBlocking {

                    // -------------- execute task, wait for the first one to finish and plot result and move to the next one ------------- \\
//                    delay(1000)

//                    val deferred1 = async { calculateThings(10) }.await()
//                    launch(Dispatchers.Main) {
//                        status_textView.text = "Result1: ${deferred1}"
//                    }
//
//                    delay(2000)
//                    val deferred2 = async { calculateThings(20) }.await()
//                    launch(Dispatchers.Main) {
//                        status_textView.text = "Result 2: ${deferred2}"
//                    }

                    // -------------- wait until all calculation is done and plot the result ------------- \\
                    val deferred3 = async { calculateThings(30) }
                    val deferred4 = async { calculateThings(40) }

                    val sum = deferred3.await() + deferred4.await()

                    launch(Dispatchers.Main) {
                        status_textView.text = "Sum result: ${sum}"
                    }
                }
            }
        }
    }

    //region Calculate things
    suspend fun calculateThings(number:Int) : Int
    {
        delay(1000) // Coroutines delay
        return number * 10
    }
//endregion

}