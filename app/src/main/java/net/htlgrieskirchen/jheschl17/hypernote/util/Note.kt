package net.htlgrieskirchen.jheschl17.hypernote.util

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.core.content.PermissionChecker

data class Note (
    val title: String,
    val content: String,
    val priority: Priority,
    val dueDate: LocalDate,
    val completed: Boolean,
    val category: String
)

fun noteFromSerializationString(csv: String): Note {
    val elements = csv.split(";")
    return Note(
        title = elements[0],
        content = elements[1],
        priority = Priority.valueOf(elements[2]),
        dueDate = LocalDate.parse(elements[3], DateTimeFormatter.ofPattern(DATE_FORMAT)),
        completed = elements[4].toBoolean(),
        category = elements[5]
    )
}

fun noteToSerializationString(note: Note): String {
    return "${note.title};${note.content};${note.priority};" +
           "${note.dueDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))};" +
           "${note.completed};${note.category}"
}

@Deprecated("use net.htlgrieskirchen.jheschl17.hypernote.cloud.SaveManager.loadNotes instead")
fun loadNotesFromFile(activity: Activity): List<Note> { // TODO remove this
    if (checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PermissionChecker.PERMISSION_GRANTED) {
        requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1337
        )
    }

    val file = File(NOTE_FILE_PATH)
    return if (file.exists())
        registerLocalDate(GsonBuilder()).create().fromJson(
            BufferedReader(FileReader(file)),
            object:TypeToken<ArrayList<Note>>() {}.type
        )
    else
        mutableListOf()
}

@Deprecated("use net.htlgrieskirchen.jheschl17.hypernote.cloud.SaveManager.saveNotes instead")
fun saveNotesToFile(notes: List<Note>, activity: Activity) { // TODO remove this
    if (checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PermissionChecker.PERMISSION_GRANTED) {
        requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1337
        )
    }
    val br = PrintWriter(OutputStreamWriter(FileOutputStream(NOTE_FILE_PATH)))
    br.print(
        registerLocalDate(GsonBuilder()).create().toJson(notes)
    )
    br.flush()
    br.close()
}