package net.htlgrieskirchen.jheschl17.hypernote.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import net.htlgrieskirchen.jheschl17.hypernote.R

class SettingsFragment : Fragment() {

    private lateinit var view1: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_settings, container, false)
        return view1
    }
}
