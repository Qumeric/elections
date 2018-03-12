package rocks.che.elections.logic

import android.content.res.Resources
import org.json.JSONArray
import org.json.JSONObject
import rocks.che.elections.R
import rocks.che.elections.helpers.candidateResourceNameToResource

fun loadCandidate(json: JSONObject, isPlayer: Boolean = false): Candidate {
    val basicStats = json.getJSONObject("basicStats")
    val basicLevels = json.getJSONObject("basicLevels")
    val jsonPerks = json.getJSONArray("perks")
    val jsonHistory = json.getJSONArray("history")

    val opinions = Opinions()
    val levels = mutableMapOf<String, Int>()
    val perks = (0 until jsonPerks.length()).map { jsonPerks.getString(it) }
    val history = ((0 until jsonHistory.length()).map { jsonHistory.getDouble(it) }).toMutableList()

    basicStats.keys().forEach { opinions[it] = basicStats.getInt(it) }

    val boost = if (isPlayer) 0f else json.getDouble("boost").toFloat()

    basicLevels.keys().forEach { levels[it] = basicLevels.getInt(it) }
    return Candidate(
        json.getString("name"),
        perks,
        candidateResourceNameToResource[json.getString("imgResource")]!!,
        opinions, history, boost)
}

fun loadCandidates(json: JSONArray) = (0 until json.length()).map { loadCandidate(json.getJSONObject(it)) }

fun loadCandidates(resources: Resources): List<Candidate> {
    val jsonString = resources.openRawResource(R.raw.candidates)
        .bufferedReader().use { it.readText() }
    return loadCandidates(JSONArray(jsonString))
}

fun loadQuestion(json: JSONObject): Question {
    val jsonAnswers = json.getJSONArray("answers")
    val answers = mutableListOf<Answer>()

    for (a in 0 until jsonAnswers.length()) {
        val jsonAnswer = jsonAnswers.getJSONObject(a)
        val impact = Opinions()
        var statement = "EMPTY_STATEMENT"
        for (part in jsonAnswer.keys()) {
            if (part == "statement") {
                statement = jsonAnswer.getString(part)
                continue
            }
            impact[part] = jsonAnswer.getInt(part)
        }
        answers.add(Answer(statement, impact))
    }

    return Question(json.getString("statement"), answers)
}

fun loadQuestions(json: JSONObject): Questions {
    val all = hashMapOf<String, MutableList<Question>>()
    for (categoryName in json.keys()) {
        val jsonQG = json.getJSONArray(categoryName)
        all[categoryName] = (0 until jsonQG.length()).map { loadQuestion(jsonQG.getJSONObject(it)) }.toMutableList()
    }
    return Questions(all)
}

fun loadQuestions(resources: Resources): Questions {
    val jsonString = resources.openRawResource(R.raw.questions)
        .bufferedReader().use { it.readText() }
    return loadQuestions(JSONObject(jsonString))
}

fun loadQuotes(resources: Resources): List<Quote> {
    val jsonString = resources.openRawResource(R.raw.quotes)
        .bufferedReader().use { it.readText() }
    val jsonQuotes = JSONArray(jsonString)

    return (0 until jsonQuotes.length()).map {
        val jsonQuote = jsonQuotes.getJSONObject(it)
        Quote(jsonQuote.getString("quote"), jsonQuote.getString("author"))
    }
}
