package net.htlgrieskirchen.jheschl17.hypernote.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import net.htlgrieskirchen.jheschl17.hypernote.MainActivity
import net.htlgrieskirchen.jheschl17.hypernote.R


class SettingsFragmentCompat : PreferenceFragmentCompat() {

    private lateinit var preferencesChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}