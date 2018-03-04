package rocks.che.elections.logic

import android.content.Context

import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Writer
val filename = "che.rocks.saveGame"

@Throws(IOException::class, JSONException::class)
fun loadGame(ctx: Context): Gamestate? {
    var state: Gamestate? = null
    try {
        ctx.openFileInput(filename).use { `in` ->
            BufferedReader(InputStreamReader(`in`)).use { reader ->

                val jsonString = StringBuilder()
                var line = reader.readLine()
                while (line != null) {
                    // line breaks are omitted and irrelevant
                    jsonString.append(line)
                    line = reader.readLine()
                }
                // parse the JSON using JSONTokener
                val jsonState = JSONTokener(jsonString.toString()).nextValue() as JSONObject
                state = loadGameState(jsonState)
            }
        }
    } catch (e: FileNotFoundException) {
        // should never happen
    }

    return state
}

@Throws(JSONException::class, IOException::class)
fun saveGame(state: Gamestate, ctx: Context) {
    val jsonState = state.toJSON()

    // Write the file to disk
    ctx.openFileOutput(filename, Context.MODE_PRIVATE).use { out -> OutputStreamWriter(out).use { writer -> writer.write(jsonState.toString()) } }
}

@Throws(IOException::class)
fun deleteGame(ctx: Context) {
    ctx.deleteFile(filename)
}

fun checkExistance(ctx: Context): Boolean? {
    val file = ctx.getFileStreamPath(filename)
    return file != null
}
