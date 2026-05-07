package com.futurecode.ghostfinderradardetector.utils

import android.content.Context
import android.media.MediaPlayer

object AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, resId: Int) {
        stop()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

}