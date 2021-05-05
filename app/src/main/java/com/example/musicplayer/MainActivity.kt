package com.example.musicplayer

import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var songModelModelData: ArrayList<SongModel> = ArrayList()

    lateinit var songName:TextView
    lateinit var playPause:FloatingActionButton
    lateinit var next:FloatingActionButton
    lateinit var previous:FloatingActionButton
    lateinit var loop:FloatingActionButton
    lateinit var shuffle:FloatingActionButton
    lateinit var curTime:TextView
    lateinit var totTime:TextView
    lateinit var seekBar: SeekBar



    private lateinit var songAdapter: SongAdapter
    companion object{
        val PERMISSION_REQUEST_CODE = 12
    }


    var pause: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        songName= findViewById(R.id.tv_song_name)
        curTime = findViewById(R.id.tv_current_time)
        totTime = findViewById(R.id.tv_total_time)

        playPause = findViewById(R.id.btn_playpause)
        next = findViewById(R.id.btn_next)
        previous = findViewById(R.id.btn_previous)
        loop = findViewById(R.id.btn_next)
        shuffle = findViewById(R.id.btn_shuffle)

        seekBar = findViewById(R.id.sb_seekbar)

        if(ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE )
        }else{
            loadData()
        }

        val playPause: FloatingActionButton = findViewById(R.id.btn_playpause)


        songName.text = songModelModelData[0].mSongName

        playPause.setOnClickListener {
            if(songAdapter.pause){

                playPause.setImageResource(R.drawable.ic_pause)

                if(songAdapter.mp==null){
                    songAdapter.mp = MediaPlayer()
                    songAdapter.mp!!.setDataSource(songModelModelData[0].mSongPath)
                    songAdapter.mp!!.prepare()
                    songAdapter.mp!!.setOnPreparedListener {
                        songAdapter.mp!!.start()
                    }
                }
                songAdapter.mp!!.start()
                songAdapter.pause = false
                songAdapter.initializeSeekBar()
            }
            else{
                playPause.setImageResource(R.drawable.ic_play)
                songAdapter.mp!!.pause()
                songAdapter.pause = true
            }
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    if(songAdapter.mp != null) {
                        songAdapter.mp!!.seekTo(i * 1000)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }



    @Suppress("DEPRECATION")
    fun loadData(){
        var songCursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        while (songCursor != null && songCursor.moveToNext()) {
            val songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val songAlbum = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val songArtist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val songPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            songModelModelData.add(SongModel(
                songName,
                songAlbum,
                songArtist,
                songPath
            ))
        }

        songAdapter = SongAdapter(songModelModelData,applicationContext,songName,playPause,next,previous,loop,shuffle,curTime,totTime,seekBar)

        recyclerView.adapter = songAdapter
        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Permission granted",Toast.LENGTH_SHORT).show()
                loadData()
            }
        }
    }






}

