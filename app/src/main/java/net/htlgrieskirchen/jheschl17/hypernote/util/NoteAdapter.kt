package net.htlgrieskirchen.jheschl17.hypernote.util

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.listitem_note.view.*
import net.htlgrieskirchen.jheschl17.hypernote.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.function.Predicate

class NoteAdapter(
    private val context: Context,
    private val filePath: String,
    private val filter: Predicate<Note>
) : BaseAdapter() {

    override fun getItem(position: Int): Note {
        return loadNotesFromFile(filePath, context)
            .filter { filter.test(it) }
            .sortedWith(Comparator { n1, n2 -> n1.priority.intPriority - n2.priority.intPriority })[position] // TODO
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return loadNotesFromFile(filePath, context).filter { filter.test(it) }.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val note = getItem(position)
        val listItem = LayoutInflater.from(context).inflate(R.layout.listitem_note, null)
        listItem.text_notetitle.text = note.title
        listItem.text_contentpreview.text = note.content.substring(0, 30)
        if (note.priority == Priority.HIGH)
            listItem.setBackgroundColor(Color.RED)
        return listItem
    }

}