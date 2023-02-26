@file:OptIn(FlowPreview::class)

package com.example.searchwithflowoperatorexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class SearchViewModel: ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _persons = MutableStateFlow(allPersons)
    val persons = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true } }
        .combine(_persons) { text, persons ->
        if (text.isBlank()) {
            persons
        } else {
            delay(2000L) // This simulate a network call to a database
            persons.filter {
                it.doesMatchSearchQuery(text)
            }
        }
    }
    .onEach { _isSearching.update { false } }
    .stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _persons.value
    )

    fun onSearchTextChange(text:String) {
        _searchText.value = text
    }
}

private val allPersons = listOf(
    Person(firstName = "Jeff", lastName = "Bezos"),
    Person(firstName = "Melchior", lastName = "Vrolijk"),
    Person(firstName = "Steve", lastName = "Jobs"),
    Person(firstName = "Bill", lastName = "Gates"),
    Person(firstName = "Carlos", lastName = "Sainz"),
    Person(firstName = "Max", lastName = "Verstappen"),
)

