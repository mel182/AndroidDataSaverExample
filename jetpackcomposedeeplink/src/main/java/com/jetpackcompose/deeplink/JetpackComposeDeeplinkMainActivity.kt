package com.jetpackcompose.deeplink

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.jetpackcompose.deeplink.ui.theme.DataSaverExampleAppTheme

class JetpackComposeDeeplinkMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = "1"
            val descriptionText = "Test channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("1", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "home") {

                        composable("home") {
                            val context = LocalContext.current
                            Column(modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Button(onClick = {
                                    val notificationID = 134
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("app://datasaver/12")
                                    )
                                    val activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                                    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                                    notificationManager.notify(notificationID,
                                        NotificationCompat.Builder(context,"1")
                                            .setContentTitle("Navigation in Jetpack Compose")
                                            .setContentText("Everything we need to know")
                                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            .setContentIntent(activity)
                                            .build()
                                        )
                                }) {
                                    Text(text = "Test Notification")
                                }

                                Button(onClick = {
                                    navController.navigate("detail")
                                }) {
                                    Text(text = "Go to detail")
                                }
                            }
                        }

                        composable(route = "detail",
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern = "app://datasaver/{id}"
                                    action = Intent.ACTION_VIEW
                                }
                            ),
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { entry ->
                            val id = entry.arguments?.getInt("id")
                            Box(modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "The ID is $id")
                            }
                        }
                    }
                }
            }
        }
    }
}