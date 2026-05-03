package com.stark.superisland.core

import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState

class MediaManager(context: Context) {
    private val sessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

    fun getActiveSession(): MediaController? {
        val controllers = sessionManager.getActiveSessions(null)
        return controllers.firstOrNull()
    }

    fun isPlaying(controller: MediaController?): Boolean {
        return controller?.playbackState?.state == PlaybackState.STATE_PLAYING
    }
}
