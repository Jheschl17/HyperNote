package net.htlgrieskirchen.jheschl17.hypernote.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.details_note_dialog.view.*
import kotlinx.android.synthetic.main.edit_note_dialog.view.*
import net.htlgrieskirchen.jheschl17.hypernote.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun prepareEditDialog(date: LocalDate?, name: String, content: String, ctxt: Context): View {
    val dialog = LayoutInflater.from(ctxt).inflate(R.layout.edit_note_dialog, null)
    dialog.txt_note_name.setText(name)
    dialog.txt_note_content.setText(content)
    return dialog
}

fun prepareDetailsDialog(date: LocalDate?, name: String, content: String, ctxt: Context): View {
    val dialog = LayoutInflater.from(ctxt).inflate(R.layout.details_note_dialog, null)
    dialog.details_date_note_date.text = date?.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) ?: ""
    dialog.details_txt_note_name.text = name
    dialog.details_txt_note_content.text = content
    return dialog
}