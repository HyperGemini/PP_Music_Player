package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.home_page.*

class MainActivity : AppCompatActivity() {
    var songModelModelData: ArrayList<SongModel> = ArrayList()

    lateinit var songName: TextView
    lateinit var playPause: FloatingActionButton
    lateinit var next: FloatingActionButton
    lateinit var previous: FloatingActionButton
    lateinit var loop: FloatingActionButton
    lateinit var shuffle: FloatingActionButton
    lateinit var curTime: TextView
    lateinit var totTime: TextView
    lateinit var seekBar: SeekBar

    lateinit var seekTime: TextView

    lateinit var songControl: LinearLayout
    lateinit var playPausePrim: FloatingActionButton
    lateinit var nextPrim: FloatingActionButton
    lateinit var changeState: FloatingActionButton
    lateinit var songNamePrim: TextView
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    lateinit var mediaPlayerAdapter: MediaPlayerAdapter
    lateinit var viewAdapter: ViewAdapter

    var lastState: Int = BottomSheetBehavior.STATE_COLLAPSED

    private lateinit var songAdapter: SongAdapter

    companion object {
        val PERMISSION_REQUEST_CODE = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)
        setContentView(R.layout.home_page)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadData()
        }

        setOnClickListeners()

        songName.text = songModelModelData[0].mSongName

        songNamePrim.text = songModelModelData[0].mSongName
        bottomSheetBehavior = BottomSheetBehavior.from(songControl)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (lastState === BottomSheetBehavior.STATE_COLLAPSED) {
                    songNamePrim.visibility = View.GONE
                    playPausePrim.visibility = View.GONE
                    nextPrim.visibility = View.GONE
                    changeState.setImageResource(R.drawable.ic_arrow_down)
                }
                if (lastState == BottomSheetBehavior.STATE_EXPANDED) {
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState === BottomSheetBehavior.STATE_EXPANDED) {
                    recyclerView.visibility = View.GONE
                    lastState = BottomSheetBehavior.STATE_EXPANDED
                }
                if (newState === BottomSheetBehavior.STATE_COLLAPSED) {
                    songNamePrim.visibility = View.VISIBLE
                    playPausePrim.visibility = View.VISIBLE
                    nextPrim.visibility = View.VISIBLE
                    changeState.setImageResource(R.drawable.ic_arrow_up)
                    //recyclerView.visibility = View.VISIBLE
                    lastState = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val search: MenuItem? = menu?.findItem(R.id.nav_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Search songs"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                songAdapter.filter.filter(newText)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    @Suppress("DEPRECATION")
    fun loadData() {
        songName = findViewById(R.id.tv_song_name)
        curTime = findViewById(R.id.tv_current_time)
        totTime = findViewById(R.id.tv_total_time)
        playPause = findViewById(R.id.btn_playpause)
        next = findViewById(R.id.btn_next)
        previous = findViewById(R.id.btn_previous)
        loop = findViewById(R.id.btn_loop)
        shuffle = findViewById(R.id.btn_shuffle)
        seekBar = findViewById(R.id.sb_seekbar)

        seekTime = findViewById(R.id.tv_seek)

        songControl = findViewById(R.id.songControl)
        playPausePrim = findViewById(R.id.playPausePrim)
        nextPrim = findViewById(R.id.nextPrim)
        songNamePrim = findViewById(R.id.tv_song_name_prim)
        changeState = findViewById(R.id.change_state)

        val songCursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (songCursor != null && songCursor.moveToNext()) {
            val songName =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val songAlbum =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val songArtist =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val songPath =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))

            songModelModelData.add(
                SongModel(
                    songName,
                    songAlbum,
                    songArtist,
                    songPath
                )
            )
        }

        songModelModelData.sortWith(Comparator { x, y -> x.mSongName.compareTo(y.mSongName) })

        viewAdapter = ViewAdapter(
            songName,
            playPause,
            next,
            previous,
            loop,
            shuffle,
            curTime,
            totTime,
            seekBar,
            playPausePrim,
            songNamePrim
        )
        mediaPlayerAdapter = MediaPlayerAdapter(songModelModelData, viewAdapter)

        songAdapter = SongAdapter(songModelModelData, mediaPlayerAdapter)
        recyclerView.adapter = songAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT).show()
                loadData()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun setOnClickListeners() {
        playPause.setOnClickListener {
            if (!mediaPlayerAdapter.isInit) {
                mediaPlayerAdapter.createMediaPlayer(0)
            } else {
                if (mediaPlayerAdapter.isPause) {
                    mediaPlayerAdapter.resumeSong()
                } else {
                    mediaPlayerAdapter.pauseSong()
                }
            }
        }

        playPausePrim.setOnClickListener {
            if (!mediaPlayerAdapter.isInit) {
                mediaPlayerAdapter.createMediaPlayer(0)
            } else {
                if (mediaPlayerAdapter.isPause) {
                    mediaPlayerAdapter.resumeSong()
                } else {
                    mediaPlayerAdapter.pauseSong()
                }
            }
        }

        next.setOnClickListener {
            mediaPlayerAdapter.playNextSong()
        }

        nextPrim.setOnClickListener {
            mediaPlayerAdapter.playNextSong()
        }

        previous.setOnClickListener {
            mediaPlayerAdapter.playPreviousSong()
        }

        shuffle.setOnClickListener {
            if (mediaPlayerAdapter.isShuffle) {
                mediaPlayerAdapter.isShuffle = false
                Toast.makeText(applicationContext, "Shuffle is off", Toast.LENGTH_SHORT).show()
            } else {
                mediaPlayerAdapter.isShuffle = true
                Toast.makeText(applicationContext, "Shuffle is on", Toast.LENGTH_SHORT).show()
            }
        }

        loop.setOnClickListener {
            if (mediaPlayerAdapter.isLoop) {
                mediaPlayerAdapter.isLoop = false
                Toast.makeText(applicationContext, "Loop is off", Toast.LENGTH_SHORT).show()
            } else {
                mediaPlayerAdapter.isLoop = true
                Toast.makeText(applicationContext, "Loop is on", Toast.LENGTH_SHORT).show()
            }
        }

        changeState.setOnClickListener {
            val state: Int

            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                state = BottomSheetBehavior.STATE_COLLAPSED
                recyclerView.visibility = View.VISIBLE
                changeState.setImageResource(R.drawable.ic_arrow_up)
            } else {
                state = BottomSheetBehavior.STATE_EXPANDED
                changeState.setImageResource(R.drawable.ic_arrow_down)
            }
            bottomSheetBehavior.state = state
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    if (mediaPlayerAdapter.mp != null) {
                        mediaPlayerAdapter.mp!!.seekTo(i * 1000)
                        curTime.text = mediaPlayerAdapter.formatTime(i * 1000)
                        seekTime.text = mediaPlayerAdapter.formatTime(i * 1000)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                seekTime.visibility = View.VISIBLE
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                seekTime.visibility = View.GONE
            }
        })
    }
}