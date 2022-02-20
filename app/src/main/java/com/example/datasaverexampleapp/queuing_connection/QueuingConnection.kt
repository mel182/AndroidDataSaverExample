@file:Suppress("UNUSED_VARIABLE")

package com.example.datasaverexampleapp.queuing_connection

import java.util.*

class QueuingConnection {

    private val requests: Queue<ConnectionRequest> = LinkedList()

    fun addRequest(request:ConnectionRequest)
    {
        requests.add(request)

        val headQueue = requests.peek() //Get head queue of the list
        requests.remove() // Remove the head queue from the list
    }

    fun execute()
    {
        // Iteration over the queue for execution
    }
}