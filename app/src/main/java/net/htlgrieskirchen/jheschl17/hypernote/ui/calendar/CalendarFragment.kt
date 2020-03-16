package net.htlgrieskirchen.jheschl17.hypernote.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import net.htlgrieskirchen.jheschl17.hypernote.EditDayActivity
import net.htlgrieskirchen.jheschl17.hypernote.R
import java.time.LocalDate

class CalendarFragment : Fragment() {

    private lateinit var view1: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_calendar, container, false)
        
        view1.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(requireContext(), EditDayActivity::class.java).apply {
                putExtra("date", LocalDate.of(year, month + 1, dayOfMonth))
            }
            startActivity(intent)
        }
        
        return view1
    }
}
