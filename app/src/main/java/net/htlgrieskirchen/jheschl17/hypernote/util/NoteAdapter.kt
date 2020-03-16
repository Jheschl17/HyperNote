package net.htlgrieskirchen.jheschl17.hypernote.util

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.listitem_note.view.*
import net.htlgrieskirchen.jheschl17.hypernote.R
import java.util.function.Predicate

class NoteAdapter(
    private val context: Context,
    private val notes: List<Note>,
    private val filter: Predicate<Note>
) : BaseAdapter() {

    override fun getItem(position: Int): Note {
        return notes
            .filter { filter.test(it) }
            .sortedWith(compareBy({it.completed}, { it.priority.intPriority }, { it.title }))[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return notes.filter { filter.test(it) }.size
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val note = getItem(position)
        val listItem = LayoutInflater.from(context).inflate(R.layout.listitem_note, null)
        listItem.text_notetitle.text = note.title
        if (note.content.length < 30)
            listItem.text_contentpreview.text = note.content
        else
            listItem.text_contentpreview.text = "${note.content.substring(0, 30)}..."
        if (note.priority == Priority.HIGH && !note.completed)
            listItem.text_notetitle.setTextColor(Color.parseColor("#DD1111"))
        if (note.completed) {
            listItem.text_notetitle.setTextColor(Color.parseColor("#B2B2B2"))
            listItem.text_contentpreview.setTextColor(Color.parseColor("#B2B2B2"))
        }
        return listItem
    }

}