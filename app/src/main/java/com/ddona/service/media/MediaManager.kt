package com.ddona.service.media

import android.media.MediaPlayer
import com.ddona.service.model.Song
import android.provider.MediaStore

import android.content.Context
import android.database.Cursor
import android.util.Log

object MediaManager {
    private val songs = arrayListOf<Song>()
    private val mediaPlayer = MediaPlayer()
    private var currentSong = 0
    private const val MEDIA_IDLE = 0
    private const val MEDIA_PLAYING = 1
    private const val MEDIA_PAUSED = 2
    private const val MEDIA_STOPPED = 3
    private var mediaState = MEDIA_IDLE


    init {
        mediaPlayer.setOnCompletionListener {
            nextSong()
            //next bai
        }
    }

    fun setCurrentSong(index: Int) {
        currentSong = index
    }

    fun playPauseSong(isNew: Boolean = false) {
        if (mediaState == MEDIA_IDLE || mediaState == MEDIA_STOPPED || isNew) {
            mediaPlayer.reset()
            val song = songs[currentSong]
            mediaPlayer.setDataSource(song.path)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaState = MEDIA_PLAYING
            Log.d("doanpt", "song playing")
        } else if (mediaState == MEDIA_PLAYING) {
            mediaPlayer.pause()
            mediaState = MEDIA_PAUSED
        } else if (mediaState == MEDIA_PAUSED) {
            mediaPlayer.start()
            mediaState = MEDIA_PLAYING
        }
    }

    fun nextSong() {
        currentSong++
        if (currentSong > songs.size - 1) {
            currentSong = 0
        }
        playPauseSong(true)
    }

    fun previousSong() {
        if (currentSong <= 0) {
            currentSong = songs.size - 1
        } else {
            currentSong--
        }
        playPauseSong(true)
    }

    fun getSongList(context: Context) {
        val columnsName = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor: Cursor = context.contentResolver
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columnsName, null, null, null, null
            )!!
        val indexData: Int = cursor.getColumnIndex(columnsName[0])
        val indexTitle: Int = cursor.getColumnIndex(columnsName[1])
        val indexDisPlay: Int = cursor.getColumnIndex(columnsName[2])
        val indexDuration: Int = cursor.getColumnIndex(columnsName[3])
        val indexAlbum: Int = cursor.getColumnIndex(columnsName[4])
        var data: String
        var title: String
        var display: String
        var album: String
        var duration: Long
        val canNext = cursor.moveToFirst()
        songs.clear()
        while (!cursor.isAfterLast) {
            data = cursor.getString(indexData)
            title = cursor.getString(indexTitle)
            display = cursor.getString(indexDisPlay)
            album = cursor.getString(indexAlbum)
            duration = cursor.getLong(indexDuration)
            if (display.lastIndexOf(".") > 0) {
                val extension = display.substring(display.lastIndexOf("."))
                if (extension.equals(".mp3", ignoreCase = true)) {
                    songs.add(Song(title, data, display, album, duration))
                }
            }
            cursor.moveToNext()
        }
        cursor.close()
        Log.d("doanpt", "Song size is ${songs.size}")
    }


}