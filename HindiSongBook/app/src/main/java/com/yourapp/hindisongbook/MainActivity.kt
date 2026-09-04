package com.yourapp.hindisongbook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var songRepository: SongRepository

    private lateinit var songAdapter: SongAdapter

    private lateinit var searchEditText: EditText

    private lateinit var songsRecyclerView: RecyclerView

    private lateinit var alphabetRecyclerView: RecyclerView

    private lateinit var indexAdapter:
            AlphabetIndexHelper.IndexAdapter

    private var alphabeticalIndex:
            Map<String, List<Song>> = emptyMap()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )

        songRepository = SongRepository(this)

        setupViews()

        setupSearch()

        setupImportExportButton()

        setupAddSongButton()

        refreshSongs()
    }

    override fun onResume() {
        super.onResume()

        if (::songRepository.isInitialized) {
            refreshSongs()
        }
    }

    private fun setupViews() {

        searchEditText =
            findViewById(R.id.searchEditText)

        songsRecyclerView =
            findViewById(R.id.songsRecyclerView)

        alphabetRecyclerView =
            findViewById(R.id.alphabetRecyclerView)

        songsRecyclerView.layoutManager =
            LinearLayoutManager(this)

        songAdapter =
            SongAdapter(emptyList()) { song ->

                startActivity(
                    SongDetailActivity.newIntent(
                        this,
                        song.id
                    )
                )
            }

        songsRecyclerView.adapter =
            songAdapter

        alphabetRecyclerView.layoutManager =
            LinearLayoutManager(this)
    }

    private fun refreshSongs() {

        val songs =
            songRepository.getAllSongs()

        songAdapter.updateSongs(songs)

        alphabeticalIndex =
            songRepository.getAlphabeticalIndex()

        val letters =
            alphabeticalIndex.keys.toList()

        indexAdapter =
            AlphabetIndexHelper.IndexAdapter(
                letters
            ) { letter ->

                scrollToLetter(
                    letter,
                    alphabeticalIndex
                )
            }

        alphabetRecyclerView.adapter =
            indexAdapter

        supportActionBar?.subtitle =
            getString(
                R.string.song_count,
                songs.size
            )
    }

    private fun setupSearch() {

        searchEditText.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {

                    val query =
                        s?.toString()
                            ?.trim()
                            .orEmpty()

                    val results =
                        if (query.isEmpty()) {
                            songRepository.getAllSongs()
                        } else {
                            songRepository.searchSongs(
                                query
                            )
                        }

                    songAdapter.updateSongs(
                        results
                    )
                }
            }
        )
    }

    private fun scrollToLetter(
        letter: String,
        index: Map<String, List<Song>>
    ) {

        val songs =
            index[letter]
                ?: return

        val firstSong =
            songs.firstOrNull()
                ?: return

        val allSongs =
            songRepository.getAllSongs()

        val position =
            allSongs.indexOfFirst {
                it.id == firstSong.id
            }

        if (position >= 0) {

            songsRecyclerView.scrollToPosition(
                position
            )

            Toast.makeText(
                this,
                getString(
                    R.string.letter_song_count,
                    letter,
                    songs.size
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupImportExportButton() {

        findViewById<Button>(
            R.id.importExportButton
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ImportExportActivity::class.java
                )
            )
        }
    }

    private fun setupAddSongButton() {

        findViewById<FloatingActionButton>(
            R.id.fabAddSong
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AdminActivity::class.java
                )
            )
        }
    }
}