package com.example.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.`interface`.CustomItemClickListener

class SongAdapter(songModelModel: MutableList<SongModel>, mediaPlayerAdapter: MediaPlayerAdapter) :
    RecyclerView.Adapter<SongAdapter.SongListViewHolder>(), Filterable {

    var mMediaPlayer = mediaPlayerAdapter
    var mSongModel = songModelModel
    var mSongModelFilter = songModelModel

    class SongListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var song: TextView
        var album: TextView
        var albumArt: ImageView
        var mCustomItemClickListener: CustomItemClickListener? = null

        init {
            song = itemView.findViewById(R.id.song_name)
            album = itemView.findViewById(R.id.song_duration)
            albumArt = itemView.findViewById(R.id.image_cover)
            itemView.setOnClickListener(this)
        }

        fun setOnCustomItemCLickListener(customItemClickListener: CustomItemClickListener) {
            this.mCustomItemClickListener = customItemClickListener
        }

        override fun onClick(v: View?) {
            this.mCustomItemClickListener!!.onCustomItemClick(v!!, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val model = mSongModel[position]
        val songName = model.mSongName
        val songAlbum = model.mSongAlbumTitle + " - " + model.mSongArtist

        holder.song.text = songName
        holder.album.text = songAlbum
        holder.setOnCustomItemCLickListener(object : CustomItemClickListener {
            override fun onCustomItemClick(view: View, pos: Int) {
                mMediaPlayer.createMediaPlayer(pos)
            }
        })
    }

    override fun getItemCount(): Int {
        return mSongModel.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (charSequence.isEmpty()) {
                    filterResults.count = mSongModelFilter.size
                    filterResults.values = mSongModelFilter
                } else {
                    val itemModel: ArrayList<SongModel>
                    itemModel = mSongModelFilter.filter {
                        it.mSongName.toLowerCase().contains(charSequence)
                    } as ArrayList<SongModel>
                    filterResults.count = itemModel.size
                    filterResults.values = itemModel
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mSongModel = results!!.values as MutableList<SongModel>
                mMediaPlayer.updateSongList(mSongModel)
                notifyDataSetChanged()
            }
        }
    }
}