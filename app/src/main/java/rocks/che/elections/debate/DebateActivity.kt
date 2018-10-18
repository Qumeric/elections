package rocks.che.elections.debate

import android.os.Bundle
import android.util.Log
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.PollActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import rocks.che.elections.logic.inActivityChange
import java.util.*


class NextDebateStage

class DebateActivity : DefaultActivity() {
    private var stage = 0
    private val groupDistribution = mutableMapOf<String, Int>()
    private val opponentDistribution = mutableMapOf<String, Int>()
    private lateinit var gs: Gamestate
    val maxMinutes = 60
    var groupMinutes = maxMinutes / 2
    var opponentMinutes = maxMinutes - groupMinutes

    fun setGroupDistribution(d: List<Int>) =
        gs.questions.all.keys.withIndex().forEach { (i, k) -> groupDistribution[k] = d[i] }

    //FIXME order dependent...
    fun setOpponentDistribution(d: List<Int>) =
        gs.candidates.filter { it.resource != gs.candidate.resource }.withIndex().forEach { (i, c) ->
            opponentDistribution[c.name] = d[i]
        }

    fun nextStage(e: NextDebateStage = NextDebateStage()) {
        Log.d("DebateActivity", "nextStage event!")
        inActivityChange = true
        when (++stage) {
            1 -> DebateViewStart(gs.candidate.resource).setContentView(this)
            2 -> DebateViewChoose().setContentView(this)
            3 -> DebateViewGroups(groupMinutes, gs.questions).setContentView(this)
            4 -> DebateViewOpponents(opponentMinutes, gs.candidates.filter { it.resource != gs.candidate.resource }).setContentView(this)
            5 -> DebateViewEnd(gs.candidate, winGroup(), loseGroup(), attackResult()).setContentView(this)
            else -> startActivity<PollActivity>("gamestate" to gs)
        }
    }

    fun timeDistributionUpdate(gm: Int, om: Int) {
        groupMinutes = gm
        opponentMinutes = om
    }

    private val winPoints = 5 // FIXME do something smarter
    private fun winGroup(): String? {
        val r = Random().nextInt(groupMinutes + 1)
        var sum = 0
        for ((group, time) in groupDistribution) {
            sum += time
            if (sum >= r) {
                gs.candidate.opinions[group] = gs.candidate.opinions[group]!! + winPoints
                gs.updateCandidate()
                return group
            }
        }
        return null
    }

    private val losePoints = -5
    private fun loseGroup(): String? {
        val r = Random().nextDouble()
        var sum = 0f
        for ((group, time) in groupDistribution) {
            sum += 1 / (time + 1)
            if (sum > r) {
                gs.candidate.opinions[group] = gs.candidate.opinions[group]!! + losePoints
                gs.updateCandidate()
                return group
            }
        }
        return null
    }

    private fun attackResult(): String? {
        if (opponentMinutes == 0) return null
        val r = Random().nextInt(opponentMinutes + 1)
        var sum = 0
        for ((opponent, time) in opponentDistribution) {
            gs.getCandidate(opponent).boost += losePoints.toFloat() * time / opponentMinutes
        }
        for ((opponent, time) in opponentDistribution) {
            sum += time
            if (sum > r) {
                gs.getCandidate(opponent).boost += losePoints.toFloat()
                return opponent
            }
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gs = intent.getParcelableExtra("gamestate")

        nextStage(NextDebateStage())
    }
}