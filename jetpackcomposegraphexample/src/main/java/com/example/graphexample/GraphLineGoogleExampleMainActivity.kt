package com.example.graphexample


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (graph, y_axis_numbers) = createRefs()

            /*
            Surface(modifier = Modifier
                .constrainAs(y_axis_numbers) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(graph.start)
                    height = Dimension.fillToConstraints
                }
            ) {

                Keyboard.Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = backgroundColor)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        AndroidView(factory = { context ->

                            VerticalTextView(context).apply {
                                text = "Left text"
                                setTextColor(Color.WHITE)
                            }
                        })
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "110", fontSize = 12.sp)
                        Text(text = "84", fontSize = 12.sp)
                        Text(text = "56", fontSize = 12.sp)
                        Text(text = "28", fontSize = 12.sp)
                        Text(text = "0", fontSize = 12.sp)
                    }
                }
            }
            */

            Spacer(modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(graph) {
                    top.linkTo(parent.top)
                    start.linkTo(y_axis_numbers.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
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

@Preview
@Composable
fun Main() {
    Column(
        modifier = Modifier
            .width(200.dp)

    ) {
        MyText(text = "Financial Advice")
        MyText(text = "Strategy and Marketing")
        MyText(text = "Information Technology")
    }
}

@Composable
fun MyText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.secondary)
            .padding(16.dp),
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}