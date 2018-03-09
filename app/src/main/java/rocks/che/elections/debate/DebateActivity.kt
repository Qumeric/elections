package rocks.che.elections.debate

import android.os.Bundle
import android.util.Log
import com.squareup.otto.Subscribe
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.PollActivity
import rocks.che.elections.helpers.DefaultActivity
import rocks.che.elections.logic.Gamestate
import java.util.*


data class SetGroupDistribution(val vals: List<Int>)
data class SetOpponentDistribution(val vals: List<Int>)
class NextDebateStage()
class DebateTimeDistributionUpdate(val groupMinutes: Int, val opponentMinutes: Int)

class DebateActivity : DefaultActivity() {
    private var stage = 0
    private val groupDistribution = mutableMapOf<String, Int>()
    private val opponentDistribution = mutableMapOf<String, Int>()
    private lateinit var gs: Gamestate
    val maxMinutes = 60
    var groupMinutes = maxMinutes / 2
    var opponentMinutes = maxMinutes - groupMinutes

    @Subscribe
    fun setGroupDistribution(e: SetGroupDistribution) =
            gs.questions.all.keys.withIndex().forEach { (i, k) -> groupDistribution[k] = e.vals[i] }

    @Subscribe
    fun setOpponentDistribution(e: SetOpponentDistribution) =
            gs.candidates.filter { it.resource != gs.candidate.resource }.withIndex().forEach { (i, c) ->
                opponentDistribution[c.name] = e.vals[i]
            }

    @Subscribe
    fun nextStage(e: NextDebateStage = NextDebateStage()) {
        Log.d("DebateActivity", "nextStage event!")
        when (++stage) {
            1 -> DebateViewStart(gs.candidate.resource).setContentView(this)
            2 -> DebateViewChoose().setContentView(this)
            3 -> DebateViewGroups(groupMinutes, gs.questions).setContentView(this)
            4 -> DebateViewOpponents(opponentMinutes, gs.candidates.filter { it.resource != gs.candidate.resource }).setContentView(this)
            5 -> DebateViewEnd(gs.candidate, winGroup(), loseGroup(), attackResult()).setContentView(this)
            else -> startActivity<PollActivity>("gamestate" to gs)
        }
    }

    @Subscribe
    fun timeDistributionUpdate(e: DebateTimeDistributionUpdate) {
        groupMinutes = e.groupMinutes
        opponentMinutes = e.opponentMinutes
    }

    private val winPoints = 5 // FIXME do something smarter
    private fun winGroup(): String {
        val r = Random().nextInt(groupMinutes)
        var sum = 0
        for ((group, time) in groupDistribution) {
            sum += time
            if (sum > r) {
                gs.candidate.opinions[group] = gs.candidate.opinions[group]!! + winPoints
                return group
            }
        }
        throw Exception("winGroup returned nothing")
    }

    private val losePoints = -5
    private fun loseGroup(): String {
        val r = Random().nextDouble()
        var sum = 0f
        for ((group, time) in groupDistribution) {
            sum += 1 / (time + 1)
            if (sum > r) {
                gs.candidate.opinions[group] = gs.candidate.opinions[group]!! + losePoints
                return group
            }
        }
        throw Exception("loseGroup returned nothing")
    }

    private fun attackResult(): String {
        val r = Random().nextInt(opponentMinutes)
        var sum = 0
        for ((opponent, time) in opponentDistribution) {
            gs.getCandidate(opponent).boost += winPoints.toFloat() * time / opponentMinutes
        }
        for ((opponent, time) in opponentDistribution) {
            sum += time
            if (sum > r) {
                gs.getCandidate(opponent).boost += winPoints.toFloat()
                return opponent
            }
        }
        throw Exception("winGroup returned nothing")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gs = intent.getParcelableExtra("gamestate")

        nextStage(NextDebateStage())
    }
}