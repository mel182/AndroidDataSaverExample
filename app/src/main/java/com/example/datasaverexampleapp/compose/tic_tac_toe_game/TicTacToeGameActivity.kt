package com.example.datasaverexampleapp.compose.tic_tac_toe_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

class TicTacToeGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Tic tac toe game (compose path)"
        setContent {

            val isGameRunning = remember {
                mutableStateOf(true)
            }

            val resetting = remember {
                mutableStateOf(false)
            }

            var currentPlayer by remember {
                mutableStateOf<Player>(Player.X)
            }

            var playerXPoint by remember {
                mutableStateOf(0)
            }

            var playerOPoint by remember {
                mutableStateOf(0)
            }

            if (!isGameRunning.value) {
                currentPlayer = Player.X
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tic tac toe game", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 23.sp, modifier = Modifier.fillMaxWidth())
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {

                    Column {
                        Text(text = "Player X: ", textAlign = TextAlign.Center, fontSize = 16.sp)
                        Row {
                            Text(text = "$playerXPoint ", textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = "point", textAlign = TextAlign.Center, fontSize = 24.sp)
                        }
                    }

                    Column {
                        Text(text = "Player O: ", textAlign = TextAlign.Center, fontSize = 16.sp)
                        Row {
                            Text(text = "$playerOPoint ", textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(text = "point", textAlign = TextAlign.Center, fontSize = 24.sp)
                        }
                    }
                }
                TicTacToeGameUI(isResetting = resetting, isGameRunning = isGameRunning, currentPlayer = {
                    currentPlayer = it
                }, onPLayerWin = {
                    if (it == Player.X) {
                        playerXPoint++
                        Toast.makeText(this@TicTacToeGameActivity,"Player X wins", Toast.LENGTH_SHORT).show()
                    } else {
                        playerOPoint++
                        Toast.makeText(this@TicTacToeGameActivity,"Player O wins", Toast.LENGTH_SHORT).show()
                    }
                }, onDraw = {
                    Toast.makeText(this@TicTacToeGameActivity,"Draw", Toast.LENGTH_SHORT).show()
                })

                val alpha: Float by animateFloatAsState(
                    targetValue = if (isGameRunning.value) 1f else 0.0f,
                    animationSpec = tween(durationMillis = 2000)
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha)) {
                    Text(text = "Now playing:" , modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp)
                    Text(text = if (currentPlayer == Player.X) Player.X.symbol.toString() else Player.O.symbol.toString() , modifier = Modifier.fillMaxWidth(),
                        color = if (currentPlayer == Player.X) Color.Red else Color.Green,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp)
                }

                Text(text = if (isGameRunning.value) {
                    "running"
                } else if (resetting.value) {
                    "starting new round....."
                } else {
                    ""
                }, color = if (isGameRunning.value) {
                    Color(0xFF349645)
                } else if (resetting.value) {
                    Color.Red
                } else {
                    Color.Black
                }, modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp)

                Button(onClick = {
                    playerXPoint = 0
                    playerOPoint = 0
                }) {
                    Text(text = "Reset points")
                }
            }
        }
    }
}