package net.htlgrieskirchen.jheschl17.hypernote

import android.annotation.SuppressLint
import android.app.Activity
import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import net.htlgrieskirchen.jheschl17.hypernote.cloud.api.getTodo
import net.htlgrieskirchen.jheschl17.hypernote.cloud.loadNotes
import net.htlgrieskirchen.jheschl17.hypernote.util.NOTIFICATION_CHANNEL_LOCATION
import net.htlgrieskirchen.jheschl17.hypernote.util.noteToSerializationString
import java.lang.Thread.sleep
import java.time.LocalDate
import kotlin.concurrent.thread

class NotificationService : IntentService("NotificationService") {

    @SuppressLint("MissingPermission")
    override fun onHandleIntent(intent: Intent?) {
        val locationManager = getSystemService(LocationManager::class.java)
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            4000,
            0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    Log.i(
                        this@NotificationService::class.qualifiedName,
                        "location changed to $location"
                    )
                    val prefMngr =
                        PreferenceManager.getDefaultSharedPreferences(this@NotificationService)
                    val username = prefMngr.getString("username", null)
                    val password = prefMngr.getString("password", null)
                    if (username == null || password == null)
                        throw java.lang.Exception("username of password have value null")

                    val todos = loadNotes(this@NotificationService, username, password)

                    todos.forEach {
                        val note = Location("noteLocation")
                        note.latitude = it.lat
                        note.longitude = it.lon
                        if (location != null)
                            if (it.dueDate == LocalDate.now() &&
                                location.distanceTo(note) <= 50 &&
                                prefMngr.getBoolean("preference_checkbox_notifications", true)
                            ) {
                                val contentIntent = Intent(this@NotificationService, NoteDetailsActivity::class.java).apply {
                                    putExtra("note", noteToSerializationString(it))
                                }
                                val notification =
                                    NotificationCompat.Builder(this@NotificationService, NOTIFICATION_CHANNEL_LOCATION)
                                        .setContentTitle("Notification")
                                        .setContentText("Note: ${it.title}  Priority: ${it.priority}")
                                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setWhen(System.currentTimeMillis())
                                        .setContentIntent(PendingIntent.getActivity(this@NotificationService, 0, contentIntent, 0))
                                        .build()
                                val notificationManager = getSystemService(NotificationManager::class.java)
                                notificationManager.notify(1234, notification)
                            }
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }

                override fun onProviderEnabled(provider: String?) {
                }

                override fun onProviderDisabled(provider: String?) {
                }
            }
        )
    }

    override fun onDestroy() {
        Log.i(NotificationService::class.qualifiedName, "NotificationService destroyed")
    }

}