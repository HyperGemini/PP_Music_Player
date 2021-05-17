package com.example.musicplayer

import android.provider.MediaStore

class SongModel(songName:String,
                songAlbumTitle:String, songArtist: String, songPath: String
    ){
    val mSongName = songName
    val mSongAlbumTitle = songAlbumTitle
    val mSongArtist = songArtist
    val mSongPath = songPath
}