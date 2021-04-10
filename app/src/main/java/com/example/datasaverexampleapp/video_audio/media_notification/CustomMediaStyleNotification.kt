package com.example.datasaverexampleapp.video_audio.media_notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.example.datasaverexampleapp.application.AppContext
import com.example.datasaverexampleapp.type_alias.AppColor
import com.example.datasaverexampleapp.type_alias.Drawable

class CustomMediaStyleNotification {

    companion object {

        @JvmStatic
        fun create(mediaSessionCompat: MediaSessionCompat, service : MediaBrowserServiceCompat) : NotificationCompat.Builder {

            val controller = mediaSessionCompat.controller
            val mediaMetaData = controller.metadata
            val description = mediaMetaData.description

            val builder = NotificationCompat.Builder(AppContext.appContext,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    createNotificationChannel(service.javaClass.simpleName,"foreground service")
                else ""
            )

            // Add description meta data from the media session
            return builder.apply {
//                setContentText(description.title?:"")
                setContentText("Text")
//                setContentText(description.subtitle?:"")
                setContentText("SubTitle")
//                setSubText(description.description?:"")
                setSubText("Sub text")
                setContentTitle("Bon vide - Jeon ft. Ataniro")

                description.iconBitmap?.let {
                    setLargeIcon(it)
                }

                setContentIntent(controller.sessionActivity)
                setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(service,PlaybackStateCompat.ACTION_STOP))
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                // Add branding from your app
                setSmallIcon(Drawable.ic_sensor_icon)
                color = ContextCompat.getColor(AppContext.appContext,AppColor.colorPrimary)

                // Add actions
                addAction(NotificationCompat.Action(Drawable.ic_pause_button, "pause", MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PAUSE)))
                addAction(NotificationCompat.Action(Drawable.ic_stop_button,"stop",MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_STOP)))

                // Add the MediaStyle
                setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0)
                    .setMediaSession(mediaSessionCompat.sessionToken)

                    // These two lines are only required if your minSdkVersion is < API 21
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_STOP)))
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(channelId:String, channelName:String) : String {

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }
            val service = AppContext.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)

            return channelId
        }
    }
}