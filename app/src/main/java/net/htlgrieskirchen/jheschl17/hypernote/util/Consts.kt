package net.htlgrieskirchen.jheschl17.hypernote.util

import android.os.Environment.getExternalStorageDirectory
import java.io.File

const val DATE_FORMAT = "d.M.yyyy"
val NOTE_FILE_PATH = getExternalStorageDirectory().absolutePath + File.separator + "notes.json"