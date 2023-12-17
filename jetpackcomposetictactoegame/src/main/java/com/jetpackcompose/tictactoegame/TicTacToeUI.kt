package com.jetpackcompose.tictactoegame

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TicTacToeGameUI(isResetting: MutableState<Boolean>, isGameRunning: MutableState<Boolean>, onPLayerWin: (Player) -> Unit = {}, onDraw: () -> Unit = {}, currentPlayer: (Player) -> Unit = {}) {

    val scope = rememberCoroutineScope()

    var currentPlayer by remember {
        mutableStateOf<Player>(Player.X)
    }
    var gameState by remember {
        mutableStateOf(emptyGameState())
    }
    var animations = remember {
        emptyAnimations()
    }
    var drawWinnerLine = remember {
        Winner.NONE
    }

    Canvas(modifier = Modifier
        .size(300.dp)
        .border(color = Color.Black, width = 1.dp)
        .padding(10.dp)
        .pointerInput(key1 = true) {
            detectTapGestures {

                if (!isGameRunning.value)
                    return@detectTapGestures

                val sectionClicked = it.asCanvasArea(canvasSize = size)

                if (sectionClicked != Canvas1DCoordinate.None) {
                    val result = gameState.updatePlayerState(
                        coordinate = sectionClicked,
                        currentPlayer = currentPlayer,
                        animations = animations,
                        coroutineScope = scope
                    )
                    if (!result.isEmpty()) {
                        gameState = result.gameState
                        currentPlayer = result.currentPlayer
                        currentPlayer(currentPlayer)
                    }
                }

                val isFieldFull = gameState.all { row ->
                    row.all { it != 'E' }
                }

                val didXWin = didPlayerWin(gameState = gameState, player = Player.X)

                val didOWin = didPlayerWin(gameState = gameState, player = Player.O)

                if (didXWin != Winner.NONE) {
                    onPLayerWin(Player.X)
                    drawWinnerLine = didXWin
                } else if (didOWin != Winner.NONE) {
                    onPLayerWin(Player.O)
                    drawWinnerLine = didOWin
                } else if (isFieldFull) {
                    onDraw()
                    drawWinnerLine = Winner.NONE
                }
                if (isFieldFull || didXWin != Winner.NONE || didOWin != Winner.NONE) {
                    scope.launch {
                        isGameRunning.value = false
                        isResetting.value = !isGameRunning.value
                        delay(3000L)
                        isGameRunning.value = true
                        isResetting.value = !isGameRunning.value
                        gameState = emptyGameState()
                        animations = emptyAnimations()
                        drawWinnerLine = Winner.NONE
                        //onNewRound()
                    }
                }
            }
        }) {
        drawLine(
            color = Color.Black,
            start = Offset(size.width / 3f, 0f),
            end = Offset(size.width / 3f, size.height),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Black,
            start = Offset(2 * size.width / 3f, 0f),
            end = Offset(2 * size.width / 3f, size.height),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Black,
            start = Offset(0f, size.width / 3f),
            end = Offset(size.height, size.width / 3f),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Black,
            start = Offset(0f, 2 * size.width / 3f),
            end = Offset(size.height, 2 * size.width / 3f),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )
        gameState.forEachIndexed{ columnIndex, row ->

            row.forEachIndexed{ rowIndex, symbol ->

                when(symbol) {

                    Player.X.symbol -> {

                        val playerXPath1 = Path().apply {
                            moveTo(
                                x = columnIndex * size.width / 3f + size.width / 6f - 50f,
                                y = rowIndex * size.height / 3f + size.height / 6f - 50f
                            )
                            lineTo(
                                x = columnIndex * size.width / 3f + size.width / 6f + 50f,
                                y = rowIndex * size.height / 3f + size.height / 6f + 50f
                            )
                        }

                        val playerXPath2 = Path().apply {
                            moveTo(
                                x = columnIndex * size.width / 3f + size.width / 6f - 50f,
                                y = rowIndex * size.height / 3f + size.height / 6f + 50f
                            )
                            lineTo(
                                x = columnIndex * size.width / 3f + size.width / 6f + 50f,
                                y = rowIndex * size.height / 3f + size.height / 6f - 50f
                            )
                        }

                        val pathOutput1 = Path()
                        PathMeasure().apply {
                            setPath(playerXPath1, false)
                            getSegment(0f, animations[columnIndex][rowIndex].value * length, pathOutput1)
                        }

                        val pathOutput2 = Path()
                        PathMeasure().apply {
                            setPath(playerXPath2, false)
                            getSegment(0f, animations[columnIndex][rowIndex].value * length, pathOutput2)
                        }

                        drawPath(
                            path = pathOutput1,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )

                        drawPath(
                            path = pathOutput2,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }

                    Player.O.symbol -> {

                        drawArc(
                            color = Color.Green,
                            startAngle = 0f,
                            sweepAngle = animations[columnIndex][rowIndex].value * 360f,
                            useCenter = false,
                            topLeft = Offset(
                                x= columnIndex * size.width / 3f + size.width / 6f - 50f,
                                y= rowIndex * size.height / 3f + size.height / 6f - 50f),
                            size = Size(width = 100f, height = 100f),
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            }
        }

        if (drawWinnerLine != Winner.NONE) {

            val singleBlockSize = Size(
                width = size.width / 3f,
                height = size.height / 3f)

            when(drawWinnerLine) {

                Winner.FIRST_ROW_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, singleBlockSize.height / 2f),
                        end = Offset(size.width, singleBlockSize.height / 2f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.SECOND_ROW_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, singleBlockSize.height + (singleBlockSize.height / 2f)),
                        end = Offset(size.width, singleBlockSize.height + (singleBlockSize.height / 2f)),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.THIRD_ROW_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, size.height - (singleBlockSize.height / 2f)),
                        end = Offset(size.width, size.height - (singleBlockSize.height / 2f)),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.FIRST_COLUMN_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(singleBlockSize.width / 2f, 0f),
                        end = Offset(singleBlockSize.width / 2f, size.height),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.SECOND_COLUMN_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(singleBlockSize.width + (singleBlockSize.width / 2f), 0f),
                        end = Offset(singleBlockSize.width + (singleBlockSize.width / 2f), size.height),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.THIRD_COLUMN_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset( size.width - (singleBlockSize.width / 2f), 0f),
                        end = Offset(size.width - (singleBlockSize.width / 2f), size.height),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.FIRST_DIAGONAL_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset( 0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Winner.SECOND_DIAGONAL_FULL -> {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset( size.width, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                else -> { }
            }
        }
    }
}

private fun emptyAnimations(): ArrayList<ArrayList<Animatable<Float, AnimationVector1D>>> {

    val arrayList = arrayListOf<ArrayList<Animatable<Float, AnimationVector1D>>>()
    for (itemOneIndex in 0..2) {
        arrayList.add(arrayListOf())
        for (itemTwoIndex in 0..2){
            arrayList[itemOneIndex].add(Animatable(0f))
        }
    }
    return arrayList
}

private fun emptyGameState(): Array<CharArray> = arrayOf(
    charArrayOf('E','E','E'),
    charArrayOf('E','E','E'),
    charArrayOf('E','E','E')
)

private fun updateGameState(gameState: Array<CharArray>, itemIndex: Int, vector1DIndex:Int, symbol: Char): Array<CharArray>{
    val arrayCopy = gameState.copyOf()
    arrayCopy[itemIndex][vector1DIndex] = symbol
    return arrayCopy
}

private fun Array<CharArray>.updatePlayerState(coordinate:Canvas1DCoordinate, currentPlayer: Player, animations:ArrayList<ArrayList<Animatable<Float, AnimationVector1D>>>, coroutineScope: CoroutineScope) : PlayerUpdateData {

    var result = PlayerUpdateData()
    if (this[coordinate.index1][coordinate.index2] == 'E') {
        val newGameState = updateGameState(gameState = this, coordinate.index1, coordinate.index2, currentPlayer.symbol)
        coroutineScope.animateFloatOne(animatable = animations[coordinate.index1][coordinate.index2])
        val updatedCurrentPlayer = !currentPlayer
        result = PlayerUpdateData(currentPlayer = updatedCurrentPlayer, gameState = newGameState)
    }

    return result
}

private fun Offset.asCanvasArea(canvasSize: IntSize) : Canvas1DCoordinate {

    val singleBlockSize = Size(
        width = canvasSize.width / 3f,
        height = canvasSize.height / 3f)

    return when {

        x < singleBlockSize.width && y < singleBlockSize.height -> Canvas1DCoordinate.TopLeft
        x in singleBlockSize.width..(2 * singleBlockSize.width) && y < singleBlockSize.height -> Canvas1DCoordinate.TopMiddle
        x > 2 * singleBlockSize.width && y < singleBlockSize.height -> Canvas1DCoordinate.TopRight
        x < singleBlockSize.width && y in singleBlockSize.height..(2 * singleBlockSize.height) -> Canvas1DCoordinate.MiddleLeft
        x in singleBlockSize.width..(2 * singleBlockSize.width) && y in singleBlockSize.height..(2 * singleBlockSize.height) -> Canvas1DCoordinate.MiddleMiddle
        x > 2 * singleBlockSize.width && y in singleBlockSize.height..(2 * singleBlockSize.height) -> Canvas1DCoordinate.MiddleRight
        x < singleBlockSize.width && y > 2 * singleBlockSize.height -> Canvas1DCoordinate.BottomLeft
        x in singleBlockSize.width..(2 * singleBlockSize.width) && y > 2 * singleBlockSize.height -> Canvas1DCoordinate.BottomMiddle
        x > 2 * singleBlockSize.width && y > 2 * singleBlockSize.height -> Canvas1DCoordinate.BottomRight
        else -> Canvas1DCoordinate.None
    }
}

private fun CoroutineScope.animateFloatOne(animatable: Animatable<Float, AnimationVector1D>) {
    launch {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500
            )
        )
    }
}

private fun didPlayerWin(gameState: Array<CharArray>, player: Player): Winner {

    var result = Winner.NONE

    val firstRowFull = gameState[0][0] == gameState[1][0] &&
            gameState[1][0] == gameState[2][0] && gameState[0][0] == player.symbol

    if (firstRowFull) {
        result = Winner.FIRST_ROW_FULL
    }

    val secondRowFull = gameState[0][1] == gameState[1][1] &&
            gameState[1][1] == gameState[2][1] && gameState[0][1] == player.symbol
    if (secondRowFull) {
        result = Winner.SECOND_ROW_FULL
    }


    val thirdRowFull = gameState[0][2] == gameState[1][2] &&
            gameState[1][2] == gameState[2][2] && gameState[0][2] == player.symbol
    if (thirdRowFull) {
        result = Winner.THIRD_ROW_FULL
    }

    val firstColumnFull = gameState[0][0] == gameState[0][1] &&
            gameState[0][1] == gameState[0][2] && gameState[0][0] == player.symbol
    if (firstColumnFull) {
        result = Winner.FIRST_COLUMN_FULL
    }

    val secondColumnFull = gameState[1][0] == gameState[1][1] &&
            gameState[1][1] == gameState[1][2] && gameState[1][0] == player.symbol
    if (secondColumnFull) {
        result = Winner.SECOND_COLUMN_FULL
    }

    val thirdColumnFull = gameState[2][0] == gameState[2][1] &&
            gameState[2][1] == gameState[2][2] && gameState[2][0] == player.symbol
    if (thirdColumnFull) {
        result = Winner.THIRD_COLUMN_FULL
    }

    val firstDiagonalFull = gameState[0][0] == gameState[1][1] &&
            gameState[1][1] == gameState[2][2] && gameState[0][0] == player.symbol

    if (firstDiagonalFull) {
        result = Winner.FIRST_DIAGONAL_FULL
    }

    val secondDiagonalFull = gameState[0][2] == gameState[1][1] &&
            gameState[1][1] == gameState[2][0] && gameState[0][2] == player.symbol

    if (secondDiagonalFull) {
        result = Winner.SECOND_DIAGONAL_FULL
    }

    return result
}