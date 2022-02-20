package com.example.datasaverexampleapp.video_audio.media_router_and_casting

import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

/**
 * To cast functionality to an Activity, start by creating a new 'OptionsProvider'
 * implementation that will define the Google Cast Options, and return them via a
 * 'castOptions' object from the 'getCastOptions' handler.
 *
 * Only the receiver application ID is a required option, as it's used to filter the list
 * of available destination and to launch the receiver app on the selected target device
 * when cast session is started.
 *
 * The destination for your app's routed media is a Cast receiver application, an HTML5/JavaScript
 * application running on a receiver device, which provides the UI to display your app's content
 * and handle media control messages.
 *
 * The cast Application Framework includes a pre-built receiver application hosted by Google that
 * can be used by providing 'CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID' as the
 * application ID.
 *
 * It is also possible to create your own custom Media Receiver, although that is beyond the scope
 * of this book. Instructions for building a custom receiver can be found at
 * 'developer.google.com/cast/docs/android_sender_setup'
 *
 * Once your options Provider has been defined, declare it within your Application manifest using a
 * meta-data tag:
 * <meta-data
 *    android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
 *    android:value="com.example.datasaverexampleapp.video_audio.media_router_and_casting.CastOptionsProvider" /> 'THIS IS THE OptionsProvider CLASS'
 */
class CastOptionsProvider : OptionsProvider
{
    override fun getCastOptions(p0: Context): CastOptions = CastOptions.Builder()
        .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
        .build()

    override fun getAdditionalSessionProviders(p0: Context): MutableList<SessionProvider>? = null

}