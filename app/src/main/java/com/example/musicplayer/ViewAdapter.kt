package com.example.musicplayer

import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewAdapter(
    songName: TextView, playPause: FloatingActionButton, next: FloatingActionButton,
    previous: FloatingActionButton, loop: FloatingActionButton,
    shuffle: FloatingActionButton, currTime: TextView, totTime: TextView,
    seekBar: SeekBar, playPausePrim: FloatingActionButton, songNamePrim: TextView
) {
    var mSongName = songName
    var mPlayPause = playPause
    var mNext = next
    var mPrevious = previous
    var mLoop = loop
    var mShuffle = shuffle
    var mCurTime = currTime
    var mTotTime = totTime
    var mSeekBar = seekBar
    var mPlayPousePrim = playPausePrim
    var mSongNamePrim = songNamePrim
}