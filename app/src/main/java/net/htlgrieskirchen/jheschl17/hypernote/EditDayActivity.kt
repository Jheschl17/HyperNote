package net.htlgrieskirchen.jheschl17.hypernote

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_edit_day.*
import kotlinx.android.synthetic.main.edit_note_dialog.view.*
import net.htlgrieskirchen.jheschl17.hypernote.cloud.loadNotes
import net.htlgrieskirchen.jheschl17.hypernote.cloud.saveNotes
import net.htlgrieskirchen.jheschl17.hypernote.util.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.function.Predicate
import kotlin.system.exitProcess

class EditDayActivity : AppCompatActivity() {

    private lateinit var notes: MutableList<Note>
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_day)

        val date = intent.getSerializableExtra("date") as LocalDate
        text_reportday.text = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT))

        val prefMngr = PreferenceManager.getDefaultSharedPreferences(this)
        val username = prefMngr.getString("username", null)
        val password = prefMngr.getString("password", null)
        if (username == null || password == null)
            throw java.lang.Exception("username of password have value null")

        notes = loadNotes(this, username, password) as MutableList<Note>
        adapter = NoteAdapter(
            this,
            notes,
            Predicate { it.dueDate == date },
            prefMngr
        )
        lst_reportnotes.adapter = adapter

        registerForContextMenu(lst_reportnotes)

        lst_reportnotes.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, NoteDetailsActivity::class.java).apply {
                putExtra("note", noteToSerializationString(adapter.getItem(position)))
            }
            startActivity(intent)
        }

        val location = getLocation(this)
        if (location == null) {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_LONG).show()
            this.finishAffinity()
        }
        floatingbutton_newnote.setOnClickListener {
            val editNoteDialog = prepareEditDialog(null, "", "", applicationContext)
            AlertDialog.Builder(this)
                .setTitle("Neue Notiz")
                .setView(editNoteDialog)
                .setPositiveButton("OK") { _, _ ->
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
                                category = editNoteDialog.txt_note_category.text.toString(),
                                lon = location!!.longitude,
                                lat = location.latitude
                            )
                        )
                        adapter.notifyDataSetChanged()
                    } catch (e: Exception) {e.printStackTrace()}
                }
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

                val prefMngr = PreferenceManager.getDefaultSharedPreferences(this)
                val username = prefMngr.getString("username", null)
                val password = prefMngr.getString("password", null)
                if (username == null || password == null)
                    throw java.lang.Exception("username of password have value null")
                saveNotes(notes, this, username, password)

                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefMngr = PreferenceManager.getDefaultSharedPreferences(this)
        val username = prefMngr.getString("username", null)
        val password = prefMngr.getString("password", null)
        if (username == null || password == null)
            throw java.lang.Exception("username of password have value null")
        saveNotes(notes, this, username, password)
    }
}

fun getLocation(activity: Activity): Location? {
    val locationManager = activity.getSystemService(LocationManager::class.java)
    if (ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        activity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 54121)
    }

    return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
}
