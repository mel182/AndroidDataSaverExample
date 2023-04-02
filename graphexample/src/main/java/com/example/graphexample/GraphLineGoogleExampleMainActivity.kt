package com.example.graphexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.graphexample.ui.theme.DataSaverExampleAppTheme

class GraphLineGoogleExampleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val dataList = listOf(0, 10, 40, 60, 20, 80, 90, 110)
                    LineGraphView(graphData = dataList)
                }
            }
        }
    }
}

@Preview
@Composable
fun LineGraphView(graphData: List<Int> = listOf(0, 10, 40, 60, 20, 80, 90, 110)) {

    val spacing = 100f
    val backgroundColor = Color(0xFF2C1941)
    val barLineColor = Color(0xFFBABABA)
    val animationProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = graphData, block = {
        animationProgress.animateTo(1f, tween(3000))
    })

    Box(modifier = Modifier
        .background(color = backgroundColor)
        .fillMaxSize()
    ) {

        Spacer(
            modifier = Modifier
                .padding(all = 8.dp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .drawWithCache {

                    val path = generatePath(data = graphData, size)
                    val filledPath = Path()
                    filledPath.addPath(path = path)
                    filledPath.lineTo(size.width, size.height)
                    filledPath.lineTo(0f, size.height)
                    filledPath.close()

                    val brush = Brush.verticalGradient(
                        listOf(
                            Color.Green.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    )

                    onDrawBehind {

                        val barWidthPx = 1.dp.toPx()
                        drawRect(color = barLineColor, style = Stroke(barWidthPx))

                        val verticalLines = 4
                        val verticalSize = size.width / (verticalLines + 1)

                        repeat(verticalLines) { i ->
                            val startX = verticalSize * (i + 1)
                            drawLine(
                                color = barLineColor,
                                start = Offset(startX, 0f),
                                end = Offset(startX, size.height),
                                strokeWidth = barWidthPx
                            )
                        }

                        val horizontalLines = 3
                        val sectionSize = size.height / (horizontalLines + 1)
                        repeat(horizontalLines) { i ->
                            val startY = sectionSize * (i + 1)
                            drawLine(
                                color = barLineColor,
                                start = Offset(0f, startY),
                                end = Offset(size.width, startY),
                                strokeWidth = barWidthPx
                            )
                        }

                        clipRect(right = size.width * animationProgress.value) {
                            drawPath(path = path, Color.Green, style = Stroke(2.dp.toPx()))
                            drawPath(
                                filledPath,
                                brush = brush,
                                style = Fill
                            )
                        }
                    }
                }
        )
    }
}

private fun generatePath(data: List<Int>, size: Size): Path {

    val path = Path()

    val maxValue = data.maxOrNull() ?: 0

    val dataSectionWidth = size.width / (data.size - 1)
    val dataSectionHeight = size.height / maxValue

    path.moveTo(x = 0f, y = size.height)

    data.forEachIndexed { index, balance ->
        val x = index * dataSectionWidth
        val y = if (balance == 0) size.height else size.height - (dataSectionHeight * balance)

        path.lineTo(x,y)
    }

    return path
}