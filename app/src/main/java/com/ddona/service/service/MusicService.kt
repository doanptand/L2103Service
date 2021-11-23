package com.ddona.service.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.ddona.service.R
import com.ddona.service.media.MediaManager

class MusicService : Service() {

    private val binder = MusicBinder()
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class MusicBinder : Binder() {
        fun getMusicService(): MusicService {
            return this@MusicService
        }
    }

    override fun onCreate() {
        super.onCreate()
        MediaManager.getSongList(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaManager.setCurrentSong(1)
        Log.d("doanpt", "play song")
        playPauseSong()
        val builder = Notification.Builder(this)
        builder.setContentTitle("Music")
        builder.setContentText("This is music app")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val notificationManger: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build
                .VERSION_CODES.O
        ) {

            val channel = NotificationChannel(
                "1",
                "music",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManger.createNotificationChannel(channel)
            builder.setChannelId(channel.id)
        }

        val notification = builder.build()
        startForeground(1, notification)
//        notificationManger.notify(1, notification)
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