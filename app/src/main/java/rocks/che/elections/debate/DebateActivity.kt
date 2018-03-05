package rocks.che.elections.debate

import android.os.Bundle
import com.squareup.otto.Subscribe
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import rocks.che.elections.PollActivity
import rocks.che.elections.helpers.*
import rocks.che.elections.logic.gamestate
import java.util.*

class DebateActivity : DefaultActivity() {
    private var stage = 0
    private val groupDistribution = mutableMapOf<String, Int>()
    private val opponentDistribution = mutableMapOf<String, Int>()
    val maxMinutes = 60
    var groupMinutes = maxMinutes/2
    var opponentMinutes = maxMinutes-groupMinutes

    @Subscribe fun setGroupDistribution(e: SetGroupDistribution) =
        gamestate.questions.keys.withIndex().forEach {(i, k) -> groupDistribution[k] = e.vals[i]}

    @Subscribe fun setOpponentDistribution(e: SetOpponentDistribution) =
        gamestate.questions.keys.withIndex().forEach {(i, k) -> opponentDistribution[k] = e.vals[i]}

    @Subscribe fun nextStage(e: NextDebateStage) {
        when(++stage) {
            1 -> DebateViewStart(bus).setContentView(this)
            2 -> DebateViewChoose(bus).setContentView(this)
            3 -> DebateViewGroups(groupMinutes, bus).setContentView(this)
            4 -> DebateViewOpponents(opponentMinutes, bus).setContentView(this)
            5 -> DebateViewEnd(gamestate.candidate, winGroup(), loseGroup(), attackResult(), bus).setContentView(this)
            else -> startActivity<PollActivity>()
        }
    }

    @Subscribe fun timeDistributionUpdate(e: DebateTimeDistributionUpdate) {
        groupMinutes = e.groupMinutes
        opponentMinutes = e.opponentMinutes
    }

    private val winPoints = 5 // FIXME do something smarter
    fun winGroup(): String {
        val r = Random().nextInt(groupMinutes)
        var sum = 0
        for ((group, time) in groupDistribution) {
            sum += time
            if (sum > r) {
                gamestate.candidate.opinions[group]!! += winPoints
                return group
            }
        }
        throw Exception("winGroup returned nothing")
    }

    private val losePoints = -5
    fun loseGroup(): String {
        val r = Random().nextDouble()
        var sum = 0f
        for ((group, time) in groupDistribution) {
            sum += 1/time // FIXME possible divide by zero
            if (sum > r) {
                gamestate.candidate.opinions[group]!! += losePoints
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

        nextStage(nextDebateStage)
    }
}