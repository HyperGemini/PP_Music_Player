package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.`interface`.CustomItemClickListener
import com.example.musicplayer.service.PlayMusicService

class SongAdapter(songModelModel: MutableList<SongModel>,context:Context) : RecyclerView.Adapter<SongAdapter.SongListViewHolder>() {
    var mContext = context

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

    companion object{
        val MUSICLIST = "musiclist"
        val MUSICITEMPOS = "position"
    }

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
                //toast
                for(i in 0 until mSongModel.size){
                    allMusicList.add(mSongModel[i].mSongPath)

                }

                var musicDataIntent = Intent(mContext,PlayMusicService::class.java)

                musicDataIntent.putStringArrayListExtra(MUSICLIST,allMusicList)
                musicDataIntent.putExtra(MUSICITEMPOS, pos)

                mContext.startService(musicDataIntent)
            }
        })
    }

    override fun getItemCount(): Int {
        return mSongModel.size
    }
}