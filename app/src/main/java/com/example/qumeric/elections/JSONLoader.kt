package com.example.qumeric.elections

import android.content.Context
import android.content.res.Resources
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

        val candidate = Candidate(
                JSONcandiate.getString("name"),
                JSONcandiate.getString("description"),
                opinions, levels)

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
                    JSONquestion.getString("description"),
                    answers
            ))
        }

        questions[categoryName] = QuestionGroup(questionList)
    }
    return questions
}