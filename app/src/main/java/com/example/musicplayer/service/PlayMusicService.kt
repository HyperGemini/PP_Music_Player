package com.example.musicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.musicplayer.SongAdapter

class PlayMusicService: Service() {

    var currentPos:Int = 0
    var musicDataList:ArrayList<String> = ArrayList()
    var mp:MediaPlayer?=null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        currentPos = intent!!.getIntExtra(SongAdapter.MUSICITEMPOS, 0)
        musicDataList = intent.getStringArrayListExtra(SongAdapter.MUSICLIST) as ArrayList<String>

        if(mp!=null){
            mp!!.stop()
            mp!!.release()
            mp = null
        }

        mp = MediaPlayer()
        mp!!.setDataSource(musicDataList[currentPos])
        mp!!.prepare()
        mp!!.setOnPreparedListener {
            mp!!.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }



}