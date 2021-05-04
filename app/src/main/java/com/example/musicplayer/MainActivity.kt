package com.example.musicplayer

import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var songModelModelData: ArrayList<SongModel> = ArrayList()
    private lateinit var songAdapter: SongListAdapter
    companion object{
        val PERMISSION_REQUEST_CODE = 12
    }



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


    }

    fun loadData(){
        var songCursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        while (songCursor != null && songCursor.moveToNext()) {
            val songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val songDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            songModelModelData.add(SongModel(songName,
                    songDuration
            ))
        }

        songAdapter = SongListAdapter(songModelModelData)

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

