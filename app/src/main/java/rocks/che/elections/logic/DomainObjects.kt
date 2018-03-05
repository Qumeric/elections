package rocks.che.elections.logic

import org.json.JSONArray
import org.json.JSONObject
import rocks.che.elections.R
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

typealias Opinions = HashMap<String, Opinion>

// Candidates is a candidate which player can/does play.
class Candidate(val name: String, private val description: String, val perks: List<String>,
                private val imgResource: String, _opinions: Map<String, Int>,
                private val levels: Map<String, Int>, val history: MutableList<Double> = mutableListOf()): Comparable<Candidate> {
    val opinions: Opinions = hashMapOf()
    private var boost = 0 // used only for opponents
    var resource: Int = 0
        get () = when(imgResource) {
            "navalny" -> R.drawable.navalny
            "sobchak" -> R.drawable.sobchak
            "zhirinovsky" -> R.drawable.zhirinovsky
            "putin" -> R.drawable.putin
            "grudinin" -> R.drawable.grudinin
            "yavlinsky" -> R.drawable.yavlinsky
            else -> 0 // should never be reached
        }

    init {
        for ((group, value) in _opinions) {
            opinions[group] = Opinion()
            opinions[group]!! += value
        }
        history.add(generalOpinion())
    }

    fun generalOpinion(): Double {
        var total: Double = 0.toDouble()
        for ((_, value) in opinions) {
            total += value.value
        }
        return (total / opinions.size) + boost
    }

    fun update() {
        if (name != gamestate.candidate.name) {
            boost += Random().nextInt(6)
        }
        history.add(generalOpinion())
    }


    fun toJSON(): JSONObject {
        val json = JSONObject()

        val basicStats = JSONObject()
        for ((group, op) in opinions) {
            basicStats.put(group, op.value)
        }

        val basicLevels = JSONObject()
        for ((name, lvl) in levels) {
            basicLevels.put(name, lvl)
        }

        json.put("imgResource", imgResource)
        json.put("name", name)
        json.put("description", description)
        json.put("basicStats", basicStats)
        json.put("basicLevels", basicLevels)
        json.put("history", JSONArray(history))
        json.put("perks", JSONArray(perks))

        return json
    }

    override fun compareTo(other: Candidate): Int = (generalOpinion()-other.generalOpinion()).roundToInt()
}

// default value (while candidate is unset)
val fakeCandidate = Candidate("Fake", "Something went wrong", listOf(),"navalny", mapOf(), mapOf())

data class Quote(val text: String, val author: String) : Serializable

class Question(val statement: String, val answers: List<Answer>) : Serializable {
    fun toJSON(): JSONObject {
        val json = JSONObject()

        json.put("statement", statement)
        json.put("answers", JSONArray(answers.map { it.toJSON() }))

        return json
    }
}

class Answer(val statement: String, private val impact: Map<String, Int>) : Serializable {
    // This function updates global gamestate.
    // An example of bad design...
    fun select() {
        gamestate.update()
        for ((groupName, delta) in impact) {
            if (groupName in gamestate.opinions.keys) {
                gamestate.opinions[groupName]!! += delta
            }
        }
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("statement", statement)
        for ((group, v) in impact) {
            json.put(group, v)
        }
        return json
    }
}

// Opinion is a wrapper around integer.
// It represents level of support from the specific group.
class Opinion(var value: Int = 0, private val limit: Int = 100) : Serializable {
    operator fun plusAssign(x: Int){
        value = maxOf(minOf(value+x, limit), 0)
    }
}

// QuestionGroup is a wrapper around list of questions.
// It shuffles data and provides getter to get random unused answer from the list.
class QuestionGroup(private val questions: MutableList<Question>, shuffle:Boolean = true) {
    var nextQuestionIndex = 0

    init {
        if (shuffle) {
            val rg = Random()
            for (i in 0 until questions.size) {
                val randomPosition = rg.nextInt(questions.size)
                questions[i] = questions[randomPosition].also { questions[randomPosition] = questions[i] }
            }
        }
    }

    fun getQuestion(): Question {
        nextQuestionIndex %= questions.size
        return questions[nextQuestionIndex++]
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put("nextQuestionIndex", nextQuestionIndex)
        json.put("questions", JSONArray(questions))
        return json
    }
}

// Gamestate is a class which stores global game state.
// It should be loaded/created once and used as a singleton.
class Gamestate(val candidate: Candidate, val questions: Map<String, QuestionGroup>,
                val candidates: MutableList<Candidate>) {
    private val pollFrequency = 5
    var step = 0
    val opinions: Opinions = candidate.opinions
    var money = 0

    init {
        candidates.removeAll { it.name == candidate.name }
    }

    fun toJSON(): JSONObject {
        val json = JSONObject()

        val jsonQuestions = JSONObject()
        for ((group, qGroup) in questions) {
            jsonQuestions.put(group, qGroup.toJSON())
        }

        json.put("candidate", candidate.toJSON())
        json.put("questions", jsonQuestions)
        json.put("candidates", JSONArray(candidates.map { it.toJSON() }))
        json.put("money", money)
        json.put("step", step)
        return json
    }

    fun isPollTime() = step != 0 && step % pollFrequency == 0

    fun isLost() = candidates.all {it.generalOpinion() > candidate.generalOpinion()}

    fun isWon() = candidates.size == 1 && candidate.generalOpinion() > candidates[0].generalOpinion()

    // Removes the weakest candidate.
    // Returns its name.
    fun expel(): String {
        val weakest = candidates.min()
        candidates.remove(weakest)
        return weakest!!.name
    }

    fun update() {
        step++
        candidate.update()
        candidates.forEach { it.update()}
    }
}

var gamestate: Gamestate = Gamestate(fakeCandidate, HashMap(), mutableListOf()) // FIXME

fun getGroupResource(group: String): Int {
    return when(group) {
        "media" -> R.drawable.media
        "business" -> R.drawable.business
        "foreign" -> R.drawable.foreign
        "workers" -> R.drawable.workers
        "military" -> R.drawable.military
        else -> 0 // should never be reached
    }
}

