package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    lateinit var mediaPlayerAdapter: MediaPlayerAdapter
    lateinit var viewAdapter: ViewAdapter



    private lateinit var songAdapter: SongAdapter
    companion object{
        val PERMISSION_REQUEST_CODE = 12
    }



    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE )
        }else{
            loadData()
        }

        setOnClickListeners()
        songName.text = songModelModelData[0].mSongName


    }


    private fun setOnClickListeners() {
        playPause.setOnClickListener {
            if(!mediaPlayerAdapter.isInit){
                mediaPlayerAdapter.creatMediaPlayer(0)
            }else{
                if(mediaPlayerAdapter.isPause){
                    mediaPlayerAdapter.resumeSong()
                }else{
                    mediaPlayerAdapter.pauseSong()
                }
            }
        }

        next.setOnClickListener{
            mediaPlayerAdapter.playNextSong()
        }

        previous.setOnClickListener{
            mediaPlayerAdapter.playPreviousSong()
        }

        shuffle.setOnClickListener{
            if(mediaPlayerAdapter.isShuffle){
                mediaPlayerAdapter.isShuffle = false
                Toast.makeText(applicationContext,"Shuffle is off",Toast.LENGTH_SHORT).show()
            }else{
                mediaPlayerAdapter.isShuffle = true
                Toast.makeText(applicationContext,"Shuffle is on",Toast.LENGTH_SHORT).show()
            }
        }

        loop.setOnClickListener{
            if(mediaPlayerAdapter.isLoop){
                mediaPlayerAdapter.isLoop = false
                Toast.makeText(applicationContext,"Loop is off",Toast.LENGTH_SHORT).show()
            }else{
                mediaPlayerAdapter.isLoop = true
                Toast.makeText(applicationContext,"Loop is on",Toast.LENGTH_SHORT).show()
            }
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    if(mediaPlayerAdapter.mp != null) {
                        mediaPlayerAdapter.mp!!.seekTo(i * 1000)
                        curTime.text = mediaPlayerAdapter.formatTime(i*1000)
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
        songName = findViewById(R.id.tv_song_name)
        curTime = findViewById(R.id.tv_current_time)
        totTime = findViewById(R.id.tv_total_time)
        playPause = findViewById(R.id.btn_playpause)
        next = findViewById(R.id.btn_next)
        previous = findViewById(R.id.btn_previous)
        loop = findViewById(R.id.btn_loop)
        shuffle = findViewById(R.id.btn_shuffle)
        seekBar = findViewById(R.id.sb_seekbar)

        // TODO: Limit Search
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

        songModelModelData.sortWith(Comparator{ x ,y -> x.mSongName.compareTo(y.mSongName)})

        viewAdapter = ViewAdapter(songName,playPause,next,previous,loop,shuffle,curTime,totTime,seekBar)
        mediaPlayerAdapter = MediaPlayerAdapter(songModelModelData,viewAdapter)
        songAdapter = SongAdapter(songModelModelData,mediaPlayerAdapter)

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

