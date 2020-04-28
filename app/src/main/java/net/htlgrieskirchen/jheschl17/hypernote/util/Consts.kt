package net.htlgrieskirchen.jheschl17.hypernote.util

import android.os.Environment.getExternalStorageDirectory
import java.io.File

const val DATE_FORMAT = "d.M.yyyy"
const val LOCATIONIQ_API_KEY = "cff3623155ab89"
const val NOTIFICATION_CHANNEL_LOCATION = "channel_location"
val NOTE_FILE_PATH = getExternalStorageDirectory().absolutePath + File.separator + "notes.json"