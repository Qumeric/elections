package rocks.che.elections.logic

import android.content.Context

import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

const val filename = "che.rocks.saveGame"

@Throws(IOException::class, JSONException::class, FileNotFoundException::class)
fun loadGame(ctx: Context): Gamestate {
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
            return loadGameState(jsonState)
        }
    }
}

@Throws(JSONException::class, IOException::class)
fun save(ctx: Context, value: String, fName: String=filename) {
    ctx.openFileOutput(fName, Context.MODE_PRIVATE).use {
        out -> OutputStreamWriter(out).use { writer -> writer.write(value) }
    }
}

@Throws(IOException::class)
fun delete(ctx: Context, fName: String = filename) = ctx.deleteFile(fName)

fun checkExistence(ctx: Context, fName: String = filename): Boolean = ctx.getFileStreamPath(fName) != null
