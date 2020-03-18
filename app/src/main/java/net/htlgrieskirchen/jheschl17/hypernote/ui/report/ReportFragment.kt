package net.htlgrieskirchen.jheschl17.hypernote.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.fragment_report.view.lst_reportnotes
import net.htlgrieskirchen.jheschl17.hypernote.NoteDetailsActivity
import net.htlgrieskirchen.jheschl17.hypernote.R
import net.htlgrieskirchen.jheschl17.hypernote.util.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Predicate

class ReportFragment : Fragment() {

    private lateinit var view1: View
    private lateinit var notes: MutableList<Note>
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_report, container, false)

        view1.text_reportday.text = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern(DATE_FORMAT)
        )

        notes = loadNotesFromFile(CSV_FILE_NAME, requireContext()) as MutableList<Note>
        adapter = NoteAdapter(
            requireContext(),
            notes,
            Predicate { it.dueDate == LocalDate.now() },
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        view1.lst_reportnotes.adapter = this.adapter

        registerForContextMenu(view1.lst_reportnotes)

        view1.lst_reportnotes.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java).apply {
                putExtra("note", noteToSerializationString(adapter.getItem(position)))
            }
            startActivity(intent)
        }

        return view1
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.contextmenu_notes, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val listviewIndex = (item.menuInfo as AdapterView.AdapterContextMenuInfo?)?.position ?: -1
        val viewIndex = notes.indexOf(adapter.getItem(listviewIndex))
        when (item.itemId) {
            R.id.menu_postpone -> {
                notes[viewIndex] = notes[viewIndex].copy(dueDate = notes[viewIndex].dueDate.plusDays(1))
            }
            R.id.menu_mark_as_finished -> {
                notes[viewIndex] = notes[viewIndex].copy(completed = true)
            }
        }
        saveNotesToFile(notes, CSV_FILE_NAME, requireContext())
        adapter.notifyDataSetChanged()
        return true
    }
}
