package net.htlgrieskirchen.jheschl17.hypernote

import android.app.Activity
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import net.htlgrieskirchen.jheschl17.hypernote.cloud.api.getTodo
import net.htlgrieskirchen.jheschl17.hypernote.cloud.loadNotes
import java.time.LocalDate

class NotificationService : Thread {

    private lateinit var activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    override fun run() {
        sleep(5000)

        val prefMngr = PreferenceManager.getDefaultSharedPreferences(activity)
        val username = prefMngr.getString("username", null)
        val password = prefMngr.getString("password", null)
        if (username == null || password == null)
            throw java.lang.Exception("username of password have value null")
        val todos = loadNotes(activity, username, password)
        todos.forEach {
            val note = Location("noteLocation")
            note.latitude = it.lat
            note.longitude = it.lon
            val self = getLocation(activity)!!
            if (it.dueDate == LocalDate.now() || self.distanceTo(note) <= 1000)
                NotificationCompat.Builder(activity)
                    .setContentTitle("NoteIfication")
                    .setContentText("Note: ${it.title} Priority: ${it.priority}")
        }
    }

}