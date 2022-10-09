package com.example.datasaverexampleapp.compose.tic_tac_toe_game

data class PlayerUpdateData(val currentPlayer: Player = Player.X, val gameState: Array<CharArray> = arrayOf()) {
    fun isEmpty() = this == PlayerUpdateData()
}
