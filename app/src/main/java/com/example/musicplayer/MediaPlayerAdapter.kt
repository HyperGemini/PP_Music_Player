package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Handler

class MediaPlayerAdapter(songList: MutableList<SongModel>, viewAdapter: ViewAdapter) {
    private var mSongList = songList
    private var position = 0
    var isLoop = false
    var isShuffle = false
    var isPause = true
    var isInit = false
    var mp: MediaPlayer? = null
    private var mViewAdapter = viewAdapter

    private var mSeekBar = mViewAdapter.mSeekBar
    private var mCurTime = mViewAdapter.mCurTime
    private var mSongName = mViewAdapter.mSongName
    private var mTotTime = mViewAdapter.mTotTime
    private var mPlayPause = mViewAdapter.mPlayPause

    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()


    fun creatMediaPlayer(pos: Int) {
        if (isInit) {
            destroyPlayer()
        }

        isPause = false
        mPlayPause.setImageResource(R.drawable.ic_pause)

        isInit = true
        position = pos
        mp = MediaPlayer()
        mp!!.setDataSource(mSongList[pos].mSongPath)
        mp!!.prepare()
        mp!!.setOnPreparedListener {
            mp!!.start()
        }
        initializeSeekBar()
        setSongName()
        setSongDuration()

        mp!!.setOnCompletionListener {
            destroyPlayer()
            playNextSong()
        }
    }

    fun playNextSong() {

        when {
            isShuffle -> {
                position = (0 until mSongList.size).random()
                creatMediaPlayer(position)

            }
            isLoop -> {
                creatMediaPlayer(position)
            }
            else -> {
                if (position >= mSongList.size - 1) {
                    position = -1
                }
                creatMediaPlayer(position + 1)
            }
        }
    }

    fun playPreviousSong() {
        if (position <= 0) {
            position = mSongList.size
        }
        creatMediaPlayer(position - 1)
    }

    private fun destroyPlayer() {
        mp!!.stop()
        mp!!.release()
        mp = null
        isInit = false
    }

    private fun setSongName() {
        mSongName.text = mSongList[position].mSongName
    }

    private fun setSongDuration() {
        mTotTime.text = formatTime(mp!!.duration)
    }

    private fun initializeSeekBar() {
        mSeekBar.max = mp!!.duration / 1000

        runnable = Runnable {
            mSeekBar.progress = mp!!.currentPosition / 1000
            mCurTime.text = formatTime(mp!!.currentPosition)
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    fun formatTime(ms: Int): String { // iz ms u MM:SS
        val sec = ms / 1000
        val s = sec % 60
        val m = sec / 60

        val sStr: String = if (s < 10) {
            "0${s}"
        } else {
            "$s"
        }

        val mStr: String = if (m < 10) {
            "0${m}"
        } else {
            "$m"
        }

        return "$mStr:$sStr"

    }

    fun pauseSong() {
        mp!!.pause()
        mPlayPause.setImageResource(R.drawable.ic_play)
        isPause = true
    }

    fun resumeSong() {
        mp!!.start()
        initializeSeekBar()
        mPlayPause.setImageResource(R.drawable.ic_pause)
        isPause = false
    }
}