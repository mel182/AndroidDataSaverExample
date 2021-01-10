package com.example.datasaverexampleapp.content_provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.core.os.CancellationSignal
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.datasaverexampleapp.room_db.DatabaseAccessor
import com.example.datasaverexampleapp.room_db.RoomDBViewModel
import com.example.datasaverexampleapp.room_db.UserDAO
import com.example.datasaverexampleapp.room_db.UserDatabase

class TestContentProvider : ContentProvider()
{
    companion object{
        private val AUTHORITY = "com.test.provider.datasaverexampleapp"
        private val ALLROWS = 1
        private val SINGLE_ROW = 2
        private val DELETE_ROW = 3
        private val uriMatcher:UriMatcher

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(AUTHORITY,"all", ALLROWS)
            uriMatcher.addURI(AUTHORITY,"user", SINGLE_ROW)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        // Perform a query and return Cursor

        if(uriMatcher.match(uri) == ALLROWS)
        {
            return DatabaseAccessor.dataAccessObject?.loadAllUsersCursor()
        } else if (uriMatcher.match(uri) == SINGLE_ROW)
        {
            //getUserByID
            selectionArgs?.let {

                try {
                    val id = it[0].toInt()
                    return DatabaseAccessor.dataAccessObject?.getUserByID(id)
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
        cancellationSignal: android.os.CancellationSignal?
    ): Cursor? {
        // Perform a query and return Cursor
        if(uriMatcher.match(uri) == ALLROWS)
        {
            return DatabaseAccessor.dataAccessObject?.loadAllUsersCursor()
        } else if (uriMatcher.match(uri) == SINGLE_ROW)
        {
            selectionArgs?.let {

                try {
                    val id = it[0].toInt()
                    return DatabaseAccessor.dataAccessObject?.getUserByID(id)
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }

        return null
    }

    override fun getType(uri: Uri): String {
        // Return the mime-type of a query
        return when(uriMatcher.match(uri))
        {
            ALLROWS -> "vnd.android.cursor.dir/vnd.datasaverexampleapp.users"
            SINGLE_ROW -> "vnd.android.cursor.item/vnd.datasaverexampleapp.users"
            else -> ""
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Insert the content values and return a URI to the record
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        // Delete the matching records and return the number of records deleted
        selectionArgs?.let {

            try {
                val id = it[0].toInt()
                DatabaseAccessor.dataAccessObject?.deleteUsersByID(id)
                return id
            }catch (e:Exception)
            {
                e.printStackTrace()
            }
        }

        return -1
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        // Update the matching records with the provided content values, returning the number of records updated
        return 0
    }
}

// private fun <T> LiveData<T>.observe(testContentProvider: ContentProvider, observer: Observer<T>) { }

//            RoomDBViewModel().getAllUsers().observe(this, Observer { result ->
//
//                val cursor =
//            })