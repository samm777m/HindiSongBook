package com.yourapp.hindisongbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImportExportActivity : AppCompatActivity() {

    private lateinit var dbHelper: SongDatabaseHelper

    private lateinit var statusTextView: TextView

    private lateinit var exportJsonButton: Button

    private lateinit var exportSqlButton: Button

    private lateinit var importJsonButton: Button

    private lateinit var importSqlButton: Button

    private lateinit var clearDatabaseButton: Button

    private lateinit var backupListRecyclerView:
            RecyclerView

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_import_export
        )

        dbHelper =
            SongDatabaseHelper(this)

        setupViews()

        setupListeners()

        supportActionBar?.title =
            getString(
                R.string.import_export_database
            )

        supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )

        refreshScreen()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized) {
            refreshScreen()
        }
    }

    private fun setupViews() {

        statusTextView =
            findViewById(
                R.id.exportStatusTextView
            )

        exportJsonButton =
            findViewById(
                R.id.exportJsonButton
            )

        exportSqlButton =
            findViewById(
                R.id.exportSqlButton
            )

        importJsonButton =
            findViewById(
                R.id.importJsonButton
            )

        importSqlButton =
            findViewById(
                R.id.importSqlButton
            )

        clearDatabaseButton =
            findViewById(
                R.id.clearDatabaseButton
            )

        backupListRecyclerView =
            findViewById(
                R.id.backupListRecyclerView
            )

        backupListRecyclerView.layoutManager =
            LinearLayoutManager(this)
    }

    private fun setupListeners() {

        exportJsonButton.setOnClickListener {

            val success =
                dbHelper.exportToJson(this)

            if (success) {

                Toast.makeText(
                    this,
                    R.string.json_export_success,
                    Toast.LENGTH_LONG
                ).show()

            } else {

                Toast.makeText(
                    this,
                    R.string.json_export_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }

            refreshScreen()
        }

        exportSqlButton.setOnClickListener {

            val success =
                dbHelper.exportToSql(this)

            if (success) {

                Toast.makeText(
                    this,
                    R.string.sql_export_success,
                    Toast.LENGTH_LONG
                ).show()

            } else {

                Toast.makeText(
                    this,
                    R.string.sql_export_failed,
                    Toast.LENGTH_SHORT
                ).show()
            }

            refreshScreen()
        }

        importJsonButton.setOnClickListener {

            showFileSelectionDialog(
                "json"
            )
        }

        importSqlButton.setOnClickListener {

            showFileSelectionDialog(
                "sql"
            )
        }

        clearDatabaseButton.setOnClickListener {

            showClearDatabaseConfirmation()
        }
    }

    private fun showFileSelectionDialog(
        extension: String
    ) {

        val files =
            dbHelper.getBackupFiles(this)
                .filter {
                    it.endsWith(
                        ".$extension",
                        ignoreCase = true
                    )
                }

        if (files.isEmpty()) {

            Toast.makeText(
                this,
                getString(
                    R.string.no_backup_files,
                    extension
                ),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        AlertDialog.Builder(this)
            .setTitle(
                R.string.select_backup_file
            )
            .setItems(
                files.toTypedArray()
            ) { _, which ->

                confirmImport(
                    files[which]
                )
            }
            .show()
    }

    private fun confirmImport(
        fileName: String
    ) {

        AlertDialog.Builder(this)
            .setTitle(
                R.string.import_database
            )
            .setMessage(
                R.string.import_replace_message
            )
            .setPositiveButton(
                R.string.yes
            ) { _, _ ->

                val success =
                    if (
                        fileName.endsWith(
                            ".json",
                            ignoreCase = true
                        )
                    ) {

                        dbHelper.importFromJson(
                            this,
                            fileName
                        )

                    } else {

                        dbHelper.importFromSql(
                            this
                        )
                    }

                Toast.makeText(
                    this,
                    if (success) {
                        R.string.import_success
                    } else {
                        R.string.import_failed
                    },
                    Toast.LENGTH_LONG
                ).show()

                refreshScreen()
            }
            .setNegativeButton(
                R.string.cancel,
                null
            )
            .show()
    }

    private fun showClearDatabaseConfirmation() {

        AlertDialog.Builder(this)
            .setTitle(
                R.string.clear_database
            )
            .setMessage(
                R.string.clear_database_message
            )
            .setPositiveButton(
                R.string.delete_all
            ) { _, _ ->

                dbHelper.writableDatabase.delete(
                    "songs",
                    null,
                    null
                )

                Toast.makeText(
                    this,
                    R.string.all_songs_deleted,
                    Toast.LENGTH_SHORT
                ).show()

                refreshScreen()
            }
            .setNegativeButton(
                R.string.cancel,
                null
            )
            .show()
    }

    private fun refreshScreen() {

        statusTextView.text =
            getString(
                R.string.total_songs,
                dbHelper.getSongCount()
            )

        loadBackupList()
    }

    private fun loadBackupList() {

        val files =
            dbHelper.getBackupFiles(this)

        backupListRecyclerView.adapter =
            BackupAdapter(files) { fileName ->

                showBackupOptions(
                    fileName
                )
            }
    }

    private fun showBackupOptions(
        fileName: String
    ) {

        AlertDialog.Builder(this)
            .setTitle(
                R.string.backup_options
            )
            .setItems(
                arrayOf(
                    getString(
                        R.string.import_this_file
                    ),
                    getString(
                        R.string.delete_this_file
                    )
                )
            ) { _, which ->

                when (which) {

                    0 -> confirmImport(
                        fileName
                    )

                    1 -> {

                        val deleted =
                            dbHelper.deleteBackupFile(
                                this,
                                fileName
                            )

                        if (deleted) {

                            Toast.makeText(
                                this,
                                R.string.file_deleted,
                                Toast.LENGTH_SHORT
                            ).show()

                            refreshScreen()
                        }
                    }
                }
            }
            .show()
    }

    inner class BackupAdapter(
        private val files: List<String>,
        private val onFileClick:
            (String) -> Unit
    ) : RecyclerView.Adapter<
            BackupAdapter.BackupViewHolder>() {

        inner class BackupViewHolder(
            itemView: View
        ) : RecyclerView.ViewHolder(itemView) {

            val fileNameTextView: TextView =
                itemView.findViewById(
                    R.id.backupFileNameTextView
                )

            val fileSizeTextView: TextView =
                itemView.findViewById(
                    R.id.backupFileSizeTextView
                )

            val fileDateTextView: TextView =
                itemView.findViewById(
                    R.id.backupFileDateTextView
                )
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BackupViewHolder {

            val view =
                LayoutInflater
                    .from(parent.context)
                    .inflate(
                        R.layout.item_backup_file,
                        parent,
                        false
                    )

            return BackupViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: BackupViewHolder,
            position: Int
        ) {

            val fileName =
                files[position]

            val file =
                File(
                    applicationContext
                        .getExternalFilesDir(null),
                    "exports/$fileName"
                )

            holder.fileNameTextView.text =
                fileName

            holder.fileSizeTextView.text =
                formatFileSize(
                    file.length()
                )

            holder.fileDateTextView.text =
                formatDate(
                    file.lastModified()
                )

            holder.itemView.setOnClickListener {

                onFileClick(fileName)
            }
        }

        override fun getItemCount(): Int {
            return files.size
        }

        private fun formatFileSize(
            size: Long
        ): String {

            return when {

                size < 1024 ->
                    "$size B"

                size < 1024 * 1024 ->
                    "${size / 1024} KB"

                else ->
                    "${size / (1024 * 1024)} MB"
            }
        }

        private fun formatDate(
            timestamp: Long
        ): String {

            return SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
            ).format(
                Date(timestamp)
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressedDispatcher.onBackPressed()

        return true
    }
}