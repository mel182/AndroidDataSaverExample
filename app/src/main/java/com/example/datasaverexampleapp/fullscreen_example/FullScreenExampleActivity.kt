@file:Suppress("UNNECESSARY_SAFE_CALL", "UNUSED_ANONYMOUS_PARAMETER")

package com.example.datasaverexampleapp.fullscreen_example

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsets.Type.navigationBars
import android.view.WindowInsets.Type.statusBars
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_full_screen_example.*

@Suppress("DEPRECATION")
class FullScreenExampleActivity : AppCompatActivity() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_example)
        title = "Full screen example"

        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = Color.WHITE
        }


        // Android 4.4 Kit Kat (API level 19) added the ability to provide truly immersive experience
        // even when the user is interacting with your Activity. This comes with the addition of two
        // additional flags:
        //
        // SYSTEM_UI_FLAG_IMMERSIVE: In immersive mode, users can interact with the Activity,
        //                           requiring that they swipe down the top edge of the screen
        //                           in order to reveal the hidden system UI and exit immersive
        //                           mode. This is appropriate for book or news reader, where users
        //                           will need to touch the Activity to scroll or change pages.
        //
        // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: Similar to immersive mode, the user can fully interact
        //                                  with the Activity. However, swiping down only temporary reveals
        //                                  the system UI, before it automatically hides again. This
        //                                  is appropriate for a game or drawing app that expects
        //                                  infrequent use of the system UI.
        //
        // When using only these flags, the position of your Views will be adjusted whenever the system UI
        // is hidden or shown. To stabilize your UI, you can use the additional flags of SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // , SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and SYSTEM_UI_FLAG_LAYOUT_STABLE to request that the Activity always be laid
        // out as if the system UI is always hidden.


        hide_system_ui_button?.setOnClickListener {

            window?.apply {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                    setDecorFitsSystemWindows(false)
                    statusBarColor = Color.TRANSPARENT
                    insetsController?.let { controller ->
                        controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        navigationBarColor = Color.TRANSPARENT
                        controller.hide(statusBars())
                    }
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        }

        hide_system_ui_immersive_button?.setOnClickListener {

            // Hide the navigation bar, status bar and use IMMERSIVE
            // Note the usage of the _LAYOUT flags to keep a stable layout
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or // Hide nav bar
                        View.SYSTEM_UI_FLAG_FULLSCREEN or // Hide status bar
                        View.SYSTEM_UI_FLAG_IMMERSIVE
            }
        }

        hide_system_ui_immersive_sticky_button?.setOnClickListener {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // immersive sticky
            }
        }

        show_system_ui_button?.setOnClickListener {

            window?.apply {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    setDecorFitsSystemWindows(true)
                    statusBarColor = Color.BLACK
                    insetsController?.let { controller ->
                        controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        navigationBarColor = Color.BLACK
                        controller.show(statusBars())
                    }
                } else {
                    decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN
                }
            }
        }

        window?.decorView?.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

                setOnApplyWindowInsetsListener { view, windowInsets ->

                    if (windowInsets.isVisible(statusBars()))
                    {
                        Toast.makeText(this@FullScreenExampleActivity, "Display Action Bar and Status bar",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@FullScreenExampleActivity, "Hide Action Bar and Status bar",Toast.LENGTH_SHORT).show()
                    }

                    windowInsets
                }

            } else {
                // You may choose to hide and display the App Bar and other navigational controls based
                // on entering and exiting full screen mode.
                // You can do this by registering an 'OnSystemUiVisibilityChangeListener' to a View - typically
                // the view you're using the navigation visibility.
                // Note: The system UI flags are reset whenever the user leaves (and subsequently) your app.
                //       As a result the timing of calls to set these flags is important to ensure the UI is
                //       always in the state you expect. It's recommended that you set and reset any system UI flags
                //       within the 'onResume' and 'onWindowFocusChanged' handler.
                window?.decorView?.setOnSystemUiVisibilityChangeListener { visibility ->

                    if (visibility == View.SYSTEM_UI_FLAG_VISIBLE)
                    {
                        Toast.makeText(this@FullScreenExampleActivity, "Display Action Bar and Status bar",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@FullScreenExampleActivity, "Hide Action Bar and Status bar",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}