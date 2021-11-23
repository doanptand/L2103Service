package com.ddona.service.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ddona.service.media.MediaManager

class MusicService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaManager.setCurrentSong(1)
        Log.d("doanpt","play song")
        playPauseSong()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setSongIndex(index: Int) {
        MediaManager.setCurrentSong(index)
    }

    fun playPauseSong() {
        MediaManager.playPauseSong()
    }

    fun nextSong() {
        MediaManager.nextSong()
    }

    fun previousSong() {
        MediaManager.previousSong()
    }
}