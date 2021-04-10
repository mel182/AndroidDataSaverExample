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
        fun create(mediaSessionCompat: MediaSessionCompat, service : MediaBrowserServiceCompat) : Notification{

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
                setContentText(description.title?:"")
                setContentText(description.subtitle?:"")
                setSubText(description.description?:"")

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
                addAction(NotificationCompat.Action(Drawable.ic_pause_button, "pause", MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_PLAY_PAUSE)))
                addAction(NotificationCompat.Action(Drawable.skip_next_icon,"Skip",MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)))

                // Add the MediaStyle
                setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0)
                    .setMediaSession(mediaSessionCompat.sessionToken)

                    // These two lines are only required if your minSdkVersion is < API 21
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(service, PlaybackStateCompat.ACTION_STOP)))
            }.build()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(channelId:String, channelName:String) : String {

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }
            val service = AppContext.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)

            return channelId
        }
    }
}