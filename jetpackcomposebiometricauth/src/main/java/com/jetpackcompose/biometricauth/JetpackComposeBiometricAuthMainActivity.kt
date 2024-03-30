package com.jetpackcompose.biometricauth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jetpackcompose.biometricauth.BiometricResult.AuthenticationError
import com.jetpackcompose.biometricauth.BiometricResult.AuthenticationFailed
import com.jetpackcompose.biometricauth.BiometricResult.AuthenticationNotSet
import com.jetpackcompose.biometricauth.BiometricResult.AuthenticationSuccess
import com.jetpackcompose.biometricauth.BiometricResult.FeatureUnavailable
import com.jetpackcompose.biometricauth.BiometricResult.HardwareUnavailable
import com.jetpackcompose.biometricauth.ui.theme.DataSaverExampleAppTheme

/**
 * This example demonstrate how to use the biometric authentication
 * on Android using Jetpack compose
 */
class JetpackComposeBiometricAuthMainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val biometricResult by promptManager.promptResults.collectAsState(
                        initial = null
                    )
                    val enrollLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult(),
                        onResult = {
                            println("Activity result: $it")
                        }
                    )
                    LaunchedEffect(key1 = biometricResult) {
                        if (biometricResult is AuthenticationNotSet) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                    putExtra(
                                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                                    )
                                }
                                enrollLauncher.launch(enrollIntent)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            promptManager.showBiometricPrompt(
                                title = "Sample prompt title",
                                description = "Sample prompt description"
                            )
                        }) {
                            Text(text = "Authenticate")
                        }
                    }

                    biometricResult?.let { result ->
                        Text(
                            text = when(result) {
                                is AuthenticationError -> {
                                    "Authentication error: ${result.error}"
                                }
                                AuthenticationFailed -> {
                                    "Authentication failed"
                                }
                                AuthenticationNotSet -> {
                                    "Authentication not set"
                                }
                                AuthenticationSuccess -> {
                                    "Authentication success"
                                }
                                FeatureUnavailable -> {
                                    "Feature unavailable"
                                }
                                HardwareUnavailable -> {
                                    "Hardware unavailable"
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}