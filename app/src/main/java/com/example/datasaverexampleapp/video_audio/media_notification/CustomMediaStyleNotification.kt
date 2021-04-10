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

                // PlaybackStateCompat options:
                // - ACTION_STOP :  Indicates this session supports the stop command (setActions(long))
                // - ACTION_PAUSE: Indicates this session supports the pause command (setActions(long))
                // - ACTION_PLAY: Indicates this session supports the play command (setActions(long))
                // - ACTION_REWIND: Indicates this session supports the rewind command (setActions(long))
                // - ACTION_SKIP_TO_PREVIOUS: Indicates this session supports the previous command (setActions(long))
                // - ACTION_SKIP_TO_NEXT: Indicates this session supports the next command (setActions(long))
                // - ACTION_FAST_FORWARD: Indicates this session supports the forward command (setActions(long))
                // - ACTION_SET_RATING: Indicates this session supports the rating command (setActions(long))
                // - ACTION_SEEK_TO: Indicates this session supports the seek command (setActions(long))
                // - ACTION_PLAY_PAUSE: Indicates this session supports the play/pause toggle command (setActions(long))
                // - ACTION_PLAY_FROM_MEDIA_ID: Indicates this session supports the play from media id command (setActions(long))
                // - ACTION_PLAY_FROM_SEARCH: Indicates this session supports play from search command (setActions(long))
                // - ACTION_SKIP_TO_QUEUE_ITEM: Indicates this session supports the skip to queue item command (setActions(long))
                // - ACTION_PLAY_FROM_URI: Indicates this session supports the play from URI command (setActions(long))
                // - ACTION_PREPARE: Indicates this session supports the prepare command (setActions(long))
                // - ACTION_PREPARE_FROM_MEDIA_ID: Indicates this session supports the prepare from media id command (setActions(long))
                // - ACTION_PREPARE_FROM_SEARCH: Indicates this session supports the prepare from search command (setActions(long))
                // - ACTION_PREPARE_FROM_URI: Indicates this session supports the prepare from URI command (setActions(long))
                // - ACTION_SET_REPEAT_MODE: Indicates this session supports the set repeat mode command (setActions(long))
                // - ACTION_SET_SHUFFLE_MODE_ENABLED: Indicates this session supports the set repeat mode command (setActions(long)) (deprecated: Use {@link #ACTION_SET_SHUFFLE_MODE} instead)
                // - ACTION_SET_CAPTIONING_ENABLED: Indicates this session supports the set captioning enabled command (setActions(long))
                // - ACTION_SET_SHUFFLE_MODE: Indicates this session supports the set shuffle mode command (setActions(long))
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


            // IMPORTANCE:
            // - IMPORTANCE_DEFAULT: shows everywhere, makes noise, but does not visually intrude
            // - IMPORTANCE_HIGH: shows everywhere, makes noise and peeks. May use full screen intents
            // - IMPORTANCE_LOW: Shows in the shade, and potentially in the status bar
            // - IMPORTANCE_MAX: Unused
            // - IMPORTANCE_MIN: only shows in the shade, below the fold. This should not be used with 'Service.startForeground' since a
            //                   foreground service is supposed to be something the user cares about so it does not make semantic sense
            //                   to mark its notification as minimum importance. If you do this as of Android version Build.VERSION_CODES.O,
            //                   the system will show a higher-priority notification about your app running in the background.
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            }
            val service = AppContext.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)

            return channelId
        }
    }
}