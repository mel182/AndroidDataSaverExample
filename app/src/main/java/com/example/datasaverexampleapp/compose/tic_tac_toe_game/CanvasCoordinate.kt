package com.example.datasaverexampleapp.compose.tic_tac_toe_game

sealed class Canvas1DCoordinate(val index1:Int, val index2:Int) {
    object TopLeft: Canvas1DCoordinate(index1 = 0, index2 = 0)
    object TopMiddle: Canvas1DCoordinate(index1 = 1, index2 = 0)
    object TopRight: Canvas1DCoordinate(index1 = 2, index2 = 0)
    object MiddleLeft: Canvas1DCoordinate(index1 = 0, index2 = 1)
    object MiddleMiddle: Canvas1DCoordinate(index1 = 1, index2 = 1)
    object MiddleRight: Canvas1DCoordinate(index1 = 2, index2 = 1)
    object BottomLeft: Canvas1DCoordinate(index1 = 0, index2 = 2)
    object BottomMiddle: Canvas1DCoordinate(index1 = 1, index2 = 2)
    object BottomRight: Canvas1DCoordinate(index1 = 2, index2 = 2)
    object None: Canvas1DCoordinate(index1 = -1, index2 = -1)
}