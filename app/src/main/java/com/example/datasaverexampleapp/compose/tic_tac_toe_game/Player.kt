package com.example.datasaverexampleapp.compose.tic_tac_toe_game

sealed class Player(val symbol: Char) {
    object X : Player('X')
    object O : Player('O')

    operator fun not(): Player {
        return if (this is X) O else X
    }
}
