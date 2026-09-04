package com.yourapp.hindisongbook

import android.content.Context

class SongRepository(context: Context) {

    private val dbHelper =
        SongDatabaseHelper(context.applicationContext)

    fun getAllSongs(): List<Song> {
        return dbHelper.getAllSongs()
    }

    fun searchSongs(query: String): List<Song> {
        return dbHelper.searchSongs(query)
    }

    fun getSongById(id: Int): Song? {
        return dbHelper.getSongById(id)
    }

    fun getAlphabeticalIndex(): Map<String, List<Song>> {
        return dbHelper.getAlphabeticalIndex()
    }

    fun addSong(
        title: String,
        chorus: String,
        lyrics: String
    ): Long {
        return dbHelper.addSong(
            title,
            chorus,
            lyrics
        )
    }

    fun updateSong(song: Song): Int {
        return dbHelper.updateSong(song)
    }

    fun deleteSong(id: Int): Int {
        return dbHelper.deleteSong(id)
    }

    fun getSongCount(): Int {
        return dbHelper.getSongCount()
    }

    fun exportToJson(context: Context): Boolean {
        return dbHelper.exportToJson(context)
    }

    fun exportToSql(context: Context): Boolean {
        return dbHelper.exportToSql(context)
    }

    fun importFromJson(
        context: Context,
        fileName: String
    ): Boolean {
        return dbHelper.importFromJson(
            context,
            fileName
        )
    }

    fun importFromSql(context: Context): Boolean {
        return dbHelper.importFromSql(context)
    }

    fun getBackupFiles(context: Context): List<String> {
        return dbHelper.getBackupFiles(context)
    }

    fun deleteBackupFile(
        context: Context,
        fileName: String
    ): Boolean {
        return dbHelper.deleteBackupFile(
            context,
            fileName
        )
    }
}