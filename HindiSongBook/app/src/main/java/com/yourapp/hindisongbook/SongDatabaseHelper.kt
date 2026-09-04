package com.yourapp.hindisongbook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class SongDatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context.applicationContext,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {
        private const val DATABASE_NAME = "hindi_songs.db"
        private const val DATABASE_VERSION = 3

        private const val TABLE_NAME = "songs"

        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CHORUS = "chorus"
        private const val COLUMN_LYRICS = "lyrics"

        private const val EXPORT_DIRECTORY = "exports"

        private const val JSON_FILE_NAME = "hindi_songs_backup.json"
        private const val SQL_FILE_NAME = "hindi_songs_backup.sql"
    }

    override fun onCreate(db: SQLiteDatabase) {
        createSongsTable(db)
        insertSampleData(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        /*
         * IMPORTANT:
         * Never drop the songs table during a normal application upgrade.
         *
         * Future schema changes should be added here as migrations.
         */
        if (oldVersion < 3) {
            /*
             * Version 3 does not require a schema change.
             *
             * This version exists to replace the old destructive
             * upgrade implementation.
             */
        }
    }

    private fun createSongsTable(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_CHORUS TEXT NOT NULL,
                $COLUMN_LYRICS TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    private fun insertSampleData(db: SQLiteDatabase) {

        val sampleSongs = listOf(
            Song(
                1,
                "Aankhon Mein Tera Hi Chehra",
                "Aankhon mein tera hi chehra hai",
                """
                Aankhon mein tera hi chehra hai
                Dil mein teri hi dhun hai
                Kya khoobsurat yeh lamha hai
                Tere bina ab jeena nahi
                """.trimIndent()
            ),

            Song(
                2,
                "Bole Chudiyan",
                "Bole chudiyan bole kangna",
                """
                Bole chudiyan bole kangna
                Haaye bole chudiyan
                Kisi ko deewana karein
                """.trimIndent()
            ),

            Song(
                3,
                "Channa Mereya",
                "Channa mereya mereya",
                """
                Channa mereya mereya
                Ve tu vi na ja
                Kyun main jaana
                """.trimIndent()
            ),

            Song(
                4,
                "Dil Diya Gallan",
                "Dil diya gallan kare",
                """
                Dil diya gallan kare
                Haaye dil diya
                Kaisi yeh teri khudai
                """.trimIndent()
            ),

            Song(
                5,
                "Ek Ladki Ko Dekha Toh Aisa Laga",
                "Ek ladki ko dekha toh aisa laga",
                """
                Ek ladki ko dekha toh aisa laga
                Jaise khilta hua gulaab
                Jaise surma sooni
                """.trimIndent()
            ),

            Song(
                6,
                "Gerua",
                "Gerua gerua gerua",
                """
                Gerua gerua gerua
                Tere sang yaara
                Khushbu mein teri main sama
                """.trimIndent()
            ),

            Song(
                7,
                "Hawayein",
                "Hawayein hawayein",
                """
                Hawayein hawayein
                Le jaaye mujhe
                Tere khwabon mein
                """.trimIndent()
            ),

            Song(
                8,
                "Ishq Wala Love",
                "Ishq wala love",
                """
                Ishq wala love
                Dil pe kya hai
                Tere liye main deewana
                """.trimIndent()
            ),

            Song(
                9,
                "Jai Ho",
                "Jai ho jai ho",
                """
                Jai ho jai ho
                Tujhe salaam
                Mere desh ka
                """.trimIndent()
            ),

            Song(
                10,
                "Kala Chashma",
                "Kala chashma kala chashma",
                """
                Kala chashma kala chashma
                Teri main hoon
                Tera main hoon
                """.trimIndent()
            ),

            Song(
                11,
                "London Thumakda",
                "London thumakda",
                """
                London thumakda
                Dilli thumakda
                Sara India thumakda
                """.trimIndent()
            ),

            Song(
                12,
                "Mere Rashke Qamar",
                "Mere rashke qamar",
                """
                Mere rashke qamar
                Tujhe dekha toh
                Khuda yaad aaya
                """.trimIndent()
            ),

            Song(
                13,
                "Nazm Nazm",
                "Nazm nazm aaye",
                """
                Nazm nazm aaye
                Tere ishq mein
                Main kya karun
                """.trimIndent()
            ),

            Song(
                14,
                "O Rangrez",
                "O rangrez re",
                """
                O rangrez re
                Tere ishq mein
                Rang jaun main
                """.trimIndent()
            ),

            Song(
                15,
                "Pachtaoge",
                "Pachtaoge pachtaoge",
                """
                Pachtaoge pachtaoge
                Tere liye main
                Chhod doon duniya
                """.trimIndent()
            ),

            Song(
                16,
                "Qaafirana",
                "Qaafirana qaafirana",
                """
                Qaafirana qaafirana
                Tere ishq mein
                Ho gaya deewana
                """.trimIndent()
            ),

            Song(
                17,
                "Raabta",
                "Raabta raabta",
                """
                Raabta raabta
                Tere ishq mein
                Main kahin bhi jaun
                """.trimIndent()
            ),

            Song(
                18,
                "Saware",
                "Saware saware",
                """
                Saware saware
                Tere ishq mein
                Main khoya rahun
                """.trimIndent()
            ),

            Song(
                19,
                "Tum Hi Ho",
                "Tum hi ho ab tum hi ho",
                """
                Tum hi ho ab tum hi ho
                Tere liye main
                Jiya main mara
                """.trimIndent()
            ),

            Song(
                20,
                "Udi Udi",
                "Udi udi jab hawa",
                """
                Udi udi jab hawa
                Tere ishq mein
                Main uda rahun
                """.trimIndent()
            )
        )

        sampleSongs.forEach { song ->

            val values = ContentValues().apply {
                put(COLUMN_TITLE, song.title)
                put(COLUMN_CHORUS, song.chorus)
                put(COLUMN_LYRICS, song.lyrics)
            }

            db.insert(TABLE_NAME, null, values)
        }
    }

    private fun cursorToSong(
        cursor: android.database.Cursor
    ): Song {

        return Song(
            id = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_ID)
            ),

            title = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_TITLE)
            ),

            chorus = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_CHORUS)
            ),

            lyrics = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_LYRICS)
            )
        )
    }

    fun getAllSongs(): List<Song> {

        val songs = mutableListOf<Song>()

        readableDatabase.query(
            TABLE_NAME,

            arrayOf(
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_CHORUS,
                COLUMN_LYRICS
            ),

            null,
            null,
            null,
            null,

            "$COLUMN_TITLE COLLATE NOCASE ASC"
        ).use { cursor ->

            while (cursor.moveToNext()) {
                songs.add(cursorToSong(cursor))
            }
        }

        return songs
    }

    fun searchSongs(query: String): List<Song> {

        val search = query.trim()

        if (search.isEmpty()) {
            return getAllSongs()
        }

        val songs = mutableListOf<Song>()

        val selection =
            if (search.toIntOrNull() != null) {

                "$COLUMN_ID = ? OR " +
                        "$COLUMN_TITLE LIKE ? OR " +
                        "$COLUMN_CHORUS LIKE ? OR " +
                        "$COLUMN_LYRICS LIKE ?"

            } else {

                "$COLUMN_TITLE LIKE ? OR " +
                        "$COLUMN_CHORUS LIKE ? OR " +
                        "$COLUMN_LYRICS LIKE ?"
            }

        val selectionArgs =
            if (search.toIntOrNull() != null) {

                arrayOf(
                    search,
                    "%$search%",
                    "%$search%",
                    "%$search%"
                )

            } else {

                arrayOf(
                    "%$search%",
                    "%$search%",
                    "%$search%"
                )
            }

        readableDatabase.query(
            TABLE_NAME,

            arrayOf(
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_CHORUS,
                COLUMN_LYRICS
            ),

            selection,
            selectionArgs,

            null,
            null,

            "$COLUMN_TITLE COLLATE NOCASE ASC"
        ).use { cursor ->

            while (cursor.moveToNext()) {
                songs.add(cursorToSong(cursor))
            }
        }

        return songs
    }

    fun getSongById(id: Int): Song? {

        readableDatabase.query(
            TABLE_NAME,

            arrayOf(
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_CHORUS,
                COLUMN_LYRICS
            ),

            "$COLUMN_ID = ?",

            arrayOf(id.toString()),

            null,
            null,
            null
        ).use { cursor ->

            if (cursor.moveToFirst()) {
                return cursorToSong(cursor)
            }
        }

        return null
    }

    fun getAlphabeticalIndex(): Map<String, List<Song>> {

        return getAllSongs()
            .filter { it.title.isNotBlank() }
            .groupBy {
                it.title
                    .trim()
                    .substring(0, 1)
                    .uppercase()
            }
            .toSortedMap()
    }

    fun addSong(
        title: String,
        chorus: String,
        lyrics: String
    ): Long {

        val values = ContentValues().apply {
            put(COLUMN_TITLE, title.trim())
            put(COLUMN_CHORUS, chorus.trim())
            put(COLUMN_LYRICS, lyrics.trim())
        }

        return writableDatabase.insert(
            TABLE_NAME,
            null,
            values
        )
    }

    fun updateSong(song: Song): Int {

        val values = ContentValues().apply {
            put(COLUMN_TITLE, song.title.trim())
            put(COLUMN_CHORUS, song.chorus.trim())
            put(COLUMN_LYRICS, song.lyrics.trim())
        }

        return writableDatabase.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(song.id.toString())
        )
    }

    fun deleteSong(id: Int): Int {

        return writableDatabase.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getSongCount(): Int {

        readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_NAME",
            null
        ).use { cursor ->

            if (cursor.moveToFirst()) {
                return cursor.getInt(0)
            }
        }

        return 0
    }

    // ---------------------------------------------------------
    // EXPORT
    // ---------------------------------------------------------

    private fun getExportDirectory(context: Context): File {

        val directory = File(
            context.getExternalFilesDir(null),
            EXPORT_DIRECTORY
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        return directory
    }

    fun exportToJson(
        context: Context,
        fileName: String = JSON_FILE_NAME
    ): Boolean {

        return try {

            val jsonArray = JSONArray()

            getAllSongs().forEach { song ->

                val jsonObject = JSONObject().apply {
                    put("id", song.id)
                    put("title", song.title)
                    put("chorus", song.chorus)
                    put("lyrics", song.lyrics)
                }

                jsonArray.put(jsonObject)
            }

            val file = File(
                getExportDirectory(context),
                fileName
            )

            file.writeText(
                jsonArray.toString(2),
                Charsets.UTF_8
            )

            true

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }

    fun exportToSql(context: Context): Boolean {

        return try {

            val file = File(
                getExportDirectory(context),
                SQL_FILE_NAME
            )

            BufferedWriter(
                FileWriter(file, Charsets.UTF_8)
            ).use { writer ->

                writer.write(
                    """
                    BEGIN TRANSACTION;

                    CREATE TABLE IF NOT EXISTS songs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        chorus TEXT NOT NULL,
                        lyrics TEXT NOT NULL
                    );

                    """.trimIndent()
                )

                getAllSongs().forEach { song ->

                    val title = escapeSql(song.title)
                    val chorus = escapeSql(song.chorus)
                    val lyrics = escapeSql(song.lyrics)

                    writer.write(
                        "INSERT OR REPLACE INTO songs " +
                                "(id, title, chorus, lyrics) VALUES " +
                                "(${song.id}, '$title', '$chorus', '$lyrics');\n"
                    )
                }

                writer.write("\nCOMMIT;\n")
            }

            true

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }

    private fun escapeSql(value: String): String {
        return value.replace("'", "''")
    }

    // ---------------------------------------------------------
    // IMPORT JSON
    // ---------------------------------------------------------

    fun importFromJson(
        context: Context,
        fileName: String
    ): Boolean {

        return try {

            val file = File(
                getExportDirectory(context),
                fileName
            )

            if (!file.exists()) {
                return false
            }

            val jsonArray = JSONArray(
                file.readText(Charsets.UTF_8)
            )

            val db = writableDatabase

            db.beginTransaction()

            try {

                db.delete(
                    TABLE_NAME,
                    null,
                    null
                )

                for (i in 0 until jsonArray.length()) {

                    val obj = jsonArray.getJSONObject(i)

                    val values = ContentValues().apply {

                        if (obj.has("id")) {
                            put(
                                COLUMN_ID,
                                obj.getInt("id")
                            )
                        }

                        put(
                            COLUMN_TITLE,
                            obj.optString("title")
                        )

                        put(
                            COLUMN_CHORUS,
                            obj.optString("chorus")
                        )

                        put(
                            COLUMN_LYRICS,
                            obj.optString("lyrics")
                        )
                    }

                    db.insertOrThrow(
                        TABLE_NAME,
                        null,
                        values
                    )
                }

                db.setTransactionSuccessful()

                true

            } finally {

                db.endTransaction()
            }

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }

    // ---------------------------------------------------------
    // IMPORT SQL
    // ---------------------------------------------------------

    fun importFromSql(context: Context): Boolean {

        return try {

            val file = File(
                getExportDirectory(context),
                SQL_FILE_NAME
            )

            if (!file.exists()) {
                return false
            }

            val db = writableDatabase

            db.beginTransaction()

            try {

                /*
                 * The SQL export generated by this application uses
                 * simple INSERT statements. Instead of blindly splitting
                 * on semicolons, execute complete statements while
                 * respecting quoted strings.
                 */

                val statements = splitSqlStatements(
                    file.readText(Charsets.UTF_8)
                )

                statements.forEach { statement ->

                    val sql = statement.trim()

                    if (sql.isNotEmpty()) {

                        val upper = sql.uppercase()

                        if (
                            upper.startsWith("BEGIN") ||
                            upper.startsWith("COMMIT") ||
                            upper.startsWith("ROLLBACK")
                        ) {
                            return@forEach
                        }

                        db.execSQL(sql)
                    }
                }

                db.setTransactionSuccessful()

                true

            } finally {

                db.endTransaction()
            }

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }

    private fun splitSqlStatements(
        sql: String
    ): List<String> {

        val statements = mutableListOf<String>()

        val current = StringBuilder()

        var insideString = false

        var index = 0

        while (index < sql.length) {

            val char = sql[index]

            if (char == '\'') {

                current.append(char)

                if (
                    insideString &&
                    index + 1 < sql.length &&
                    sql[index + 1] == '\''
                ) {
                    current.append('\'')
                    index += 2
                    continue
                }

                insideString = !insideString

            } else if (char == ';' && !insideString) {

                val statement = current.toString().trim()

                if (statement.isNotEmpty()) {
                    statements.add(statement)
                }

                current.clear()

            } else {

                current.append(char)
            }

            index++
        }

        val lastStatement = current.toString().trim()

        if (lastStatement.isNotEmpty()) {
            statements.add(lastStatement)
        }

        return statements
    }

    // ---------------------------------------------------------
    // BACKUP FILES
    // ---------------------------------------------------------

    fun getBackupFiles(context: Context): List<String> {

        val directory = getExportDirectory(context)

        return directory
            .listFiles()
            ?.filter {
                it.isFile &&
                        (
                                it.extension.equals(
                                    "json",
                                    ignoreCase = true
                                ) ||
                                it.extension.equals(
                                    "sql",
                                    ignoreCase = true
                                )
                                )
            }
            ?.map { it.name }
            ?.sorted()
            ?: emptyList()
    }

    fun deleteBackupFile(
        context: Context,
        fileName: String
    ): Boolean {

        return try {

            val directory = getExportDirectory(context)

            val file = File(
                directory,
                fileName
            )

            file.canonicalPath.startsWith(
                directory.canonicalPath + File.separator
            ) && file.delete()

        } catch (e: Exception) {

            e.printStackTrace()
            false
        }
    }
}