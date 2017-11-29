package com.example.qumeric.elections

import android.util.Log
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

typealias Opinions = HashMap<String, Opinion>

// Candidates is a candidate which player can/does play.
class Candidate(val name: String, val description: String, _opinions: Map<String, Int>, val levels: Map<String, Int>) {
    val opinions: Opinions = hashMapOf()
    init {
        for ((group, value) in _opinions) {
            opinions[group] = Opinion()
            opinions[group]!!.add(value)
        }
    }
}


// default value (while candidate is unset)
val fakeCandidate = Candidate("Fake", "Something went wrong", mapOf(), mapOf())

class Question(val text:String, val description: String, val answers: List<Answer>): Serializable {
    fun selectAnswer(answer: Int) {
        answers[answer].select()
    }
}

class Answer(val statement:String, val impact:Map<String, Int>): Serializable {
    fun select() {
        gamestate.step += 1
        for ((groupName, delta) in impact) {
            Log.e("Answer", groupName)
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

// QuestionGroup is a wrapper around list of questions.
// It shuffles data and provides getter to get random unused answer from the list.
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

// Gamestate is a class which stores global game state.
// It should be loaded/created once and used as a singleton.
class Gamestate(candidate: Candidate, val questions: HashMap<String, QuestionGroup>) {
    var step = 1
    val opinions: Opinions = candidate.opinions
    init {
        Log.d("Gamestate", "Gamestate init")
    }

    fun getQuestion(group: String) : Question {
        return questions[group]!!.getQuestion()
    }
}

var gamestate: Gamestate = Gamestate(fakeCandidate, HashMap())