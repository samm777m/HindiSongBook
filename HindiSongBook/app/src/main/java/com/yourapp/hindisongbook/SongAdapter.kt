package com.yourapp.hindisongbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongAdapter(
    private var songs: List<Song>,
    private val onSongClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView =
            itemView.findViewById(R.id.songTitleTextView)

        val chorusTextView: TextView =
            itemView.findViewById(R.id.songChorusTextView)

        val idTextView: TextView =
            itemView.findViewById(R.id.songIdTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)

        return SongViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int
    ) {
        val song = songs[position]

        holder.titleTextView.text = song.title
        holder.chorusTextView.text = song.chorus
        holder.idTextView.text = "#${song.id}"

        holder.itemView.setOnClickListener {
            onSongClick(song)
        }
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun updateSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}
