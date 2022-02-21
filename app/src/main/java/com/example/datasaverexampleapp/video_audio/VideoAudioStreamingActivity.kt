package com.example.datasaverexampleapp.video_audio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityVideoAudioStreamingBinding
import com.example.datasaverexampleapp.type_alias.Layout
import com.example.datasaverexampleapp.video_audio.audio_playback_focus.AudioPlaybackFocusExampleActivity
import com.example.datasaverexampleapp.video_audio.audio_recording.AudioRecordingActivity
import com.example.datasaverexampleapp.video_audio.background_audio.BackgroundAudioExampleActivity
import com.example.datasaverexampleapp.video_audio.exoplayer.ExoPlayerActivity
import com.example.datasaverexampleapp.video_audio.foreground_service.ForegroundServiceExampleActivity
import com.example.datasaverexampleapp.video_audio.media_router_and_casting.MediaRouterAndCastingActivity
import com.example.datasaverexampleapp.video_audio.media_session.MediaSessionActivity
import com.example.datasaverexampleapp.video_audio.mediaplayer_video.MediaPlayerVideoPlaybackActivity

/**
 * Smartphones and tablets have become so popular that for many
 * people they have entirely replaced all other portable electronics -
 * including, cameras, music players, and audio recorders. As a result
 * Android's media APIs, which allow us to build apps that offer a rich
 * audio, video and camera experience, have become increasingly powerful
 * and important.
 *
 * Android 8.1 Oreo (API Level 27) supports the following multimedia formats
 * for playback as part of the framework. Note that some devices may support
 * playback of additional file formats:
 * AUDIO
 * - ACC LC
 * - HE-ACCv1 (AAC+)
 * - HE-ACCv2 (Enhanced AAC+)
 * - ACC ELD (Enhanced Low Delay AAC)
 * - AMR-NB
 * - AMR-WB
 * - FLAC
 * - MP3
 * - MIDI
 * - Ogg Vorbis
 * - PCM/WAVE
 * - Opus
 *
 * IMAGE
 * - JPEG
 * - PNG
 * - WEBP
 * - GIF
 * - BMP
 *
 * VIDEO
 * - H.263
 * - H.264 AVC
 * - H.265 HEVC
 * - MPEG-4 SP
 * - VP8
 * - VP9
 *
 * The following network protocols are supported for streaming media:
 * - RTSP (RTP, SDP)
 * - HTTP/HTTPS progressive streaming
 * - HTTP/HTTPS live streaming (on device running Android 3.0 or above)
 *
 * Note: For full details on the currently supported media formats and recommendations for video
 *       encoding and audio streaming, see the Supported Media Formats page on the Android Developer
 *       documentation site.
 */
class VideoAudioStreamingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_audio_streaming)
        title = "Video/Audio Example"

        DataBindingUtil.setContentView<ActivityVideoAudioStreamingBinding>(
            this, Layout.activity_video_audio_streaming
        ).apply {

            mediaPlayerVideoPlayback?.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, MediaPlayerVideoPlaybackActivity::class.java)
                startActivity(intent)
            }

            exoPlayerExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, ExoPlayerActivity::class.java)
                startActivity(intent)
            }

            audioPlaybackExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, AudioPlaybackFocusExampleActivity::class.java)
                startActivity(intent)
            }

            mediaSessionExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, MediaSessionActivity::class.java)
                startActivity(intent)
            }

            mediaRouterAndCastingExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, MediaRouterAndCastingActivity::class.java)
                startActivity(intent)
            }

            backgroundAudioExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, BackgroundAudioExampleActivity::class.java)
                startActivity(intent)
            }

            foregroundServiceExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, ForegroundServiceExampleActivity::class.java)
                startActivity(intent)
            }

            audioRecordingExample.setOnClickListener {
                val intent = Intent(this@VideoAudioStreamingActivity, AudioRecordingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}