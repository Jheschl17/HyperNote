package net.htlgrieskirchen.jheschl17.hypernote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.htlgrieskirchen.jheschl17.hypernote.util.NOTIFICATION_CHANNEL_LOCATION

/*
username: jheschl13371234
password: password
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_calendar, R.id.navigation_report, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.selectedItemId = R.id.navigation_report

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_LOCATION,
            "group location",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "notification channel for note reminders in app HyperNote"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        this.registerReceiver(SMSReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        startService(Intent(this, NotificationService::class.java))
    }

}
