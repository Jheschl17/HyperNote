package net.htlgrieskirchen.jheschl17.hypernote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_day.*
import kotlinx.android.synthetic.main.edit_note_dialog.view.*
import net.htlgrieskirchen.jheschl17.hypernote.util.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.function.Predicate
import android.content.DialogInterface
import android.content.Intent

class EditDayActivity : AppCompatActivity() {

    private lateinit var notes: MutableList<Note>
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_day)

        val date = intent.getSerializableExtra("date") as LocalDate
        text_reportday.text = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))

        notes = loadNotesFromFile(CSV_FILE_NAME, this) as MutableList<Note>
        adapter = NoteAdapter(
            this,
            notes,
            Predicate { it.dueDate == date }
        )
        lst_reportnotes.adapter = adapter

        lst_reportnotes.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, NoteDetailsActivity::class.java).apply {
                putExtra("note", noteToCsvString(adapter.getItem(position)))
            }
            startActivity(intent)
        }

        floatingbutton_newnote.setOnClickListener {
            val editNoteDialog = prepareEditDialog(null, "", "", applicationContext)
            AlertDialog.Builder(this)
                .setTitle("Neue Notiz")
                .setView(editNoteDialog)
                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    try {
                        notes.add(
                            Note(
                                title = editNoteDialog.txt_note_name.text.toString(),
                                content = editNoteDialog.txt_note_content.text.toString(),
                                priority = Priority.valueOf(
                                    editNoteDialog.spinner_priority.selectedItem.toString().toUpperCase()
                                ),
                                dueDate = LocalDate.parse(
                                    text_reportday.text.toString(),
                                    DateTimeFormatter.ofPattern(DATE_FORMAT)
                                ),
                                completed = false
                            )
                        )
                        adapter.notifyDataSetChanged()
                    } catch (e: Exception) {e.printStackTrace()}
                })
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveNotesToFile(notes, CSV_FILE_NAME, this)
    }
}
