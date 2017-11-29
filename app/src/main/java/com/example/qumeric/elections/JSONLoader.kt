package com.example.qumeric.elections

import android.content.Context
import org.json.JSONArray

fun loadCandidates(context: Context): List<Candidate> {
    val jsonString = context.resources.openRawResource(R.raw.candidates)
            .bufferedReader().use { it.readText() }
    val reader = JSONArray(jsonString)
    for (i in 0 until reader.length()) {
        val JSONcandiate = reader.getJSONObject(i)

    }
}
