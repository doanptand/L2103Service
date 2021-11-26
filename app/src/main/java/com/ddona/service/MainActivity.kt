package com.ddona.service

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ddona.service.databinding.ActivityMainBinding
import com.ddona.service.service.MusicService
import com.ddona.service.util.Const

class MainActivity : AppCompatActivity() {

    private lateinit var musicService: MusicService

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startMusicService()
        } else {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    123
                )
        }
        binding.btnNext.setOnClickListener {
            val nextIntent = Intent(Const.INTENT_NEXT_SONG)
            LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent)
//            musicService.nextSong()
//            val nextIntent = Intent("com.doan.dep.trai")
//            sendBroadcast(nextIntent)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent)
        }
        binding.btnPrevious.setOnClickListener {
            val previousIntent = Intent(Const.INTENT_PREVIOUS_SONG)
            LocalBroadcastManager.getInstance(this).sendBroadcast(previousIntent)
//            musicService.previousSong()
        }
        binding.btnPause.setOnClickListener {
            val playPauseIntent = Intent(Const.INTENT_PLAY_PAUSE_SONG)
            LocalBroadcastManager.getInstance(this).sendBroadcast(playPauseIntent)
//            musicService.playPauseSong()
        }
        binding.btnPlay.setOnClickListener {
            val playPauseIntent = Intent(Const.INTENT_PLAY_PAUSE_SONG)
            LocalBroadcastManager.getInstance(this).sendBroadcast(playPauseIntent)
//            musicService.playPauseSong()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMusicService()
            } else {
                Toast.makeText(this, "We can't do anything without permission", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getMusicService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }

    }

    private fun startMusicService() {
        val intent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        bindService(intent, connection, BIND_AUTO_CREATE)
    }
}