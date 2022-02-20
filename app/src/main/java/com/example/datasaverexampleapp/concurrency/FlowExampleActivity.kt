@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.example.datasaverexampleapp.concurrency

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityFlowExampleBinding
import com.example.datasaverexampleapp.type_alias.Layout
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

        DataBindingUtil.setContentView<ActivityFlowExampleBinding>(
            this, Layout.activity_flow_example
        ).apply {

            fixedFlowButton.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val buttonText = fixedFlowButton.text
                    fixedFlow.collect { item ->
                        fixedFlowTextView.text = item.toString()
                    }
                    fixedFlowTextView.text = "done"
                    delay(1000)
                    fixedFlowTextView.text = buttonText
                }
            }

            collectionFlow.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val buttonText = collectionFlow.text
                    this@FlowExampleActivity.collectionFlow.collect { item ->
                        collectionFlowTextView.text = item.toString()
                    }
                    collectionFlowTextView.text = "done"
                    delay(1000)
                    collectionFlowTextView.text = buttonText
                }
            }

            lambdaFlow.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val buttonText = lambdaFlowTextView.text
                    this@FlowExampleActivity.lambdaFlow.collect { item ->
                        collectionFlowTextView.text = item.toString()
                    }
                    lambdaFlowTextView.text = "done"
                    delay(1000)
                    lambdaFlowTextView.text = buttonText
                }
            }

            lambdaFlow.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val buttonText = lambdaFlowTextView.text
                    this@FlowExampleActivity.lambdaFlow.collect { item ->
                        lambdaFlowTextView.text = item.toString()
                    }
                    lambdaFlowTextView.text = "done"
                    delay(1000)
                    lambdaFlowTextView.text = buttonText
                }
            }

            channelFlow.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val buttonText = channelFlowTextView.text
                    this@FlowExampleActivity.channelFlow.collect { item ->
                        channelFlowTextView.text = item.toString()
                    }
                    channelFlowTextView.text = "done"
                    delay(1000)
                    channelFlowTextView.text = buttonText
                }
            }

            zipNormal.setOnClickListener {

                val customAdapter = FlowZipNormalListAdapter()

                flowZipResultList.apply {
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

            zipWhenOneCompletesBeforeOne.setOnClickListener {

                val customAdapter = FlowZipNormalListAdapter()

                flowZipBeforeOneList.apply {
                    layoutManager = LinearLayoutManager(this@FlowExampleActivity)
                    adapter = customAdapter
                }

                val numbers = (1..3).asFlow()
                val stringValue = flowOf("one","two","three","four")

                runBlocking {
                    numbers.zip(stringValue) { a,b -> "$a -> $b" }.collect {
                        customAdapter.updateList(it)
                    }
                }
            }

            zipWhenOneEmitsAfterSomeDelay.setOnClickListener {

                val customAdapter = FlowZipNormalListAdapter()

                zipWhenOneEmitsAfterSomeDelayList.apply {
                    layoutManager = LinearLayoutManager(this@FlowExampleActivity)
                    adapter = customAdapter
                }

                val number = (1..3).asFlow().onEach { delay(300) }
                val values = flowOf("one","two","three").onEach { delay(400) }

                runBlocking {

                    number.zip(values){ a,b -> "$a -> $b" }
                        .collect {
                            customAdapter.updateList(it)
                        }
                }
            }

            combineWhenOneEmitsAfterSomeDelay.setOnClickListener {

                val customAdapter = FlowZipNormalListAdapter()

                combineWhenOneEmitsAfterSomeDelayList.apply {
                    layoutManager = LinearLayoutManager(this@FlowExampleActivity)
                    adapter = customAdapter
                }

                val numbers = (1..3).asFlow().onEach { delay(300) }
                val values = flowOf ("one","two","three").onEach { delay(400) }

                CoroutineScope(Dispatchers.Main).launch {
                    numbers.combine(values){ a,b -> "$a -> $b" }.collect {
                        customAdapter.updateList(it)
                    }
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

    @ExperimentalCoroutinesApi
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