package net.htlgrieskirchen.jheschl17.hypernote.cloud.api

import android.os.AsyncTask
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.htlgrieskirchen.jheschl17.hypernote.util.Note
import net.htlgrieskirchen.jheschl17.hypernote.util.Priority
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private const val BASE_PATH = "http://sickinger-solutions.at/notesserver"

/**
 * returns: id of newly registered user. Null if registration fails.
 */
fun register(
    username: String,
    password: String,
    name: String
): Int? {
    return try {
        val ret = GsonBuilder().create().fromJson(
            post(
                "${BASE_PATH}/register.php",
                GsonBuilder().create().toJson(ApiRegisterIn(username, password, name)).toByteArray()
            ),
            ApiRegisterOut::class.java
        )
        ret.userId
    } catch (e: Exception) {
        null
    }
}

class ApiRegisterTask: AsyncTask<ApiRegisterIn, Void, Int?>() {
    override fun doInBackground(vararg params: ApiRegisterIn): Int? {
        return register(params[0].username, params[0].password, params[0].name)
    }
}

data class ApiRegisterIn(
    val username: String,
    val password: String,
    val name: String
)

data class ApiRegisterOut(
    val userId: Int
)


/**
 * returns: id of newly created todolist.
 */
fun createTodoList(
    username: String,
    password: String,
    name: String,
    additionalData: String
): Int {
    val ret = GsonBuilder().create().fromJson(
        post(
            "${BASE_PATH}/todolists.php?username=${username}&password=${password}",
            GsonBuilder().create().toJson(ApiCreateTodoListJson(name, additionalData)).toByteArray()
        ),
        ApiCreateTodoListOut::class.java
    )
    return ret.id
}

class ApiCreateTodoListTask: AsyncTask<ApiCreateTodoListIn, Void, Int?>() {
    override fun doInBackground(vararg params: ApiCreateTodoListIn): Int? {
        return createTodoList(params[0].username, params[0].password, params[0].name, params[0].additionalData)
    }
}

data class ApiCreateTodoListJson(
    val name: String,
    val additionalData: String
)

data class ApiCreateTodoListIn(
    val username: String,
    val password: String,
    val name: String,
    val additionalData: String
)

data class ApiCreateTodoListOut(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val additionalData: String
)


fun getTodoList(
    username: String,
    password: String,
    id: Int
): ApiTodoList? {
    return GsonBuilder().create().fromJson(
        get(
            "${BASE_PATH}/todolists.php?username=${username}&password=${password}"
        ),
        ApiTodoList::class.java
    )
}

class ApiGetTodoListTask: AsyncTask<ApiGetTodoListIn, Void, ApiTodoList?>() {
    override fun doInBackground(vararg params: ApiGetTodoListIn): ApiTodoList? {
        return getTodoList(params[0].username, params[0].password, params[0].id)
    }
}

data class ApiGetTodoListIn(
    val username: String,
    val password: String,
    val id: Int
)

data class ApiTodoList(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val additionalData: String
)


fun getAllTodoLists(
    username: String,
    password: String
): List<ApiTodoList>? {
    return try {
        GsonBuilder().create().fromJson(
            get(
                "${BASE_PATH}/todolists.php?username=${username}&password=${password}"
            ),
            object:TypeToken<ArrayList<ApiTodoList>>() {}.type
        )
    } catch (e: Exception) {
        null
    }
}

class ApiGetAllTodoListsTask: AsyncTask<ApiGetAllTodoListsIn, Void, List<ApiTodoList>?>() {
    override fun doInBackground(vararg params: ApiGetAllTodoListsIn): List<ApiTodoList>? {
        return getAllTodoLists(params[0].username, params[0].password)
    }
}

data class ApiGetAllTodoListsIn(
    val username: String,
    val password: String
)


fun editTodoList(
    username: String,
    password: String,
    id: Int,
    name: String,
    additionalData: String
): ApiCreateTodoListOut {
    TODO()
}

fun deleteTodoList(
    username: String,
    password: String,
    id: Int
) {
    TODO()
}

/**
 * returns: id of newly created todo
 */
