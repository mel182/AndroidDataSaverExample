package com.jetpackcompose.appshortcut

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.jetpackcompose.appshortcut.ui.theme.DataSaverExampleAppTheme

class JetpackComposeAppShortCutMainActivity : ComponentActivity() {

    private val viewModel: JetpackComposeAppShortCutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        setContent {
            DataSaverExampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        when(viewModel.shortcutType) {
                            ShortcutType.STATIC -> Text(text = "Static shortcut clicked")
                            ShortcutType.DYNAMIC -> Text(text = "Dynamic shortcut clicked")
                            ShortcutType.PINNED -> Text(text = "Pinned shortcut clicked")
                            null -> Unit
                        }

                        Button(
                            onClick = ::addDynamicShortcut
                        ) {
                            Text(text = "Add dynamic shortcut")
                        }
                    }
                }
            }
        }
    }

    private fun addDynamicShortcut() {
        val shortcut =
            ShortcutInfoCompat.Builder(applicationContext, "dynamic")
                .setShortLabel("Short label")
                .setLongLabel("This is a long label")
                .setIcon(
                    IconCompat.createWithResource(
                        applicationContext,
                        R.drawable.airport_shuttle
                    )
                ).setIntent(
                    Intent(applicationContext, JetpackComposeAppShortCutMainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        putExtra("shortcut_id","dynamic")
                    }
                ).build()
        ShortcutManagerCompat.pushDynamicShortcut(applicationContext, shortcut)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            when(intent.getStringExtra("shortcut_id")) {
                "static" -> viewModel.onShortcutClicked(ShortcutType.STATIC)
                "dynamic" -> viewModel.onShortcutClicked(ShortcutType.DYNAMIC)
                "pinned" -> viewModel.onShortcutClicked(ShortcutType.PINNED)
            }
        }
    }
}