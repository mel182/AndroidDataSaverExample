package com.jetpackcompose.cameraaiexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jetpackcompose.cameraaiexample.data.TfLiteLandmarkClassifier
import com.jetpackcompose.cameraaiexample.domain.Classification
import com.jetpackcompose.cameraaiexample.presentation.CameraPreview
import com.jetpackcompose.cameraaiexample.presentation.LandmarkImageAnalyzer
import com.jetpackcompose.cameraaiexample.ui.theme.DataSaverExampleAppTheme

class CameraAIMainActivity : ComponentActivity() {

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {

                if (!hasRequiredPermissions()) {
                    ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS,12)
                    return@DataSaverExampleAppTheme
                }

                var classifications by remember {
                    mutableStateOf(emptyList<Classification>())
                }
                val analyzer = remember {
                    LandmarkImageAnalyzer(
                        classifier = TfLiteLandmarkClassifier(
                            context = applicationContext
                        ),
                        onResult = {
                            classifications = it
                        }
                    )

                }
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(applicationContext),
                            analyzer
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    ) {
                        classifications.forEach {
                            Text(text = it.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = Color.LightGray)
                                    .padding(all = 8.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }

                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}