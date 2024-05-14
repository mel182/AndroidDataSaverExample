package com.example.jetpackcomposedraggableslider

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.jetpackcomposedraggableslider.ui.theme.DataSaverExampleAppTheme

class JetpackComposeDraggableSliderMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataSaverExampleAppTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF191C1E)),
                    verticalArrangement = Arrangement.Center
                ) {

                    val viewHeight = (LocalConfiguration.current.screenWidthDp.dp - 90.dp) / 2

                    Log.i("TAG12", "view height: $viewHeight")

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        DraggableSlider(modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.Center)
                            .padding(horizontal = 45.dp)
                            .fillMaxHeight(),
                            stroke = 40f,
                            onChange = {
                                Log.i("TAG12", "on change: $it")
                            }
                        )

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 140.dp)
                        ) {

                            val (leftText, rightText) = createRefs()

                            Text(
                                text = "16°",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(45.dp)
                                    .constrainAs(leftText) {
                                        start.linkTo(parent.start)
                                        bottom.linkTo(parent.bottom)
                                        width = Dimension.fillToConstraints
                                    },
                                color = Color.White
                            )


                            Text(
                                text = "32°",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(45.dp)
                                    .constrainAs(rightText) {
                                        end.linkTo(parent.end)
                                        bottom.linkTo(parent.bottom)
                                        width = Dimension.fillToConstraints
                                    },
                                color = Color.White
                            )
                        }



                    }


//                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
//
//                        val (leftText, centerSlider, rightText) = createRefs()
//
//                        Text(
//                            text = "16°",
//                            modifier = Modifier.constrainAs(leftText) {
//                                      start.linkTo(parent.start)
//                                bottom.linkTo(parent.bottom)
//                                end.linkTo(centerSlider.start)
//                                width = Dimension.fillToConstraints
//                            },
//                            color = Color.White)
//
//
//
//                        Text(
//                            text = "32°",
//                            modifier = Modifier.constrainAs(rightText) {
//                                start.linkTo(centerSlider.end)
//                                bottom.linkTo(parent.bottom)
//                                end.linkTo(parent.end)
//                                width = Dimension.fillToConstraints
//                            },
//                            color = Color.White)
//                    }
                }
            }
        }
    }
}