package net.htlgrieskirchen.jheschl17.hypernote.ui.settings

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import net.htlgrieskirchen.jheschl17.hypernote.R

class SettingsFragment : Fragment() {

    private lateinit var view1: View
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_settings, container, false)

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.view_settings, SettingsFragmentCompat())
            .commit()

        return view1
    }

}
