package com.jetpackcompose.tictactoegame

data class PlayerUpdateData(val currentPlayer: Player = Player.X, val gameState: Array<CharArray> = arrayOf()) {
    fun isEmpty() = this == PlayerUpdateData()
}
