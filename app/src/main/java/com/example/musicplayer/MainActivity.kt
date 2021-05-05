package com.example.musicplayer

import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
<<<<<<< HEAD
import android.view.MotionEvent
import android.widget.EditText
=======
>>>>>>> 2eb76c6ada99910b99b1be2c542a6908e45bca59
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var songModelModelData: ArrayList<SongModel> = ArrayList()
    private lateinit var songAdapter: SongAdapter
    companion object{
        val PERMISSION_REQUEST_CODE = 12
    }

    lateinit var songName:TextView

    var pause: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songName= findViewById(R.id.tv_song_name)
        if(ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE )
        }else{
            loadData()
        }

        val playPause: FloatingActionButton = findViewById(R.id.btn_playpause)
<<<<<<< HEAD


        songName.text = songModelModelData[songAdapter.pos_].mSongName

        playPause.setOnClickListener {
            if(songAdapter.pause){
                playPause.setImageResource(R.drawable.ic_play)
                songAdapter.pause = false
                songAdapter.mp!!.pause()
            }
            else{
                playPause.setImageResource(R.drawable.ic_pause)
                songAdapter.pause = true
                songAdapter.mp!!.start()
            }
        }


        recyclerView.setOnClickListener{
            Toast.makeText(applicationContext,"Permission granted",Toast.LENGTH_SHORT).show()
            songName.text = songModelModelData[songAdapter.pos_].mSongName
=======
        val songName: TextView = findViewById(R.id.tv_song_name)

        songName.text = songModelModelData[0].mSongName

        playPause.setOnClickListener {
            if(pause){
                playPause.setImageResource(R.drawable.ic_play)
                pause = false
            }
            else{
                playPause.setImageResource(R.drawable.ic_pause)
                pause = true
            }
>>>>>>> 2eb76c6ada99910b99b1be2c542a6908e45bca59
        }
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

<<<<<<< HEAD
        songAdapter = SongAdapter(songModelModelData,applicationContext,songName)



=======
        songAdapter = SongAdapter(songModelModelData,applicationContext)
>>>>>>> 2eb76c6ada99910b99b1be2c542a6908e45bca59

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

