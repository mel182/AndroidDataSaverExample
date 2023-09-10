package com.example.searchwithflowoperatorexample

data class Person(val firstName:String, val lastName:String) {

    fun doesMatchSearchQuery(query:String): Boolean {

        if (query.isBlank() || firstName.isBlank() || lastName.isBlank())
            return false

        val matchingCombinations = listOf(
            "$firstName$lastName",
            "$firstName $lastName",
            "${firstName.first()} ${lastName.first()}",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
