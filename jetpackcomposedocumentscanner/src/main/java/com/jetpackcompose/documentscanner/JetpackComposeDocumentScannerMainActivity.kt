package com.jetpackcompose.documentscanner

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.jetpackcompose.documentscanner.ui.theme.DataSaverExampleAppTheme
import java.io.File
import java.io.FileOutputStream

/**
 * This is an example of the Google Machine learning dependency for scanning document
 */
class JetpackComposeDocumentScannerMainActivity : ComponentActivity() {

    private val documentScannerOptions = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setPageLimit(5)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .build()
    private val scanner = GmsDocumentScanning.getClient(documentScannerOptions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
                    var imageUris by remember {
                        mutableStateOf<List<Uri>>(emptyList())
                    }
                    val scannerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = {
                            if (it.resultCode == RESULT_OK) {
                                val result = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                                imageUris = result?.pages?.map { it.imageUri } ?: emptyList()

                                result?.pdf?.let { pdf ->
                                    val fileOutputStream = FileOutputStream(File(filesDir,"scan.pdf"))
                                    contentResolver.openInputStream(pdf.uri)?.use {
                                        it.copyTo(fileOutputStream)
                                    }
                                }
                            }
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        imageUris.forEach { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Button(onClick = {
                            scanner.getStartScanIntent(this@JetpackComposeDocumentScannerMainActivity)
                                .addOnSuccessListener {
                                    scannerLauncher.launch(
                                        IntentSenderRequest.Builder(it).build()
                                    )
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@JetpackComposeDocumentScannerMainActivity,
                                        "Exception: ${it.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                        }) {
                            Text(text = "Scan document")
                        }
                    }
                }
            }
        }
    }
}