package com.example.datasaverexampleapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Drawable
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity() {

    private val MESSAGE_CHANNEL = "messages"
    private var notificationManager:NotificationManagerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        title = "Notification Example"
        notificationManager = NotificationManagerCompat.from(applicationContext)

        simple_notification_button?.setOnClickListener {

            // On Android 8.0 or higher devices, rather than providing Notification settings within your app,
            // should redirect users to the system Notification settings screen.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, baseContext.packageName)
                startActivity(intent)
            }

            createMessageNotificationChannel()
            val NEW_MESSAGE_ID = 1
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Simple notification"
            val content = "This is a simple notification"

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)

            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        notification_background_color_button?.setOnClickListener {

            // On Android 8.0 or higher devices, rather than providing Notification settings within your app,
            // should redirect users to the system Notification settings screen.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, baseContext.packageName)
                startActivity(intent)
            }

            createMessageNotificationChannel()
            val NEW_MESSAGE_ID = 1
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Simple notification"
            val content = "This is a simple notification"

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color

            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        notification_large_icon_button?.setOnClickListener {

            // On Android 8.0 or higher devices, rather than providing Notification settings within your app,
            // should redirect users to the system Notification settings screen.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, baseContext.packageName)
                startActivity(intent)
            }

            createMessageNotificationChannel()
            val NEW_MESSAGE_ID = 1
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Large icon notification"
            val content = "This is a notification with large icon"

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color

            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }



    }

    private fun createMessageNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val name = "Simple notification"
            val channel = NotificationChannel(
                MESSAGE_CHANNEL,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }


    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(applicationContext, drawableId)

        return drawable?.let { convertedDrawable ->

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = DrawableCompat.wrap(convertedDrawable).mutate()
            }
            val bitmap = Bitmap.createBitmap(
                convertedDrawable.intrinsicWidth,
                convertedDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            convertedDrawable.setBounds(0, 0, canvas.width, canvas.height)
            convertedDrawable.draw(canvas)
            return bitmap
        }
    }

}