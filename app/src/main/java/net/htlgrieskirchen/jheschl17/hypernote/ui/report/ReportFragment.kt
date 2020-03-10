package net.htlgrieskirchen.jheschl17.hypernote.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import net.htlgrieskirchen.jheschl17.hypernote.R

class ReportFragment : Fragment() {

    private lateinit var view1: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_report, container, false)
        return view1
    }
}
