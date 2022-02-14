package com.example.datasaverexampleapp.appbar.searchview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import com.example.datasaverexampleapp.R

class SearchViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_view)
        title = "Search view Example"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_view_menu,menu)

        val searchItem = menu.findItem(R.id.searchview_item)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnSearchClickListener {

        }

        return true
    }
}