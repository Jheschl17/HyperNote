package net.htlgrieskirchen.jheschl17.hypernote.util

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

data class Note(
    val title: String,
    val content: String,
    val priority: Priority,
    val dueDate: LocalDate
)

fun noteFromCsvString(csv: String): Note {
    val elements = csv.split(";")
    return Note(
        title = elements[0],
        content = elements[1],
        priority = Priority.valueOf(elements[2]),
        dueDate = LocalDate.parse(elements[3], DateTimeFormatter.ofPattern(DATE_FORMAT))
    )
}

fun noteToCsvString(note: Note): String {
    return "${note.title};${note.content};${note.priority}" +
            note.dueDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
}

fun loadNotesFromFile(fileName: String, ctxt: Context): List<Note> {
    val file = ctxt.getFileStreamPath(fileName)
    return if (file.exists())
        BufferedReader(InputStreamReader(ctxt.openFileInput(fileName))).lines()
            .map { noteFromCsvString(it) }
            .collect(Collectors.toList())
    else
        mutableListOf()
}

fun saveNotesToFile(notes: List<Note>, fileName: String, context: Context) {
    val br = PrintWriter(OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE)))
    notes.forEach { br.println(noteToCsvString(it)) }
    br.flush()
    br.close()
}