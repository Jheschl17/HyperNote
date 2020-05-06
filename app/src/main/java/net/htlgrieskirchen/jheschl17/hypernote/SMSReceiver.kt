package net.htlgrieskirchen.jheschl17.hypernote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import net.htlgrieskirchen.jheschl17.hypernote.ui.report.getInstance
import net.htlgrieskirchen.jheschl17.hypernote.util.noteFromSmsString
import java.lang.Exception

private val TAG = SMSReceiver::class.java.name

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive entered.")
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsList = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            smsList.forEach {
                try {
                    val editDayActivity = getInstance()
                    val note =
                        noteFromSmsString(it.displayMessageBody, editDayActivity.requireActivity())
                    editDayActivity.notes.add(note)
                    editDayActivity.adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(context, "Invalid Note Format", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}