package com.example.qumeric.elections

import android.content.res.Resources
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

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

        val resName = JSONcandiate.getString("imgResource")

        val candidate = Candidate(
                JSONcandiate.getString("name"),
                JSONcandiate.getString("description"),
                resources.getIdentifier(resName, "drawable", "com.example.qumeric.elections"),
                opinions, levels)

        candidates.add(candidate)
    }
    return candidates
}

fun loadFakeCandidates(resources: Resources): MutableList<FakeCandidate> {
    val jsonString = resources.openRawResource(R.raw.fakecandidates)
            .bufferedReader().use { it.readText() }
    val reader = JSONArray(jsonString)

    val candidates = mutableListOf<FakeCandidate>()

    for (i in 0 until reader.length()) {
        val JSONcandidate = reader.getJSONObject(i)

        Log.d("loadFakeCandidates", "Name is: " + JSONcandidate.getString("name"))

        val candidate = FakeCandidate(
                JSONcandidate.getString("name"),
                JSONcandidate.getString("description"),
                JSONcandidate.getDouble("level")
        )

        candidates.add(candidate)
    }
    return candidates
}

fun loadQuestions(resources: Resources): HashMap<String, QuestionGroup> {
    val jsonString = resources.openRawResource(R.raw.questions)
            .bufferedReader().use { it.readText() }
    val reader = JSONObject(jsonString)

    val questions = HashMap<String, QuestionGroup>()

    for (categoryName in reader.keys()) {
        val questionList = mutableListOf<Question>()
        val JSONcategory = reader.getJSONArray(categoryName)
        for (q in 0 until JSONcategory.length()) {
            val JSONquestion = JSONcategory.getJSONObject(q)
            val JSONanswers = JSONquestion.getJSONArray("answers")
            val answers = mutableListOf<Answer>()

            for (a in 0 until JSONanswers.length()) {
                val JSONanswer = JSONanswers.getJSONObject(a)
                val impact = mutableMapOf<String, Int>()
                var statement = "EMPTY_STATEMENT"
                for (part in JSONanswer.keys()) {
                    if (part == "statement") {
                        statement = JSONanswer.getString(part)
                        continue
                    }
                    impact[part] = JSONanswer.getInt(part)
                }
                answers.add(Answer(statement, impact))
            }

            questionList.add(Question(
                    JSONquestion.getString("statement"),
                    answers
            ))
        }

        questions[categoryName] = QuestionGroup(questionList)
    }
    return questions
}

fun loadQuotes(resources: Resources): List<Quote> {
    val jsonString = resources.openRawResource(R.raw.quotes)
            .bufferedReader().use { it.readText() }
    val JSONquotes = JSONArray(jsonString)

    val quotes = mutableListOf<Quote>()

    for (q in 0 until JSONquotes.length()) {
        val JSONquote = JSONquotes.getJSONObject(q)
        quotes.add(Quote(JSONquote.getString("quote"), JSONquote.getString("author")))
    }

    return quotes
}