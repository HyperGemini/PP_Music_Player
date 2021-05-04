package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongListAdapter(songModelModel: MutableList<SongModel>) : RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {
    class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var song: TextView
        var album: TextView
        var albumArt: ImageView

        init {
            song = itemView.findViewById(R.id.song_name)
            album = itemView.findViewById(R.id.song_duration)
            albumArt = itemView.findViewById(R.id.image_cover)
        }
    }

    var mSongModel = songModelModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.song_item, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val model = mSongModel[position]
        val songName = model.mSongName
        val songAlbum = model.mSongAlbumTitle
        holder!!.song.text = songName
        holder!!.album.text = songAlbum
    }

    override fun getItemCount(): Int {
        return mSongModel.size
    }
}