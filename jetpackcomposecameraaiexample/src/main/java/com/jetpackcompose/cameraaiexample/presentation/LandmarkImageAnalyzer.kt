package com.jetpackcompose.cameraaiexample.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.jetpackcompose.cameraaiexample.domain.Classification
import com.jetpackcompose.cameraaiexample.domain.LandmarkClassifier

class LandmarkImageAnalyzer(
    private val classifier: LandmarkClassifier,
    private val onResult: (List<Classification>) -> Unit
): ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {

        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321, 321)

            val results = classifier.classify(bitmap, rotationDegrees)
            onResult(results)
        }
        frameSkipCounter++

        image.close()
    }

}