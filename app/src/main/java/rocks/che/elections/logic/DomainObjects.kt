package rocks.che.elections.logic

import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.parcel.Parcelize
import rocks.che.elections.R
import rocks.che.elections.helpers.groupToResource
import java.util.*
import kotlin.math.roundToInt

const val secretFilename = "che.rocks.secret"

class Opinions : HashMap<String, Int>() {
    override fun get(key: String): Int? {
        val value = super.get(key) ?: return null
        return maxOf(0, minOf(value, 100))
    }
}

@Parcelize
class Candidate(val name: String, val perks: List<String>,
                val resource: Int, val opinions: Opinions,
                val history: MutableList<Double>, var boost: Float = 0f) : Comparable<Candidate>, Parcelable {
    val generalOpinion get() = maxOf(0.0, opinions.values.sum().toDouble() / opinions.size + boost)

    init {
        if (history.size == 0)
            history.add(generalOpinion)
    }

    fun update(isOpponent: Boolean = false) {
        if (isOpponent) {
            boost += Random().nextInt(7) - 2
            when (resource) {
                R.drawable.candidate_putin -> boost += 1
                R.drawable.candidate_yavlinsky -> boost -= 1
            }
        }
        history.add(generalOpinion)
    }

    override fun compareTo(other: Candidate): Int {
        if (generalOpinion.isNaN()||other.generalOpinion.isNaN()) {
            Log.e("Candidate", "Shitty value! $name ${other.name} $generalOpinion ${other.generalOpinion}")
        }
        return (generalOpinion - other.generalOpinion).roundToInt()
    }
}

/*val fakeCandidate = Candidate("Fake", "Something went wrong",
        (1..3).map { "blah ".repeat(it) },
        "candidate_navalny", mapOf(), mapOf())
val fakeQuestions = mapOf<String, QuestionGroup>(
        "military" to QuestionGroup(),
        "workers" to QuestionGroup(),
        "foreign" to QuestionGroup(),
        "media" to QuestionGroup(),
        "business" to QuestionGroup()
)
val fakeOpinions = hashMapOf<String, Opinion>(
        "military" to Opinion(8),
        "workers" to Opinion(16),
        "foreign" to Opinion(32),
        "media" to Opinion(64),
        "business" to Opinion(100)
)*/

@Parcelize
data class Quote(val text: String, val author: String) : Parcelable

@Parcelize
data class Question(val statement: String, val answers: List<Answer>) : Parcelable

@Parcelize
data class Answer(val statement: String, val impact: HashMap<String, Int>) : Parcelable

@Parcelize
class Questions(val all: HashMap<String, MutableList<Question>>,
                val answered: HashMap<String, Int> = groupToResource.keys.associateBy({ it }, { 0 }) as HashMap) : Parcelable {
    init {
        all.values.forEach { it.shuffle() }
    }

    fun get(group: String): Question {
        val ptr = answered[group]!!
        answered[group] = (ptr + 1) % all[group]!!.size
        return all[group]!![answered[group]!!]
    }
}

const val pollFrequency = 5

@Parcelize
class Gamestate(val candidate: Candidate, val candidates: MutableList<Candidate>, val questions: Questions,
                var step: Int = 0, var money: Int = 0, var nextDebates: Boolean = true,
                var lastGroup: String? = null) : Parcelable {
    val isPollTime get() = step != 0 && step % pollFrequency == 0
    val worst get() = candidates.min()
    val isWorst get() = worst!!.resource == candidate.resource
    val isBest get() = candidates.max()!!.resource == candidate.resource
    val isWon get() = candidates.size == 2 && isBest

    init {
        if (step == 0 && candidate.resource == R.drawable.candidate_grudinin) { // FIXME possible to cheat
            money = 50
        }
    }

    fun getCandidate(name: String): Candidate {
        return candidates.find { it.name == name }!!
    }

    fun updateCandidate() {
        val candidateInList = (candidates.filter { it.resource == candidate.resource })[0]
        candidate.opinions.forEach { (k, v) ->
            candidateInList.opinions[k] = v
        }
    }

    fun update(impact: HashMap<String, Int>) {
        val candidateInList = (candidates.filter { it.resource == candidate.resource })[0]
        candidate.opinions.forEach { (k, v) ->
            if (impact[k] != null) {
                candidate.opinions[k] = impact[k]!! * 2 + v
            }
            candidate.opinions[k] = (impact[k] ?: 0) + candidate.opinions[k]!!
            candidateInList.opinions[k] = v
        }
        candidate.update()
        candidateInList.update()
        candidates.filter { it.resource != candidate.resource }.forEach { it.update(true) }
    }

    fun save() {
        val json = Gson().toJson(this)
        Prefs.putString("gamestate", json)
    }

    companion object {
        fun loadGame(): Gamestate? {
            return try {
                val json = Prefs.getString("gamestate", null) ?: return null
                return Gson().fromJson(json, Gamestate::class.java)
            } catch (e: Exception) {
                Log.e("loadGame", "Unable to load existing save")
                Prefs.remove("gamestate")
                null
            }
        }
    }
}

var inActivityChange = false
