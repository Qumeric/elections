package rocks.che.elections.debate

import android.os.Bundle
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView
import rocks.che.elections.DefaultActivity
import rocks.che.elections.PollActivity
import rocks.che.elections.logic.gamestate
import java.util.*

class DebateActivity : DefaultActivity() {
    private val views = mutableListOf<AnkoComponent<DebateActivity>>()
    private var stage = 0
    val groupDistribution = mutableMapOf<String, Int>()
    val opponentDistribution = mutableMapOf<String, Int>()
    val maxMinutes = 60
    var groupMinutes = maxMinutes/2
    var opponentMinutes = maxMinutes-groupMinutes

    fun setGroupDistribution(vals: List<Int>) {
        var cVal = 0
        for ((group, _) in gamestate.questions) {
            groupDistribution[group] = vals[cVal++]
        }
    }

    fun setOpponentDistribution(vals: List<Int>) {
        var cVal = 0
        for (candidate in gamestate.candidates) {
            opponentDistribution[candidate.name] = vals[cVal++]
        }
    }

    fun nextStage() {
        if (stage+1 == views.size) {
            startActivity(intentFor<PollActivity>())
            // FIXME assign scores
            return
        }
        views[++stage].setContentView(this)
    }

    val winPoints = 5 // FIXME do something smarter
    fun winGroup(): String {
        val r = Random().nextInt(groupMinutes)
        var sum = 0
        for ((group, time) in groupDistribution) {
            sum += time
            if (sum > r) {
                gamestate.candidate.opinions[group]!!.add(winPoints)
                return group
            }
        }
        throw Exception("winGroup returned nothing")
    }

    val losePoints = 5
    fun loseGroup(): String {
        val r = Random().nextDouble()
        var sum = 0f
        for ((group, time) in groupDistribution) {
            sum += 1/time
            if (sum > r) {
                gamestate.candidate.opinions[group]!!.add(losePoints)
                return group
            }
        }
        throw Exception("loseGroup returned nothing")
    }

    fun attackResult(): Boolean {
        return true // FIXME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        views.add(DebateViewStart())
        views.add(DebateViewChoose())
        views.add(DebateViewGroups())
        views.add(DebateViewOpponents())
        views.add(DebateViewEnd())
        views[stage].setContentView(this)
    }
}