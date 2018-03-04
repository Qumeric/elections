package rocks.che.elections.logic

import android.content.res.Resources
import org.json.JSONArray
import org.json.JSONObject
import rocks.che.elections.R

fun loadCandidate(json: JSONObject): Candidate {
    val basicStats = json.getJSONObject("basicStats")
    val basicLevels = json.getJSONObject("basicLevels")

    val opinions = mutableMapOf<String, Int>()
    val levels = mutableMapOf<String, Int>()

    for (stat in basicStats.keys()) {
        opinions[stat] = basicStats.getInt(stat)
    }

    for (level in basicLevels.keys()) {
        levels[level] = basicLevels.getInt(level)
    }

    val candidate = Candidate(
            json.getString("name"),
            json.getString("description"),
            json.getString("imgResource"),
            opinions, levels)

    val jsonHistory = json.getJSONArray("history")
    for (i in 0 until jsonHistory.length()) {
        candidate.history.add(jsonHistory.getDouble(i))
    }

    return candidate
}

fun loadCandidates(json: JSONArray): List<Candidate> {
    return (0 until json.length()).map { loadCandidate(json.getJSONObject(it)) }
}

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
        val impact = mutableMapOf<String, Int>()
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

fun loadQuestions(json: JSONObject, shuffle: Boolean = false): HashMap<String, QuestionGroup> {
    val questions = HashMap<String, QuestionGroup>()

    for (categoryName in json.keys()) {
        val jsonQG = json.get(categoryName)
        var nextQuestionIndex = 0
        val questionList = when(jsonQG) {
            is JSONArray -> {
                (0 until jsonQG.length()).map { loadQuestion(jsonQG.getJSONObject(it)) }
            }
            is JSONObject -> {
                val qs = jsonQG.getJSONArray("questions")
                nextQuestionIndex = jsonQG.getInt("nextQuestionIndex")
                (0 until qs.length()).map { loadQuestion(qs.getJSONObject(it)) }
            }
            else -> listOf() // should never be reached
        }

        questions[categoryName] = QuestionGroup(questionList as MutableList<Question>, shuffle)
        questions[categoryName]!!.nextQuestionIndex = nextQuestionIndex
    }
    return questions
}

fun loadQuestions(resources: Resources): HashMap<String, QuestionGroup> {
    val jsonString = resources.openRawResource(R.raw.questions)
            .bufferedReader().use { it.readText() }
    return loadQuestions(JSONObject(jsonString), true)
}

fun loadQuotes(resources: Resources): List<Quote> {
    val jsonString = resources.openRawResource(R.raw.quotes)
            .bufferedReader().use { it.readText() }
    val jsonQuotes = JSONArray(jsonString)

    val quotes = mutableListOf<Quote>()

    for (q in 0 until jsonQuotes.length()) {
        val jsonQuote = jsonQuotes.getJSONObject(q)
        quotes.add(Quote(jsonQuote.getString("quote"), jsonQuote.getString("author")))
    }

    return quotes
}

fun loadGameState(json: JSONObject): Gamestate {
    val candidate = loadCandidate(json.getJSONObject("candidate"))

    val jsonQuestions = json.getJSONObject("questions")
    val questions = loadQuestions(jsonQuestions)

    val jsonCandidates = json.getJSONArray("candidates")
    val candidates = mutableListOf<Candidate>()
    for (i in 0 until jsonCandidates.length()) {
        candidates.add(loadCandidate(jsonCandidates.getJSONObject(i)))
    }

    val gs = Gamestate(candidate, questions, candidates)
    gs.money = json.getInt("money")
    gs.step = json.getInt("step")

    return gs
}