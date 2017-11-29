package com.example.qumeric.elections

import android.content.Context
import android.content.res.Resources
import org.json.JSONArray

fun loadCandidates(resources: Resources): List<Candidate> {
    val jsonString = resources.openRawResource(R.raw.candidates)
            .bufferedReader().use { it.readText() }
    val reader = JSONArray(jsonString)

    val candidates = mutableListOf<Candidate>()

    for (i in 0 until reader.length()) {
        val JSONcandiate = reader.getJSONObject(i)

        val basicStats = JSONcandiate.getJSONObject("basicStats")
        val basicLevels = JSONcandiate.getJSONObject("basicLevels")

        val opinions = mutableMapOf<String, Int>()
        val levels = mutableMapOf<String, Int>()

        for (stat in basicStats.keys()) {
            opinions[stat] = basicStats.getInt(stat)
        }

        for (level in basicLevels.keys()) {
            levels[level] = basicLevels.getInt(level)
        }

        val candidate = Candidate(
                JSONcandiate.getString("name"),
                JSONcandiate.getString("description"),
                opinions, levels)

        candidates.add(candidate)
    }
    return candidates
}
