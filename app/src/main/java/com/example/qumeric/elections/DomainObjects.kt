package com.example.qumeric.elections

import android.util.Log
import java.io.Serializable
import java.util.*

typealias Opinions = HashMap<String, Opinion>

class Candidate(val name: String, val description: String, _opinions: Map<String, Int>) {
    val opinions: Opinions = hashMapOf()
    init {
        for ((group, value) in _opinions) {
            opinions[group] = Opinion()
            opinions[group]!!.add(value)
        }
    }
}

val candidates = listOf(Candidate(
        "Нэвэльный",
        "Make Russia блэт again",
        mapOf("party" to 50,
                "public" to 50,
                "foreign" to 50,
                "media" to 50,
                "business" to 50,
                "staff" to 50)))

// default value (while candidate is unset)
val fakeCandidate = Candidate("Fake", "Something went wrong", mapOf())

class Question(val text:String, val answers: List<Answer>): Serializable {
    fun selectAnswer(answer: Int) {
        answers[answer].select()
    }
}

class Answer(val statement:String, val impact:Map<String, Int>): Serializable {
    fun select() {
        gamestate.step += 1
        for ((groupName, delta) in impact) {
            gamestate.opinions[groupName]!!.add(delta)
        }
    }
}

// Opinion is a wrapper around integer.
// It represents level of support from the specific group.
class Opinion() : Serializable{
    var value = 0
    private var _limit = 100
    private var limit: Int
        get() = _limit
        set(value) {
            _limit = value
        }

    fun add(x : Int) {
        Log.d("OPINION", String.format("old value: %d", value))
        value = minOf(value+x, limit)
        value = maxOf(value, 0)
        Log.d("OPINION", String.format("new value: %d", value))
    }

    fun calcAdded(x: Int): Int{
        return maxOf(minOf(value+x, limit), 0)
    }
}

class QuestionGroup(val questions: MutableList<Question>) {
    var nextQuestionIndex = 0
    init {
        // Shuffle list of questions
        val rg = Random()
        for (i in 0 until questions.size) {
            val randomPosition = rg.nextInt(questions.size)
            val tmp = questions[i]
            questions[i] = questions[randomPosition]
            questions[randomPosition] = tmp
        }
    }
    fun getQuestion() : Question {
        nextQuestionIndex %= questions.size
        return questions[nextQuestionIndex++]
    }
}

class Gamestate(candidate: Candidate) {
    var step = 1
    val questions: HashMap<String, QuestionGroup> = hashMapOf()
    val opinions: Opinions = candidate.opinions
    init {
        Log.d("GAMESTATE", "Gamestate init")
        val groups = mapOf(
                "party" to mutableListOf(
                        Question("party question", listOf(
                                Answer("yes", mapOf("party" to 1)),
                                Answer("no", mapOf("party" to -1)))
                        )
                ),
                "public" to mutableListOf(
                        Question("public question", listOf(
                                Answer("yes", mapOf("public" to 1)),
                                Answer("no", mapOf("public" to -1)))
                        )
                ),
                "media" to mutableListOf(
                        Question("media question", listOf(
                                Answer("yes", mapOf("media" to 1)),
                                Answer("no", mapOf("media" to -1)))
                        )
                ),
                "foreign" to mutableListOf(
                        Question("foreign question", listOf(
                                Answer("yes", mapOf("foreign" to 1)),
                                Answer("no", mapOf("foreign" to -1)))
                        )
                ),
                "business" to mutableListOf(
                        Question("business question", listOf(
                                Answer("yes", mapOf("business" to 1)),
                                Answer("no", mapOf("business" to -1)))
                        )
                ),
                "staff" to mutableListOf(
                        Question("staff question", listOf(
                                Answer("yes", mapOf("staff" to 1)),
                                Answer("no", mapOf("staff" to -1)))
                        )
                ))
        for ((group, qList) in groups) {
            questions[group] = QuestionGroup(qList)
        }
    }

    fun getQuestion(group: String) : Question {
        return questions[group]!!.getQuestion()
    }
}

var gamestate: Gamestate = Gamestate(fakeCandidate)