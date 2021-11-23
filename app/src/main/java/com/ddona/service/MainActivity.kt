package com.ddona.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddona.service.media.MediaManager
import com.ddona.service.service.MusicService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MediaManager.getSongList(this)
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
    }
}