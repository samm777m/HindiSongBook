package com.yourapp.hindisongbook

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText

    private lateinit var chorusEditText: EditText

    private lateinit var lyricsEditText: EditText

    private lateinit var saveButton: Button

    private lateinit var songRepository: SongRepository

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_admin
        )

        songRepository =
            SongRepository(this)

        titleEditText =
            findViewById(
                R.id.adminTitleEditText
            )

        chorusEditText =
            findViewById(
                R.id.adminChorusEditText
            )

        lyricsEditText =
            findViewById(
                R.id.adminLyricsEditText
            )

        saveButton =
            findViewById(
                R.id.adminSaveButton
            )

        supportActionBar?.title =
            getString(R.string.add_new_song)

        supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )

        saveButton.setOnClickListener {

            val title =
                titleEditText.text
                    .toString()
                    .trim()

            val chorus =
                chorusEditText.text
                    .toString()
                    .trim()

            val lyrics =
                lyricsEditText.text
                    .toString()
                    .trim()

            if (
                title.isEmpty() ||
                chorus.isEmpty() ||
                lyrics.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    R.string.all_fields_required,
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val id =
                songRepository.addSong(
                    title,
                    chorus,
                    lyrics
                )

            if (id > 0) {

                Toast.makeText(
                    this,
                    getString(
                        R.string.song_added,
                        id
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {

                Toast.makeText(
                    this,
                    R.string.song_add_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressedDispatcher.onBackPressed()

        return true
    }
}