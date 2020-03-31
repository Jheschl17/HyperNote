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
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.preference.PreferenceManager

class EditDayActivity : AppCompatActivity() {

    private lateinit var notes: MutableList<Note>
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_day)

        val date = intent.getSerializableExtra("date") as LocalDate
        text_reportday.text = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))

        notes = loadNotesFromFile(NOTE_FILE_PATH, this) as MutableList<Note>
        adapter = NoteAdapter(
            this,
            notes,
            Predicate { it.dueDate == date },
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        lst_reportnotes.adapter = adapter

        registerForContextMenu(lst_reportnotes)

        lst_reportnotes.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, NoteDetailsActivity::class.java).apply {
                putExtra("note", noteToSerializationString(adapter.getItem(position)))
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
                                completed = false,
                                category = editNoteDialog.txt_note_category.text.toString()
                            )
                        )
                        adapter.notifyDataSetChanged()
                    } catch (e: Exception) {e.printStackTrace()}
                })
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.contextmenu_editday, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val listviewIndex = (item.menuInfo as AdapterView.AdapterContextMenuInfo?)?.position ?: -1
        return when (item.itemId) {
            R.id.menu_delete -> {
                notes.remove(adapter.getItem(listviewIndex))
                saveNotesToFile(notes, NOTE_FILE_PATH, this)
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveNotesToFile(notes, NOTE_FILE_PATH, this)
    }
}
