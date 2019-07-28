package rocks.che.elections.logic

import android.content.res.Resources
import com.pixplicity.easyprefs.library.Prefs
import org.json.JSONArray
import org.json.JSONObject
import rocks.che.elections.R
import rocks.che.elections.helpers.candidateResourceNameToResource
import rocks.che.elections.helpers.isRussian

fun loadCandidate(json: JSONObject, isPlayer: Boolean = false): Candidate {
    val basicStats = json.getJSONObject("basicStats")
    val basicLevels = json.getJSONObject("basicLevels")
    val jsonPerks = json.getJSONArray("perks")
    val jsonHistory = json.getJSONArray("history")

    val name = json.getString("name")
    val opinions = Opinions()
    val levels = mutableMapOf<String, Int>()
    val perks = (0 until jsonPerks.length()).map { jsonPerks.getString(it) }
    val history = ((0 until jsonHistory.length()).map { jsonHistory.getDouble(it) }).toMutableList()

    basicStats.keys().forEach { opinions[it] = basicStats.getInt(it) }

    val boost = if (isPlayer) 0f else json.getDouble("boost").toFloat()

    val isLocked = name in listOf("Ovalny", "ОвальныЙ") && !Prefs.contains(secretFilename)

    basicLevels.keys().forEach { levels[it] = basicLevels.getInt(it) }
    return Candidate(
        name,
        perks,
        candidateResourceNameToResource[json.getString("imgResource")]!!,
        opinions, history, boost, isLocked)
}

fun loadCandidates(json: JSONArray) = (0 until json.length()).map { loadCandidate(json.getJSONObject(it)) }

fun loadCandidates(resources: Resources): List<Candidate> {
    val isRus = isRussian(resources)
    val jsonString = resources.openRawResource(if (isRus) R.raw.candidates else R.raw.candidates_eng)
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
    val isRus = isRussian(resources)
    val jsonString = resources.openRawResource(if (isRus) R.raw.questions else R.raw.questions_eng)
        .bufferedReader().use { it.readText() }
    return loadQuestions(JSONObject(jsonString))
}

fun loadQuotes(resources: Resources): List<Quote> {
    val isRus = isRussian(resources)
    val jsonString = resources.openRawResource(if (isRus) R.raw.quotes else R.raw.quotes_eng)
        .bufferedReader().use { it.readText() }
    val jsonQuotes = JSONArray(jsonString)

    return (0 until jsonQuotes.length()).map {
        val jsonQuote = jsonQuotes.getJSONObject(it)
        Quote(jsonQuote.getString("quote"), jsonQuote.getString("author"))
    }
}