fun createTodo(
    username: String,
    password: String,
    todoListId: Int,
    title: String,
    description: String,
    dueDate: String,
    state: String,
    additionalData: String
): Int {
    val ret = GsonBuilder().create().fromJson(
        post(
            "${BASE_PATH}/todo.php?username=${username}&password=${password}",
            GsonBuilder()
                .create()
                .toJson(ApiCreateTodoInJson(todoListId, title, description, dueDate, state, additionalData))
                .toByteArray()
        ),
        ApiTodo::class.java
    )
    return ret.id
}

class ApiCreateTodoTask: AsyncTask<ApiCreateTodoIn, Void, Int>() {
    override fun doInBackground(vararg params: ApiCreateTodoIn): Int {
        return createTodo(
            params[0].username,
            params[0].password,
            params[0].todoListId,
            params[0].title,
            params[0].description,
            params[0].dueDate,
            params[0].state,
            params[0].additionalData
        )
    }
}

data class ApiCreateTodoInJson(
    val todoListId: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val state: String,
    val additionalData: String
)

data class ApiCreateTodoIn(
    val username: String,
    val password: String,
    val todoListId: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val state: String,
    val additionalData: String
)

fun noteToApiCreateTodoIn(note: Note, username: String, password: String, todoListId: Int): ApiCreateTodoIn {
    return ApiCreateTodoIn(
        username = username,
        password = password,
        todoListId = todoListId,
        title = note.title,
        description = note.content,
        dueDate = """${note.dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 01:00:00""",
        state = note.completed.toString(),
        additionalData = "${note.priority};${note.category}"
    )
}

data class ApiTodo(
    val id: Int,
    val ownerId: Int,
    val todoListId: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val state: String, // true = completed       false = not completed
    val additionalData: String // PRIORITY;CATEGORY
)

fun apiTodoToNote(apiTodo: ApiTodo): Note {
    return Note(
        title = apiTodo.title,
        content = apiTodo.description,
        priority = Priority.valueOf(apiTodo.additionalData.split(";")[0]),
        dueDate = LocalDate.parse(apiTodo.dueDate.split(" ")[0]),
        completed = apiTodo.state.toBoolean(),
        category = apiTodo.additionalData.split(";")[1]
    )
}


fun getTodo(
    username: String,
    password: String,
    id: Int
): ApiTodo {
    return GsonBuilder().create().fromJson(
        get("${BASE_PATH}/todo.php?username=${username}&password=${password}&id=${id}"),
        ApiTodo::class.java
    )
}

class ApiGetTodoTask: AsyncTask<ApiGetTodoIn, Void, ApiTodo>() {
    override fun doInBackground(vararg params: ApiGetTodoIn): ApiTodo {
        return getTodo(params[0].username, params[0].password, params[0].id)
    }
}

data class ApiGetTodoIn(
    val username: String,
    val password: String,
    val id: Int
)


fun getAllTodos(
    username: String,
    password: String
): List<ApiTodo> {
    return GsonBuilder().create().fromJson(
        get("${BASE_PATH}/todo.php?username=${username}&password=${password}"),
        object:TypeToken<ArrayList<ApiTodo>>() {}.type
    )
}

class ApiGetAllTodosTask: AsyncTask<ApiGetAllTodosIn, Void, List<ApiTodo>>() {
    override fun doInBackground(vararg params: ApiGetAllTodosIn): List<ApiTodo> {
        return getAllTodos(params[0].username, params[0].password)
    }
}

data class ApiGetAllTodosIn(
    val username: String,
    val password: String
)


fun editTodo(
    username: String,
    password: String,
    id: Int,
    todoListId: Int,
    title: String,
    description: String,
    dueDate: String,
    state: String,
    additionalData: String
): Note {
    TODO()
}

fun deleteTodo(
    username: String,
    password: String,
    id: Int
) {
    TODO()
}

private fun post(url: String, data: ByteArray): String {
    val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
    connection.doOutput = true
    connection.requestMethod = "POST"
    connection.setRequestProperty("Content-Type", "application/json")
    connection.setFixedLengthStreamingMode(data.size)
    connection.outputStream.write(data)
    connection.outputStream.flush()
    return connection.inputStream.readString()
}

private fun get(url: String): String {
    val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Content-Type", "application/json")
    return connection.inputStream.readString()
}

fun InputStream.readString(): String {
    return this.bufferedReader()
        .readLines()
        .reduce { s1, s2 -> s1 + s2}
}