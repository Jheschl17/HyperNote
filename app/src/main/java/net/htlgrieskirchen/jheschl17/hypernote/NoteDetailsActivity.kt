package net.htlgrieskirchen.jheschl17.hypernote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_note_details.*
import net.htlgrieskirchen.jheschl17.hypernote.util.DATE_FORMAT
import net.htlgrieskirchen.jheschl17.hypernote.util.noteFromCsvString
import java.time.format.DateTimeFormatter

class NoteDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val note = noteFromCsvString(intent.getStringExtra("note") as String)

        text_note_title.text = note.title
        text_due_date.text = note.dueDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
        text_done_status.text = note.completed.toString()
        text_priority.text = note.priority.name.capitalize()
        text_content.text = note.content
    }
}
