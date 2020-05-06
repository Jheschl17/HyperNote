package net.htlgrieskirchen.jheschl17.hypernote

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_credentials_login.*
import net.htlgrieskirchen.jheschl17.hypernote.cloud.verifyCredentials

class CredentialsLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credentials_login)

        button_credentials_done.setOnClickListener {
            val username = text_username.text.toString()
            val password = text_password.text.toString()
            if (verifyCredentials(username, password)) {
                PreferenceManager.getDefaultSharedPreferences(this).edit {
                    putString("username", text_username.text.toString())
                    putString("password", text_password.text.toString())
                }
                startActivity(Intent(this@CredentialsLoginActivity, MainActivity::class.java))
            } else {
                Toast.makeText(
                    this@CredentialsLoginActivity,
                    "Invalid Credentials",
                    Toast.LENGTH_LONG
                    )
                    .show()
            }
        }

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS), 1)
    }
}
