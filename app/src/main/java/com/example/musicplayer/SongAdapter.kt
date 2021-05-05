package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.`interface`.CustomItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SongAdapter(songModelModel: MutableList<SongModel>,context:Context,
                  songName:TextView,playPause:FloatingActionButton,next:FloatingActionButton,previouse:FloatingActionButton,
                  loop:FloatingActionButton, shuffle:FloatingActionButton,currTime:TextView,totTime:TextView,seekBar: SeekBar) : RecyclerView.Adapter<SongAdapter.SongListViewHolder>() {
    var mContext = context
    var mSongNames = songName
    var mp: MediaPlayer?= null
    var pause:Boolean = true
    var mPlayPause = playPause
    var mNext = next
    var mPrevious = previouse
    var mLoop = loop
    var mShuffle = shuffle
    var mCurTime = currTime
    var mTotTime = totTime
    var mSeekBar = seekBar

    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()

    class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var song: TextView
        var album: TextView
        var albumArt: ImageView
        var mCustomItemClickListener: CustomItemClickListener?=null

        init {
            song = itemView.findViewById(R.id.song_name)
            album = itemView.findViewById(R.id.song_duration)
            albumArt = itemView.findViewById(R.id.image_cover)
            itemView.setOnClickListener(this)
        }

        fun setOnCustomItemCLickListener(customItemClickListener: CustomItemClickListener){
            this.mCustomItemClickListener = customItemClickListener
        }

        override fun onClick(v: View?) {
            this.mCustomItemClickListener!!.onCustomItemClick(v!!, adapterPosition)
        }
    }

    var mSongModel = songModelModel
    var allMusicList:ArrayList<String> =ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.song_item, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val model = mSongModel[position]
        val songName = model.mSongName
        val songAlbum = model.mSongAlbumTitle + " - " + model.mSongArtist
        holder!!.song.text = songName
        holder!!.album.text = songAlbum
        holder.setOnCustomItemCLickListener(object : CustomItemClickListener{
            override fun onCustomItemClick(view: View, pos: Int) {
                mSongNames.text = model.mSongName
                pause = false
                mPlayPause.setImageResource(R.drawable.ic_pause)

                if(mp!=null){
                    mp!!.stop()
                    mp!!.release()
                    mp = null
                }

                mp = MediaPlayer()
                mp!!.setDataSource(model.mSongPath)
                mp!!.prepare()
                mp!!.setOnPreparedListener {
                    mp!!.start()
                }
                initializeSeekBar()

                mTotTime.text = formatTime(mp!!.duration)
            }
        })


    }

    fun initializeSeekBar() {
        mSeekBar.max = mp!!.duration/1000

        runnable = Runnable {
            mSeekBar.progress = mp!!.currentPosition/1000
            mCurTime.text = formatTime(mp!!.currentPosition)
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
    fun formatTime(ms:Int): String{
        var sec = ms/1000
        var s = sec%60
        var m = sec/60
        var s_str:String
        var m_str:String
        if(s<10){
            s_str = "0${s}"
        }else{
            s_str = "${s}"
        }

        if(m<10){
            m_str = "0${m}"
        }else{
            m_str = "${m}"
        }

        return m_str+":"+s_str


    }

    override fun getItemCount(): Int {
        return mSongModel.size
    }
}