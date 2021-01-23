package com.example.datasaverexampleapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.type_alias.Drawable
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity() {

    private val MESSAGE_CHANNEL = "messages"
    private var notificationManager:NotificationManagerCompat? = null
    private var receiver:BroadcastReceiver? = null

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

        notification_with_intent_button?.setOnClickListener {

            createMessageNotificationChannel()
            val NEW_MESSAGE_ID = 1
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification with intent"
            val content = "This is a notification with intent"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setAutoCancel(true) // To dismiss notification when tap
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color

            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        val filter = IntentFilter()
        filter.addAction("notification_cancelled")

        receiver = object: BroadcastReceiver(){

            override fun onReceive(context: Context?, intent: Intent?) {
                Log.i("TAG","Action: ${intent?.action}")
            }
        }
        registerReceiver(receiver, filter)

        notification_with_delete_intent_button?.setOnClickListener {

            createMessageNotificationChannel()
            val NEW_MESSAGE_ID = 1
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification with intent"
            val content = "This is a notification with intent"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            // PendingIntent.getActivity(this, 0, deleteIntent, 0)

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setDeleteIntent(getDeleteIntent()) // This is the delete intent that will be launch when notificationis dismissed
                .setAutoCancel(true) // To dismiss notification when tap
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color

            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }


        notification_big_text_button?.setOnClickListener {

            val NEW_MESSAGE_ID = 2
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification Big text"
            val content = "This is a notification with big text is a notification with big text is a notification with big text is a notification with big text"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            // PendingIntent.getActivity(this, 0, deleteIntent, 0)

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setAutoCancel(true) // To dismiss notification when tap
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                .setStyle(NotificationCompat.BigTextStyle().bigText(content)) // Set big text style to the notification
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color


            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        notification_big_picture_button?.setOnClickListener {

            val NEW_MESSAGE_ID = 2
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification Big picture"
            val content = "This is a notification with big picture"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setAutoCancel(true) // To dismiss notification when tap
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromVectorDrawable(Drawable.ic_notification))) // Set big picture style to the notification
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color


            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        notification_messaging_style_button?.setOnClickListener {

            val NEW_MESSAGE_ID = 2
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification Messaging"
            val content = "This is a messaging notification"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            val person1 = Person.Builder().setName("User 1").build()
            val person2 = Person.Builder().setName("User 2").build()
            val person3 = Person.Builder().setName("User 3").build()

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setAutoCancel(true) // To dismiss notification when tap
                .setShowWhen(true) // Show the time the notification was posted
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                    //mUser = new Person.Builder().setName(userDisplayName).build();
                .setStyle(NotificationCompat.MessagingStyle(
                    Person.Builder().setName("User display name").build())
                    .addMessage("Message 1",System.currentTimeMillis(),person1)
                    .addMessage("Message 2",System.currentTimeMillis(),person2)
                    .addMessage("Message 3",System.currentTimeMillis(),person3)) // Set messaging style to the notification
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color


            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        notification_with_category_sender_button?.setOnClickListener {

            val NEW_MESSAGE_ID = 2
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification category & sender"
            val content = "This is a notification with category and sender"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setAutoCancel(true) // To dismiss notification when tap
                .setLargeIcon(getBitmapFromVectorDrawable(Drawable.ic_notification))
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .addPerson("tel: +31631564996") // Add people when notification is ONLY allowed by the user
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color


            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }

        notification_group_button?.setOnClickListener {

            val NEW_MESSAGE_ID = 2
            val builder = NotificationCompat.Builder(applicationContext, MESSAGE_CHANNEL)

            val title = "Notification group example"
            val content = "This is a notification group example"

            val launchIntent =  Intent(this,NotificationActivity::class.java)
            val contentIntent = TaskStackBuilder.create(applicationContext)
                .addNextIntentWithParentStack(launchIntent)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

            // This the notification inbox style property
            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.addLine("Email subject 1")
            inboxStyle.addLine("Email subject 2")
            inboxStyle.addLine("Email subject 3")
            inboxStyle.addLine("Email subject 4")

            builder.setSmallIcon(Drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent) // Set your content intent to the notification which will be executed on tap
                .setAutoCancel(true) // To dismiss notification when tap
                .setGroup("test@example.com")
                .setGroupSummary(true)
                .setStyle(inboxStyle)
                .color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) // set background color

            notificationManager?.notify(NEW_MESSAGE_ID, builder.build())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Toast.makeText(this, "Notification deleted!", Toast.LENGTH_LONG).show();
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

    private fun getDeleteIntent(): PendingIntent
    {
        val intent = Intent(this,NotificationActivity::class.java)
        intent.action = "notification_cancelled"
        return PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

}