package com.example.datasaverexampleapp.concurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_flow_example.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FlowExampleActivity : AppCompatActivity() {


    private lateinit var fixedFlow: Flow<Int>
    private lateinit var collectionFlow: Flow<Int>
    private lateinit var lambdaFlow: Flow<Int>
    private lateinit var channelFlow: Flow<Int>
    private val list = listOf<Int>(1,2,3,4,5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_example)
        setupFixedFlow()
        setupCollectionFlow()
        setupLambdaFlow()
        setupChannelFlowWithLambda()

        fixed_flow_button.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val buttonText = fixed_flow_button.text
                fixedFlow.collect { item ->
                    fixed_flow_textView.text = item.toString()
                }
                fixed_flow_textView.text = "done"
                delay(1000)
                fixed_flow_textView.text = buttonText
            }
        }

        collection_flow.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val buttonText = collection_flow.text
                collectionFlow.collect { item ->
                    collection_flow_textView.text = item.toString()
                }
                collection_flow_textView.text = "done"
                delay(1000)
                collection_flow_textView.text = buttonText
            }
        }

        lambda_flow.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val buttonText = lambda_flow_textView.text
                lambdaFlow.collect { item ->
                    collection_flow_textView.text = item.toString()
                }
                lambda_flow_textView.text = "done"
                delay(1000)
                lambda_flow_textView.text = buttonText
            }
        }

        lambda_flow.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val buttonText = lambda_flow_textView.text
                lambdaFlow.collect { item ->
                    lambda_flow_textView.text = item.toString()
                }
                lambda_flow_textView.text = "done"
                delay(1000)
                lambda_flow_textView.text = buttonText
            }
        }

        channel_flow.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val buttonText = channel_flow_textView.text
                channelFlow.collect { item ->
                    channel_flow_textView.text = item.toString()
                }
                channel_flow_textView.text = "done"
                delay(1000)
                channel_flow_textView.text = buttonText
            }
        }


        zip_normal.setOnClickListener {

            val customAdapter = FlowZipNormalListAdapter()

            flow_zip_result_list?.apply {
                layoutManager = LinearLayoutManager(this@FlowExampleActivity)
                adapter = customAdapter
            }

            val numbers = (1..3).asFlow()
            val str = flowOf("one","two","three")

            runBlocking {
                numbers.zip(str) { a,b -> "$a -> $b"}.collect {
                    customAdapter.updateList(it)
                }
            }
        }


    }

    private fun setupFixedFlow()
    {
        // Setup fixed flow
        fixedFlow = flowOf(1,2,3,4,5).onEach {
            // Set delay of 300 millisecond on each item
            delay(300)
        }
    }

    private fun setupCollectionFlow()
    {
        // Setup collection flow
        collectionFlow = list.asFlow().onEach { delay(300) }
    }

    private fun setupLambdaFlow()
    {
        // Setup lambda flow
        lambdaFlow = flow {
            list.forEach {
                delay(300)
                emit(it+2)
            }
        }
    }

    private fun setupChannelFlowWithLambda()
    {
        // Setup channel flow with lambda
        channelFlow = channelFlow {
            list.forEach {
                delay(300)
                send(it+3)
            }
        }
    }

}