package com.example.datasaverexampleapp.preference_datastore

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.datasaverexampleapp.databinding.ActivityPreferenceDataStoreBinding
import com.example.datasaverexampleapp.type_alias.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreferenceDataStoreActivity : AppCompatActivity(Layout.activity_preference_data_store) {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "ds_test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Preference data store Example"
        DataBindingUtil.setContentView<ActivityPreferenceDataStoreBinding>(
            this, Layout.activity_preference_data_store
        ).apply {

            keyInput.setOnEditorActionListener { _, actionID, _ ->

                if (actionID == EditorInfo.IME_ACTION_DONE)
                    readButton.performClick()

                false
            }

            valueInput.setOnEditorActionListener { _, actionID, _ ->

                if (actionID == EditorInfo.IME_ACTION_DONE)
                    saveButton.performClick()

                false
            }

            saveButton.setOnClickListener {
                saveButton.isEnabled = false
                keyInput.clearFocus()
                valueInput.clearFocus()
                hideKeyword()
                if (keyInput.text.toString().isNotBlank() && valueInput.text.toString().isNotBlank())
                {
                    CoroutineScope(Dispatchers.Default).launch {
                        dataStore.edit { preferences ->
                            val key = stringPreferencesKey(keyInput.text.toString())
                            val value = valueInput.text.toString()
                            preferences[key] = value
                        }

                        withContext(Dispatchers.Main) {
                            keyInput.text.clear()
                            valueInput.text.clear()
                            saveButton.isEnabled = true
                            Toast.makeText(this@PreferenceDataStoreActivity,"preference saved!",Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    saveButton.isEnabled = true
                }
            }

            readKeyInput.setOnEditorActionListener { textView, actionID, keyEvent ->

                if (actionID == EditorInfo.IME_ACTION_DONE)
                    readButton.performClick()

                false
            }

            readButton.setOnClickListener {
                saveButton.isEnabled = false
                readKeyInput.clearFocus()
                hideKeyword()
                if (readKeyInput.text.toString().isNotBlank())
                {
                    val targetKey = stringPreferencesKey(readKeyInput.text.toString())
                    val result : Flow<String> = dataStore.data.map { preferences ->
                        preferences[targetKey] ?: ""
                    }

                    CoroutineScope(Dispatchers.Main).launch {
                        result.collect { value ->
                            readKeyResult.text = value.ifBlank { "Not found!" }
                            saveButton.isEnabled = true
                        }
                    }
                } else {
                    saveButton.isEnabled = true
                }
            }

            removeKeyInput.setOnEditorActionListener { textView, actionID, keyEvent ->

                if (actionID == EditorInfo.IME_ACTION_DONE)
                    removeButton.performClick()

                false
            }

            removeButton.setOnClickListener {
                saveButton.isEnabled = false
                removeKeyInput.clearFocus()
                hideKeyword()
                if (removeKeyInput.text.toString().trim().isNotBlank())
                {
                    CoroutineScope(Dispatchers.Default).launch {
                        dataStore.edit { preferences ->
                            val key = stringPreferencesKey(removeKeyInput.text.toString())
                            preferences.remove(key)
                        }

                        withContext(Dispatchers.Main) {
                            saveButton.isEnabled = true
                            Toast.makeText(this@PreferenceDataStoreActivity,"key removed!",Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    saveButton.isEnabled = true
                }
            }
        }
    }

    private fun hideKeyword()
    {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken,0)
        }
    }
}