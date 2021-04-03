package com.example.datasaverexampleapp.video_audio.media_router_and_casting

import android.media.MediaMetadata
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.type_alias.ViewByID
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.framework.*
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import kotlinx.android.synthetic.main.activity_media_router_and_casting.*

/**
 * The Cast Application Framework provides several user interface elements that you can use to
 * initiate, and interact with, a Cast session - including the Cast Button and Mini and Expanded
 * Controllers
 *
 * The Cast Button is displayed when Cast discovers an available Receiver to which your app can cast.
 * When the user clicks the Cast Button, a dialog is displayed listing either all the available remote
 * devices to cast to or the metadata associated with the currently cast content.
 *
 * The Cast Button can be added to the app bar of your Activity as Media Route Action Provider
 * <menu xmlns:app="http://schemas.android.com/apk/res-auto"
 *       xmlns:android="http://schemas.android.com/apk/res/android">
 *       <item
 *       android:id="@+id/media_route_menu_item"
 *       android:title="@string/media_route_menu_title"
 *       app:actionProviderClass="android.support.v7.app.MediaRouteActionProvider"
 *       app:showAsAction="always" />
 * </menu>
 */
class MediaRouterAndCastingActivity : AppCompatActivity() {
    // All of your application's interaction with the Cast Application Framework are coordinated
    // through the 'CastContext' object, accessed by calling 'getSharedInstance' on the 'CastContext'
    // class - typically within the 'onCreate' handler of the Activity from which you plan to Cast media.
    private var castContext: CastContext? = null

    // Each Cast Session starts when the user selects a remote receiver from the Cast destination
    // selection dialog, and ends when they choose to stop casting (or another sender casts to the
    // same device)
    //
    // Sessions are managed by the SessionManager; you can access the current 'CastSession' using
    // the 'getCurrentCastSession' method on the 'CastContext', typically within the 'onResume'
    // handler of your Activity.
    private var castSession: CastSession? = null
    private var sessionManager: SessionManager? = null

    // You can use the Remote Media Client to set the content to stream to the remote device, and the metadata that
    // describes it using the 'MediaMetadata' class.
    private var movieMetaData: MediaMetadata? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Layout.activity_media_router_and_casting)
        title = "Media router and casting Example"
        castContext = CastContext.getSharedInstance(this)

        // Connect the media Route Button to the Cast Application Framework within your
        // Activity's 'onCreate' handler
        CastButtonFactory.setUpMediaRouteButton(applicationContext, mediaRouteButton)

        // Once the Cast Button has been added to your app, you will use a Cast Session
        // to specify the media (and its associated metadata) that your app will cast.
        castContext?.let { cast_Context ->
            sessionManager = cast_Context.sessionManager
        }

        // You can also attach a 'SessionManagerListener' to your Manager instance to listen for creation,
        // suspension, resumption and termination of new Cast Sessions.
        sessionManager?.addSessionManagerListener(object : SessionManagerListener<Session> {
            override fun onSessionStarting(session: Session?) {
                Log.i(javaClass.simpleName, "on Session starting!")
            }

            override fun onSessionStarted(session: Session?, p1: String?) {
                Log.i(javaClass.simpleName, "on Session started! with: $p1")

                castSession?.let { cast_session ->

                    cast_session.remoteMediaClient.let {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            movieMetaData = MediaMetadata.Builder().apply {
                                putString(MediaMetadata.METADATA_KEY_TITLE,"Media data title")
                            }.build()
                            castMovie(it)
                        }
                    }
                }
            }

            override fun onSessionStartFailed(p0: Session?, p1: Int) {
                Log.i(javaClass.simpleName, "on Session start failed! with: $p1")
            }

            override fun onSessionEnding(session: Session?) {
                Log.i(javaClass.simpleName, "on Session ending! with: $session")
            }

            override fun onSessionEnded(p0: Session?, p1: Int) {
                Log.i(javaClass.simpleName, "on Session ended!")
            }

            override fun onSessionResuming(p0: Session?, p1: String?) {
                Log.i(javaClass.simpleName, "on Session resuming!")
            }

            override fun onSessionResumed(p0: Session?, p1: Boolean) {
                Log.i(javaClass.simpleName, "on Session resumed!")
            }

            override fun onSessionResumeFailed(p0: Session?, p1: Int) {
                Log.i(javaClass.simpleName, "on Session resume failed!")
            }

            override fun onSessionSuspended(p0: Session?, p1: Int) {
                Log.i(javaClass.simpleName, "on Session suspended!")
            }
        })
    }

    private fun castMovie(remoteMediaClient: RemoteMediaClient)
    {
        // You can then control media playback on the remote device using the Remote Media Client.
        // The cast design checklist requires your sender app provide a mini controller that's
        // displayed whenever the user experience away from the primary content page, and an expanded
        // controller that displays a full-screen UI when the user clicks the media Notification or the
        // mini controller.
        movieMetaData?.let {

            val mediaInfoData = MediaInfo.Builder("https://www.youtube.com/watch?v=o37Q-9ECb9E").apply {
                setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            }.build()

            val request = MediaLoadRequestData.Builder().apply {
                setMediaInfo(mediaInfoData)
                setAutoplay(true)
            }.build()

            remoteMediaClient.load(request)
        }

    }

    override fun onResume() {
        super.onResume()
        castSession = sessionManager?.currentCastSession
    }

    override fun onPause() {
        super.onPause()
        castSession = null
    }

    // Then within the Fragments or Activities from which you want to Cast, override
    // the 'onCreateOptionsMenu' handler to setup the Media Route Button.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(com.example.datasaverexampleapp.type_alias.Menu.cast_menu, menu)
        CastButtonFactory.setUpMediaRouteButton(
            applicationContext,
            menu,
            ViewByID.media_route_menu_item
        )
        return true
    }
}