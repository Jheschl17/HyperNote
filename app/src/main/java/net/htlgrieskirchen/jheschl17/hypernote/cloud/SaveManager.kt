package net.htlgrieskirchen.jheschl17.hypernote.cloud

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.htlgrieskirchen.jheschl17.hypernote.cloud.api.*
import net.htlgrieskirchen.jheschl17.hypernote.util.NOTE_FILE_PATH
import net.htlgrieskirchen.jheschl17.hypernote.util.Note
import net.htlgrieskirchen.jheschl17.hypernote.util.registerLocalDate
import java.io.*
import java.lang.Exception

fun loadNotes(activity: Activity, username: String, password: String): List<Note> {
    return getTodoListId(activity)?.let { loadNotesNetwork(username, password, it) } ?: mutableListOf<Note>()
}

private fun loadNotesNetwork(username: String, password: String, id: Int): List<Note> {
    return ApiGetAllTodosTask().execute(ApiGetAllTodosIn(username, password)).get()
        .filter { it.todoListId == id }
        .map(::apiTodoToNote)
}


fun saveNotes(notes: List<Note>, activity: Activity, username: String, password: String) {
    val newListId = ApiCreateTodoListTask()
        .execute(ApiCreateTodoListIn(username, password, " ", " "))
        .get()!!
    notes.forEach {
        ApiCreateTodoTask().execute(noteToApiCreateTodoIn(it, username, password, newListId))
    }
    setTodoListId(activity, newListId)
}


private const val CUR_ID_FILE_NAME: String = "cur_id.txt"

private fun getTodoListId(activity: Activity): Int? { // TODO make dependent on username
    return activity.openFileInput(CUR_ID_FILE_NAME).readString().toIntOrNull()
}

private fun setTodoListId(activity: Activity, id: Int) { // TODO make dependent on username
    val br = activity.openFileOutput(CUR_ID_FILE_NAME, Context.MODE_PRIVATE).bufferedWriter()
    br.write(id.toString())
    br.flush()
    br.close()
}


fun verifyCredentials(username: String, password: String): Boolean {
    Log.d("SaveManager::verifyCredentials", "Entered function")
    val userId = ApiRegisterTask().execute(ApiRegisterIn(username, password, " ")).get()
    if (userId != null)
        return true

    val todoLists = ApiGetAllTodoListsTask().execute(ApiGetAllTodoListsIn(username, password)).get()
    return todoLists != null
}
