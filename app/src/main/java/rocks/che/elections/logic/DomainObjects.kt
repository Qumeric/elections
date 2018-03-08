package rocks.che.elections.logic

import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.otto.Bus
import kotlinx.android.parcel.Parcelize
import rocks.che.elections.helpers.groupToResource
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
                val history: MutableList<Double>) : Comparable<Candidate>, Parcelable {
    val generalOpinion get() = opinions.values.sum().toDouble() / opinions.size

    init {
        history.add(generalOpinion)
    }

    fun update() {
        // TODO add rating if not player candidate
        history.add(generalOpinion)
    }

    override fun compareTo(other: Candidate) = (generalOpinion - other.generalOpinion).roundToInt()
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
data class Answer(val statement: String, val impact: Opinions) : Parcelable

@Parcelize
class Questions(val all: HashMap<String, MutableList<Question>>,
                val answered: HashMap<String, Int> = groupToResource.keys.associateBy({ it }, { 0 }) as HashMap) : Parcelable {
    init {
        all.values.forEach { it.shuffle() }
    }

    fun get(group: String): Question {
        val ptr = answered[group]!!
        answered[group] = (ptr + 1) % all[group]!!.size
        return all[group]!![ptr]
    }
}

const val pollFrequency = 5

// FIXME grudinin money bonus
@Parcelize
class Gamestate(val candidate: Candidate, val candidates: MutableList<Candidate>, val questions: Questions,
                var step: Int = 0, var money: Int = 0, var nextDebates: Boolean = true,
                var lastGroup: String? = null) : Parcelable {
    val isPollTime get() = step != 0 && step % pollFrequency == 0
    val worst get() = candidates.min()
    val isWorst get() = worst == candidate
    val isBest get() = candidates.max() == candidate
    val isWon get() = candidates.size == 2 && isBest

    fun update(impact: Opinions) {
        candidate.opinions.forEach { (k, v) ->
            candidate.opinions[k] = impact[k] ?: 0 + v
        }
        candidates.forEach { it.update() }
    }

    fun save() {
        val json = Gson().toJson(this);
        Prefs.putString("gamestate", json)
    }

    companion object {
        fun loadGame(): Gamestate? {
            if (!Prefs.contains("gamestate")) {
                return null
            }
            return try {
                val json = Prefs.getString("location", null) ?: return null
                return Gson().fromJson(json, Gamestate::class.java);
            } catch (e: Exception) {
                Log.e("loadGame", "Unable to load existing save")
                Prefs.remove("gamestate")
                null
            }
        }
    }
}

val bus = Bus()

data class ChangeStepEvent(val step: Int)
data class ChangeMoneyEvent(val money: Int)
data class ChangeScoreEvent(val score: Int)
data class ChangeOpinionEvent(val group: String, val value: Int)
