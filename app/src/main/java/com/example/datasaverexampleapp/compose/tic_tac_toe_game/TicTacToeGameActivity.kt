package com.example.datasaverexampleapp.compose.tic_tac_toe_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class TicTacToeGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Tic tac toe game (compose path)"
        setContent {
            TicTacToeGameUI()
        }
    }
}