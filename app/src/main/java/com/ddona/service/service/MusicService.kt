package com.ddona.service.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ddona.service.R
import com.ddona.service.media.MediaManager
import com.ddona.service.receiver.MyFirstBroadcastReceiver
import com.ddona.service.receiver.MySecondBroadcastManager
import com.ddona.service.util.Const

class MusicService : Service() {
    private val firstBroadcast = MyFirstBroadcastReceiver()
    private val secondBroadcast = MySecondBroadcastManager()
    private val musicReceiver = MusicReceiver()

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
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction("com.doan.dep.trai")
        intentFilter.priority = 999
        registerReceiver(firstBroadcast, intentFilter)


        val intentFilter2 = IntentFilter()
        intentFilter2.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        intentFilter2.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter2.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter2.addAction("com.doan.dep.trai")
        intentFilter2.priority = 999
//        registerReceiver(secondBroadcast, intentFilter2)
        LocalBroadcastManager.getInstance(this).registerReceiver(secondBroadcast, intentFilter)

        val musicFilter = IntentFilter()
        musicFilter.addAction(Const.INTENT_STOP_APPLICATION)
        musicFilter.addAction(Const.INTENT_PREVIOUS_SONG)
        musicFilter.addAction(Const.INTENT_PLAY_PAUSE_SONG)
        musicFilter.addAction(Const.INTENT_NEXT_SONG)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(musicReceiver, musicFilter)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pushNotification() {
        val remoteView = RemoteViews(packageName, R.layout.layout_notification_music)

        val builder = Notification.Builder(this)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
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

//        builder.setContent(remoteView)
        builder.setCustomContentView(remoteView)
//        builder.setCustomBigContentView(remoteView)
//        builder.setCustomHeadsUpContentView(remoteView)

        val notification = builder.build()

        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaManager.setCurrentSong(1)
        Log.d("doanpt", "play song")
        playPauseSong()
        pushNotification()
//        notificationManger.notify(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(secondBroadcast)
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(musicReceiver)
        unregisterReceiver(firstBroadcast)
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

    inner class MusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("doanpt", "music receive action: ${intent?.action}")
            when (intent?.action) {
                Const.INTENT_NEXT_SONG -> MediaManager.nextSong()
                Const.INTENT_PLAY_PAUSE_SONG -> MediaManager.playPauseSong()
                Const.INTENT_PREVIOUS_SONG -> MediaManager.previousSong()
                Const.INTENT_STOP_APPLICATION -> this@MusicService.stopSelf()
            }
        }

    }
}